package pk.dyplom.codegen;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import com.mindfusion.diagramming.DiagramLinkList;
import com.mindfusion.diagramming.DiagramNode;
import pk.dyplom.Utils;
import pk.dyplom.diagram.*;
import pk.dyplom.eval.*;
import pk.dyplom.eval.Compiler;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Generator kodu w C++.
 */
public class CCodeGenerator implements ICodeGenerator {

    public static final String INDENT = "  ";
    public final Compiler compiler = new Compiler();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtension() {
        return "cc";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String interpolate(String string) {
        return String.format("\"%s\"", string.replaceAll("\\$(\\w+)", "\" << $1 << \""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String escape(String string) {
        return string.replaceAll("([\"])", "\\\\$1");
    }

    /**
     * {@inheritDoc}
     *
     * Wygenerowany kod diagramu umieszczany jest w obrębie szablonu `template/c.html` zawierającym
     * niezbędne deklarace `#include` oraz definicję funkcji `main`.
     * Jeśli diagram posiada blok startowy o nazwie `Main` bezargumentowe wywołanie odpowiadającej mu funkcji
     * zostanie automatycznie umieszczone w ciele `main`.
     */
    @Override
    public String generate(Diagram diagram) {
        // Definicje funkcji
        StringBuilder functionDefs = new StringBuilder();
        for (DiagramNode n : diagram.getNodes()) {
            if (n instanceof StartNode) {
                functionDefs.append(generate((StartNode) n));
            }
        }

        // Deklaracje (naglowki) funkcji
        StringBuilder functionHeaders = new StringBuilder();
        boolean autocallMain = false;
        for (DiagramNode n : diagram.getNodes()) {
            if (n instanceof StartNode) {
                functionHeaders.append(emitFunctionHeader((StartNode) n)).append(";\n\n");
                if ("Main".equals(((StartNode) n).getName()))
                    autocallMain = true;
            }
        }

        StringBuilder code = new StringBuilder();
        code.append(functionHeaders);
        code.append(functionDefs);

        String template = readTemplate();
        return String.format(template, format(code.toString()), autocallMain ? "Main();" : "");
    }

    /**
     * Ładuje szablon (plik w `template/c.html`) w ramach którego zostanie umieszczony wygenerowany kod.
     *
     * @return Zawartość szablonu
     */
    private String readTemplate() {
        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(CCodeGenerator.class.getResourceAsStream("template/c.html"));
        final String endln = System.getProperty("line.separator");
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append(endln);
        }
        scanner.close();
        return content.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(StartNode node) {
        compiler.compile(node);

        StringBuilder code = new StringBuilder();
        String header = emitFunctionHeader(node);
        String body = emitFunctionBody(node);
        code.append(header)
                .append(" {\n")
                .append(body)
                .append("}\n\n");
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(ConditionalNode node) {
        StringBuilder code = new StringBuilder();
        String condition = convertArrayLengthToVectorSize(node.getCondition());
        code.append("if").append(" (").append(condition).append(") ");
        DiagramLink thenLink = node.getThenLink();
        code.append(emitNext(thenLink == null ? null : (BaseNode) thenLink.getDestination()));
        DiagramLink elseLink = node.getElseLink();
        code.append(" else ");
        code.append(emitNext(elseLink == null ? null : (BaseNode) elseLink.getDestination()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(EndNode node) {
        StringBuilder code = new StringBuilder();
        code.append(emitReturn(node.getReturnValue()));
        code.append(emitStop());
        return code.toString();
    }

    /**
     * {@inheritDoc}
     *
     * Do reprezentacji wejścia wykorzystywany jest strumień `std::cin`.
     */
    @Override
    public String generate(InputNode node) {
        StringBuilder code = new StringBuilder();
        code.append(String.format(
                "cout << %s; cin >> %s; ",
                interpolate(escape(node.getMessage())),
                node.getVariable()));
        code.append(emitNext(node.next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     *
     * Do reprezentacji wyjścia wykorzystywany jest strumień `std::cout`.
     */
    @Override
    public String generate(OutputNode node) {
        StringBuilder code = new StringBuilder();
        code.append(String.format(
                "cout << %s << \"\\n\"; ",
                interpolate(escape(node.getMessage()))));
        code.append(emitNext(node.next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(ProcessingNode node) {
        StringBuilder code = new StringBuilder();
        String operations = removeVariableDeclarations(node.getOperations());
        operations = convertArrayLengthToVectorSize(operations);
        code.append(operations.trim().replaceAll(";\n", ";\n" + INDENT + INDENT + INDENT)).append(" ");
        code.append(emitNext(node.next()));
        return code.toString();
    }

    /**
     * Generuje kod dla danego bloku w zależności od typu.
     *
     * @param node Blok diagramu
     * @return Wygenerowany kod
     */
    private String generate(BaseNode node) {
        String comment = emitComment(node);
        if (!"".equals(comment)) comment += INDENT + INDENT + INDENT;

        if (node instanceof ConditionalNode)
            return comment + generate((ConditionalNode) node);
        if (node instanceof EndNode)
            return comment + generate((EndNode) node);
        if (node instanceof InputNode)
            return comment + generate((InputNode) node);
        if (node instanceof OutputNode)
            return comment + generate((OutputNode) node);
        if (node instanceof ProcessingNode)
            return comment + generate((ProcessingNode) node);

        return emitNext(node.next());
    }

    /**
     * {@inheritDoc}
     * 
     * Formatowanie kodu odbywa się na etapie generowania, więc ta metoda nic nie robi.
     */
    @Override
    public String format(String code) {
        return code;
    }

    /**
     * Konwertuje odwołania do atrybutu `length` dla tablic na wywołania metody `vector::size()`.
     * 
     * @param code Fragment kodu
     * @return Przekształcony kod
     */
    private String convertArrayLengthToVectorSize(String code) {
        return code.replaceAll("(.*)\\.length\\b", "$1\\.size()");
    }

    /**
     * Usuwa deklaracje zmiennych ({@code var ...}) z podanego kodu.
     * 
     * @param code Fragment kodu
     * @return Kod niezawierający deklaracji zmiennych
     */
    private String removeVariableDeclarations(String code) {
        return code.replaceAll(Compiler.VARIABLE_DECLARATION_REGEX.toString(), "");
    }

    /**
     * Generuje deklaracje zmiennych używanych w ramach bloku startowego (zmienne lokalne funkcji).
     * Informacje o typie danej zmiennej pobierane są z {@link Compiler#variables}.
     * 
     * @param startNode Blok startowy
     * @return Deklaracje zmiennych z odpowiednimi typami.
     */
    public String emitVariableDeclarations(StartNode startNode) {
        if (!compiler.variables.containsKey(startNode))
            return "";

        StringBuilder decl = new StringBuilder();
        for (Variable var : compiler.variables.get(startNode).values()) {
            decl.append("  ").append(emitVariableDeclaration(var)).append(";\n");
        }
        
        return decl.toString();
    }

    /**
     * Generuje deklarację pojedynczej zmiennej.
     *
     * @param var Zmienna dla której ma być wyemitowana deklaracja
     * @return Deklaracja zmiennej
     */
    private String emitVariableDeclaration(Variable var) {
        return emitVariableOrParamDeclaration(var, false);
    }

    /**
     * Generuje deklarację parametru formalnego funkcji.
     *
     * @param param Parametr
     * @return Deklaracja parametru
     */
    private String emitParamDeclaration(Variable param) {
        return emitVariableOrParamDeclaration(param, true);
    }

    /**
     * Generuje deklarację zmiennej lub param. formalnego funkcji.
     * Deklaracje parametrów formalnych w przypadku tablic (wektorów) nie zawierają rozmiaru.
     *
     * @param var Zmienna lub parametr
     * @param asParam Czy zmienna ma być traktowana jako parametr funkcji?
     * @return Deklaracja zmiennej lub parametru funkcji
     */
    private String emitVariableOrParamDeclaration(Variable var, boolean asParam) {
        StringBuilder decl = new StringBuilder();

        decl.append(emitTypeDeclaration(var.type));
        decl.append(" ").append(var.name);

        if (var.type instanceof Array) {
            Array array = (Array) var.type;
            if (!asParam)
                decl.append("(").append(array.sizes[0]).append(")");
        }

        return decl.toString();
    }

    /**
     * Generuje odpowiednik w C++ dla podanego typu.
     * Typy zmiennych zamieniane są na ich odpowiedniki w C++ następująco:
     *
     *  - int => int
     *  - real => float
     *  - string => std::string
     *  - bool => bool
     *  - T[N] => std::vector<TT>(N), gdzie T jest jednym z typów po lewej, a TT po prawej
     *
     * **Przykłady:**
     *
     *      i:int => int i
     *      s:string` => string s
     *      a:int[10] => vector<int> a(10)
     *      aa:real[2][2] => vector<vector<float> > aa(2)
     *
     * @param type Typ
     * @return Odpowiednik w C++ dla podanego typu.
     */
    private String emitTypeDeclaration(Type type) {
        StringBuilder decl = new StringBuilder();

        if (type instanceof Array) {
            Array array = (Array) type;
            for (int i = 0; i < array.sizes.length; i++)
                decl.append("vector<");
            decl.append(array.elementType);
            for (int i = 0; i < array.sizes.length; i++)
                decl.append("> ");

        } else { // Primitive
            decl.append(type == Primitive.REAL ? "float" : type.toString());
        }

        return decl.toString().trim();
    }

    /**
     * Generuje nagłowek funkcji odpowiadającej danemu blokowi startowemu.
     *
     * @param startNode Blok startowy
     * @return Nagłówek funkcji
     */
    private String emitFunctionHeader(StartNode startNode) {
        StringBuilder code = new StringBuilder(emitComment(startNode));
        code.append(emitFunctionReturnType(startNode)).append(" ").append(startNode.getName());
        code.append("(");
        code.append(emitFunctionParams(startNode));
        code.append(")");
        return code.toString();
    }

    /**
     * Generuje kod dla kolejnych bloków diagramu.
     * Poszczególne bloki odwiedzane są rekurencyjnie - każdy co najwyżej raz.
     *
     * @param node Aktualnie odwiedzany blok
     * @param body Dotychczas wygenerowany kod, kod dl kolejnych bloków jest do niego dopisywany
     * @param visited Zbiór już odwiedzonych bloków
     * @param startNode Aktualny blok startowy w ramach którego odwiedzane są kolejne bloki diagramu
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
        body.append(INDENT+INDENT).append("case ").append(index).append(": ");

        String nodeCode = generate(node);
        nodeCode = convertArrayLevelInitializations(nodeCode, startNode);

        body.append(nodeCode);
        body.append(" break;\n");

        for (DiagramLink link : node.getAllOutgoingLinks()) {
            emitNodeCases((BaseNode) link.getDestination(), body, visited, startNode);
        }
    }

    /**
     * Konwertuje inicjalizacje kolejnych poziomów tablic (typu `a[i] = []`, gdzie `a`
     * jest tablicą wielomymiarową) na odpowiednie inicjalizacje z użyciem `std::vector`.
     * Rozmiary kolejnych poziomów podawane w konstruktorze `vector()` są określane na podstawie deklaracji
     * zmiennej tablicowej.
     *
     * **Przykład**:
     *
     *      var a:int[3][10]; a[i] = [];          // => a[i] = vector<int>(10);
     *
     * @param code Kod, który ma byc poddany konwersji
     * @param startNode Blok startowy, w zasięgu którego zostały zadeklarowane zmienne
     * @return Kod po konwersji
     */
    private String convertArrayLevelInitializations(String code, StartNode startNode) {
        Map<String, Variable> startNodeVars = compiler.variables.get(startNode);
        if (startNodeVars == null)
            return code;

        Matcher matcher = Compiler.ARRAY_LEVEL_INIT_REGEX.matcher(code);
        while (matcher.find()) {
            String name = matcher.group(1);
            Variable var = startNodeVars.get(name);
            if (var == null || !(var.type instanceof Array)) {
                continue;
            } 
            Matcher indexMatcher = Compiler.ARRAY_INDEX_REGEX.matcher(matcher.group(2));
            int level = 0;
            while (indexMatcher.find()) level++;
            Array array = (Array) var.type;
            String vectorInit = emitTypeDeclaration(Array.of(array.elementType, array.sizes[level])) +
                    "(" + array.sizes[level] + ")";
            code = code.replaceFirst(Compiler.ARRAY_EMPTY_INIT_REGEX.toString(), "$1" + vectorInit);

        }
        return code;
    }

    /**
     * Generuje typ zwracany przez funkcję reprezentującą dany blok startowy.
     *
     * @param startNode Blok startowy
     * @return Deklaracja typu zwracanego przez funkcję
     */
    private String emitFunctionReturnType(StartNode startNode) {
        Map<String, Variable> startNodeVars = new HashMap<String, Variable>();
        if (compiler.variables.get(startNode) != null)
            startNodeVars.putAll(compiler.variables.get(startNode));
        if (compiler.params.get(startNode) != null)
            startNodeVars.putAll(compiler.params.get(startNode));

        if (startNodeVars.isEmpty())
            return "void";
        
        EndNode endNode = findEndNode(startNode, new HashMap<DiagramNode, Integer>());
        if (endNode == null || endNode.getReturnValue() == null || endNode.getReturnValue().isEmpty())
            return "void";

        Variable returnVar = startNodeVars.get(endNode.getReturnValue());
        if (returnVar == null)
            return "void";

        return emitTypeDeclaration(returnVar.type);
    }

    /**
     * Szuka bloku końcowego przechodząc rekurencyjnie przez bloki zaczynając od bloku startowego.
     *
     * @param node Aktualnie odwiedzany blok
     * @param visited Mapa zawierająca odwiedzone bloki wraz ilością odwiedzin każdego z nich
     * @return Blok końcowy lub `null` jeśli nie znaleziono
     */
    private EndNode findEndNode(BaseNode node, Map<DiagramNode, Integer> visited) {
        if (node == null)
            return null;
        Integer numVisits = visited.get(node);
        if (numVisits == null) numVisits = 0;

        if (numVisits > 0 && !(node instanceof ConditionalNode))
            // Wierzcholek byl juz odwiedzony i nie jest to rozgalezienie
            return null;

        if (node instanceof ConditionalNode) {
            if (numVisits == 1) {
                // Druga wizyta - galaz Then zostala juz odwiedzona, wiec przejdz do Else
                DiagramLink elseLink = ((ConditionalNode) node).getElseLink();
                visited.put(node, numVisits + 1);
                return findEndNode(elseLink == null ? null : (BaseNode) elseLink.getDestination(), visited);
            } else if (numVisits > 1) {
                // Galaz Else tez tworzy petle - koniec
                return null;
            }
        }

        visited.put(node, numVisits + 1);

        if (node instanceof EndNode)
            return (EndNode) node;

        for (DiagramLink link : node.getAllOutgoingLinks())
            return findEndNode((BaseNode) link.getDestination(), visited);

        return null;
    }

    /**
     * Generuje listę parametrów formalnych funkcji wraz ich typami.
     *
     * @param startNode Blok startowy reprezentowany przez funkcję
     * @return Lista param. rozdzielona przecinkami (`, `)
     */
    private String emitFunctionParams(StartNode startNode) {
        if (!compiler.params.containsKey(startNode))
            return "";

        List<String> decl = new ArrayList<String>();
        for (Variable param : compiler.params.get(startNode).values()) {
            decl.add(emitParamDeclaration(param));
        }

        return Utils.join(decl.toArray(), ", ");
    }

    /**
     * Generuje kod funkcji odpowiadającej danemu blokowi startowemu.
     *
     * @param startNode Blok startowy
     * @return Kod funkcji
     */
    private String emitFunctionBody(StartNode startNode) {
        StringBuilder body = new StringBuilder();

        DiagramLinkList outgoingLinks = startNode.getAllOutgoingLinks();
        if (outgoingLinks.isEmpty()) {
            return body.toString();
        }
        
        body.append(emitVariableDeclarations(startNode));

        body.append(INDENT).append("int ").append(emitNext(startNode)).append("\n");
        body.append(INDENT).append("while (_next > -1) {\n");
        body.append(INDENT+INDENT).append("switch (_next) {\n");
        emitNodeCases(startNode, body, null, startNode);
        body.append(INDENT+INDENT).append("}\n").append(INDENT).append("}\n");

        return body.toString();
    }

    /**
     * Generuje komentarz dla danego bloku.
     *
     * @param node Blok diagramu
     * @return Komentarz
     */
    private String emitComment(BaseNode node) {
        if (node.getComment() == null || node.getComment().isEmpty())
            return "";
        return String.format("/* %s */\n", node.getComment());
    }

    /**
     * Generuje przejście do następnego bloku.
     *
     * @param next Następny blok diagramu
     * @return Kod aktualizujący indeks kolejnego bloku do wykonania
     */
    private String emitNext(BaseNode next) {
        if (next == null) {
            return emitStop();
        }
        int index = next.getParent().getNodes().indexOf(next);
        return "_next = " + index + ";";
    }

    /**
     * Oznacza aktualny blok jako ostatni.
     *
     * @return Kod oznaczający aktualny blok jako ostatni.
     */
    private String emitStop() {
        return "_next = -1;";
    }

    /**
     * Generuje kod zwracający podaną wartość z funkcji odpowiadającej blokowi startowemu.
     *
     * @param returnValue Zwracana wartość
     * @return Kod zwracający daną wartość z funkcji
     */
    private String emitReturn(String returnValue) {
        StringBuilder code = new StringBuilder();
        if (returnValue != null && !returnValue.isEmpty()) {
            code.append("return ").append(returnValue).append("; ");
        }
        return code.toString();
    }
}
