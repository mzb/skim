package pk.dyplom.eval;

import com.mindfusion.diagramming.DiagramLink;
import com.mindfusion.diagramming.DiagramLinkList;
import com.mindfusion.diagramming.DiagramNode;
import pk.dyplom.Utils;
import pk.dyplom.diagram.BaseNode;
import pk.dyplom.diagram.StartNode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kompilator przekształcający diagram do formy wykonywalnej przez ewaluator.
 */
public class Compiler {

    /** Nazwa zmiennej lub funkcji */
    public static final Pattern NAME_REGEX = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    /** Typ zmiennej lub funckji */
    public static final Pattern TYPE_REGEX = Pattern.compile(
            Utils.join(new Object[]{  Primitive.INT, Primitive.REAL, Primitive.BOOL, Primitive.STRING }, "|")
    );
    /** Separator rodzielający nazwę zmiennej i typ */
    public static final Pattern NAME_TYPE_SEPARATOR_REGEX = Pattern.compile("\\s*:\\s*");
    /** Typ tablicowy */
    public static final Pattern ARRAY_TYPE_REGEX = Pattern.compile("\\[(\\d+)\\]");
    /** Deklaracja typu */
    public static final Pattern TYPE_DECLARATION_REGEX = Pattern.compile(
            "(" + NAME_REGEX + ")(" + NAME_TYPE_SEPARATOR_REGEX + ")" +
                    "(" + TYPE_REGEX + ")((" + ARRAY_TYPE_REGEX + ")+)?"
    );
    /** Deklaracja zmiennej (`var ...`) */
    public static final Pattern VARIABLE_DECLARATION_REGEX = Pattern.compile(
            "var\\s+" + "(" + TYPE_DECLARATION_REGEX + ")" + "\\s*;\\s*"
    );
    /** Deklaracja zmiennej typu tablicowego */
    public static final Pattern ARRAY_DECLARATION_REGEX = Pattern.compile(
            "var\\s+" + "(" + NAME_REGEX + ")(" + NAME_TYPE_SEPARATOR_REGEX + ")" +
                    "(" + TYPE_REGEX + ")((" + ARRAY_TYPE_REGEX + ")+)" + "(\\s*;\\s*)"
    );
    /** Lista parametrów formalnych funkcji */
    public static final Pattern PARAMS_LIST_REGEX = Pattern.compile(
            "((" + TYPE_DECLARATION_REGEX + "(,\\s*)?" + ")+)?"
    );
    /** Inicjalizacja pustej tablicy: `... = []` */
    public static final Pattern ARRAY_EMPTY_INIT_REGEX = Pattern.compile(
            "(=\\s*)(\\[\\])"
    );
    /** Operator indeksowania tablicy: `[i]` lub `[42]` */
    public static final Pattern ARRAY_INDEX_REGEX = Pattern.compile(
            "(\\[[^\\[]+\\])"
    );
    /** Inicjalizacja kolejnego poziomu tablicy: `a[i] = []` */
    public static final Pattern ARRAY_LEVEL_INIT_REGEX = Pattern.compile(
            "(" + NAME_REGEX + ")" + "(" + ARRAY_INDEX_REGEX + "+)" + "\\s*" + ARRAY_EMPTY_INIT_REGEX
    );

    /** Zmienne lokalne wraz z typami w obrębie danego bloku startowego */
    public final Map<StartNode, Map<String, Variable>> variables = new HashMap<StartNode, Map<String, Variable>>();
    /** Parametry formalne wraz z typami dla danego bloku startowego */
    public final Map<StartNode, Map<String, Variable>> params = new HashMap<StartNode, Map<String, Variable>>();

    /** Czy generować kod śledzący zmienne? */
    private boolean trace = false;

    /**
     * Kompiluje ciąg połączonych bloków poczynając od danego bloku startowego.
     *
     * @param start Blok startowy
     * @return Wykonywalny kod
     */
    public String compile(StartNode start) {
        StringBuilder code = new StringBuilder();
        StringBuilder header = emitFunctionHeader(start);
        StringBuilder body = emitFunctionBody(start);
        code.append(header)
                .append(" {")
                .append(body)
                .append("}");
        return code.toString();
    }

    /**
     * Generuje składnie funkcji o podanej nazwie, parametrach oraz ciele.
     *
     * @param name Nazwa funkcji
     * @param paramList Lista param. formalnych rozdzielona przecinkami
     * @param body Ciało funkcji
     * @return Kod
     */
    public String functionSyntax(String name, String paramList, String body) {
        StringBuilder code = new StringBuilder();
        code.append(emitFunctionHeader(name, paramList));
        code.append("{").append(body).append("}");
        return code.toString();
    }

    /**
     * Generuje składnię deklaracji zmiennej.
     *
     * @param name Nazwa zmiennej
     * @return Kod
     */
    public String varSyntax(String name) {
        StringBuilder code = new StringBuilder();
        code.append("var ").append(name).append(";");
        return code.toString();
    }

    /**
     * Generuje składnię `return`.
     *
     * @param expr Zwracane wyrażenie
     * @return Kod
     */
    public String returnSyntax(String expr) {
        StringBuilder code = new StringBuilder();
        code.append(functionSyntax("", "",
                new StringBuilder("return ").append(expr).append(";").toString()));
        return code.toString();
    }

    /**
     * Generuje składnię wyrażenia.
     *
     * @param expr Wyrażenie
     * @return Kod
     */
    public String expressionSyntax(String expr) {
        StringBuilder code = new StringBuilder();
        code.append("(").append(expr).append(")");
        return code.toString();
    }

    /**
     * Generuje składnię `if (...) { ... }`.
     *
     * @param condition Wyrażenie warunkowe
     * @param body Kod wykonywany jeśli wyrażenie warunkowe jest prawdziwe (opcj.)
     * @return Kod
     */
    public String emitIf(String condition, String body) {
        StringBuilder code = new StringBuilder();
        code.append("if (").append(condition).append(") ");
        if (body != null) {
            code.append("{").append(body).append("}");
        }
        return code.toString();
    }

    /**
     * Generuje kod funkcji odpowiadającej danemu blokowi startowemu.
     *
     * @param startNode Blok startowy
     * @return Kod
     */
    private StringBuilder emitFunctionBody(StartNode startNode) {
        addStartNodeParamsToVars(startNode);

        StringBuilder body = new StringBuilder();

        DiagramLinkList outgoingLinks = startNode.getAllOutgoingLinks();
        if (outgoingLinks.isEmpty()) {
            return body;
        }

        body.append("var ").append(emitNext(startNode));
        body.append("var $RETURN; ");
        body.append("while ($NEXT !== null && $ENV.get('$CANCEL') != true) {");
        body.append("switch ($NEXT) {");
        emitNodeCases(startNode, body, null, startNode);
        body.append("}} ");
        body.append("return $RETURN; ");

        return body;
    }

    /**
     * Wykrywa typy parametrów formalnych danego bloku startowego.
     *
     * @param startNode Blok startowy
     * @return Nazwy oraz typy parametrów formalnych
     */
    private Map<String, Variable> addStartNodeParamsToVars(StartNode startNode) {
        List<Variable> paramList = detectTypes(startNode.getParamsAsString());
        Map<String, Variable> startNodeParams = new LinkedHashMap<String, Variable>();
        for (Variable p : paramList)
            startNodeParams.put(p.name, p);
        return params.put(startNode, startNodeParams);
    }

    /**
     * Generuje kod dla kolejnych bloków diagramu.
     * Poszczególne bloki odwiedzane są rekurencyjnie - każdy co najwyżej raz.
     *
     * @param node Aktualnie odwiedzany blok
     * @param body Dotychczas wygenerowany kod, kod dl kolejnych bloków jest do niego dopisywany
     * @param visited Zbiór już odwiedzonych bloków
     * @param startNode Aktualny blok startowy w obrębie którego generowany jest kod
     */
    private void emitNodeCases(BaseNode node, StringBuilder body, Set<DiagramNode> visited, StartNode startNode) {
        if (visited == null) {
            visited = new HashSet<DiagramNode>();
        }
        if (node == null || visited.contains(node)) {
            return;
        }
        visited.add(node);

        int index = node.getParent().getNodes().indexOf(node);
        body.append("case ").append(index).append(": ");
        if (trace) {
            body.append("$TRACE.before(").append(index).append("); ");
        }
        body.append(compileNode(node, startNode));
        if (trace) {
            emitVarsTraceCode(body, startNode);
            body.append("$TRACE.after(").append(index).append("); ");
        }
        body.append("break; ");

        for (DiagramLink link : node.getAllOutgoingLinks()) {
            emitNodeCases((BaseNode) link.getDestination(), body, visited, startNode);
        }
    }

    /**
     * Emituje kod pozwalający śledzić zmienne w czasie wykonania.
     *
     * @param body Aktualnie wygenerowany kod
     * @param startNode Bieżący blok startowy
     */
    private void emitVarsTraceCode(StringBuilder body, StartNode startNode) {
        List<Variable> paramsAndVars = new ArrayList<Variable>();
        if (params.get(startNode) != null)
            paramsAndVars.addAll(params.get(startNode).values());
        if (variables.get(startNode) != null)
            paramsAndVars.addAll(variables.get(startNode).values());

        for (Variable v : paramsAndVars) {
            body.append("$ENV.set('var.")
                    .append(startNode.getName())
                    .append(".").append(v.name).append("', ").append(v.name).append("); ");
        }
    }

    /**
     * Generuje wykonywalny kod dla danego bloku.
     *
     * @param node Kompilowany blok
     * @param startNode Bieżący blok startowy
     * @return Kod
     */
    private String compileNode(BaseNode node, StartNode startNode) {
        Map<String, Variable> startNodeVars = variables.get(startNode);
        if (startNodeVars == null) {
            startNodeVars = new LinkedHashMap<String, Variable>();
            variables.put(startNode, startNodeVars);
        }

        String code = node.compile(this);
        List<Variable> nodeVars = detectTypes(code);
        for (Variable v : nodeVars)
            startNodeVars.put(v.name, v);

        code = ignoreTypeDeclarations(code);

        return code;
    }

    /**
     * Wykrywa typy zmiennych i parametrów formalnych zdeklarowanych w podanym kodzie.
     *
     * @param code Kod
     * @return Lista zmiennych
     */
    public List<Variable> detectTypes(String code) {
        List<Variable> vars = new ArrayList<Variable>();

        Matcher varMatcher = TYPE_DECLARATION_REGEX.matcher(code);
        while (varMatcher.find()) {
            String name = varMatcher.group(1);
            Type type;
            boolean isArray = varMatcher.group(4) != null && !varMatcher.group(4).isEmpty();
            if (isArray) {
                Matcher arrayMatcher = ARRAY_TYPE_REGEX.matcher(varMatcher.group(4));
                List<Integer> sizes = new ArrayList<Integer>();
                while (arrayMatcher.find()) {
                    sizes.add(Integer.parseInt(arrayMatcher.group(1)));
                }
                type = Array.of(Primitive.fromString(varMatcher.group(3)), sizes.toArray(new Integer[]{}));
            } else {
                type = Primitive.fromString(varMatcher.group(3));
            }
            vars.add(new Variable(name, type));
        }

        return vars;
    }

    /**
     * Usuwa informacje o typach zmiennych i parametrów formalnych z podanego kodu.
     *
     * @param code Kod
     * @return Kod bez typów w deklaracjach
     */
    public static String ignoreTypeDeclarations(String code) {
        return code
                .replaceAll(ARRAY_DECLARATION_REGEX.toString(), "var $1 = []$7")
                .replaceAll(TYPE_DECLARATION_REGEX.toString(), "$1");
    }

    /**
     * Generuje nagłowek funkcji odpowiadającej danemu blokowi startowemu.
     *
     * @param startNode Blok startowy
     * @return Nagłówek funkcji
     */
    private StringBuilder emitFunctionHeader(StartNode startNode) {
        return emitFunctionHeader(startNode.getName(), emitFunctionParams(startNode));
    }

    /**
     * Generuje nagłówek funkcji o podanej nazwie i liście parametrów.
     *
     * @param name Nazwa funkcji
     * @param paramsStr Lista parametrów formalnych
     * @return Nagłowek funkcji
     */
    private StringBuilder emitFunctionHeader(String name, String paramsStr) {
        StringBuilder header = new StringBuilder();
        header.append("function ").append(name);
        header.append("(");
        header.append(ignoreTypeDeclarations(paramsStr));
        header.append(")");
        return header;
    }

    /**
     * Generuje listę parametrów formalnych dla danego bloku startowego.
     *
     * @param startNode Blok startowy
     * @return Lista parametrów
     */
    private String emitFunctionParams(StartNode startNode) {
        return Utils.join(startNode.getParams(), ", ");
    }

    /**
     * Generuje przejście do następnego bloku.
     *
     * @param next Następny blok diagramu
     * @return Kod aktualizujący indeks kolejnego bloku do wykonania
     */
    public String emitNext(BaseNode next) {
        if (next == null) {
            return emitStop();
        }
        int index = next.getParent().getNodes().indexOf(next);
        return "$NEXT = " + index + "; ";
    }

    /**
     * Oznacza aktualny blok jako ostatni w ścieżce wykonania.
     *
     * @return Kod oznaczający aktualny blok jako ostatni.
     */
    public String emitStop() {
        return "$NEXT = null; ";
    }

    /**
     * Ustawia flagę odpowiadającą za generowanie kodu na potrzeby śledzenia zmiennych.
     * @param trace Flaga, jeśli `true` dodanie zostany kod do śledzenia zmiennyc w czasie wykonania.
     */
    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    /**
     * Przypisuje daną wartość do zmiennej zwracanej z funkcji odpowiadającej blokowi startowemu.
     *
     * @param returnValue Zwracana wartość
     * @return Kod przypisujący daną wartość do zmiennej zwracanej z funkcji
     */
    public String emitReturn(String returnValue) {
        StringBuilder code = new StringBuilder();
        if (returnValue != null && !returnValue.isEmpty()) {
            code.append("$RETURN = ").append(returnValue).append("; ");
        }
        return code.toString();
    }

    /**
     * Wykonuje interpolacja zmiennych: zamienia `$zmienna` na formę `+ zmienna +`, która
     * pozwala na wstawienie do łańcucha wartości zmiennej.
     *
     * @param string Łańcuch znaków do interpolacji zmiennych
     * @return Łańcuch po interpolacji
     */
    public static String interpolate(String string) {
        return String.format("\"%s\"", interpolateWith(string, "\" + $1 + \""));
    }

    /**
     * Zamienia `$nazwa` na podaną formę.
     *
     * @param string Łańcuch w obrębie którego ma zostać wykonana zamiana
     * @param replacement Forma na którą ma być zamienione odniesie do zmiennej
     * @return Łańcuch po zamianie
     */
    public static String interpolateWith(String string, String replacement) {
        return string.replaceAll("\\$(\\w+)", replacement);
    }

    /**
     * Zamienia znaki `"` na `\"` w podanym literale łańcuchowym.
     *
     * @param string Literał łańcuchowy.
     * @return Łańcuch po zamianie
     */
    public static String escape(String string) {
        return string.replaceAll("([\"])", "\\\\$1");
    }
}
