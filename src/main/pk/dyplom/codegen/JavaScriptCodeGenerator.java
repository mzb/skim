package pk.dyplom.codegen;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import com.mindfusion.diagramming.DiagramLinkList;
import com.mindfusion.diagramming.DiagramNode;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import pk.dyplom.Utils;
import pk.dyplom.diagram.*;
import pk.dyplom.eval.Compiler;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Generator kodu w języku JavaScript.
 *
 * Ponieważ JavaScript nie posiada statycznego typowania,
 * informacje o typach zmiennych i zwracanych przez funkcje są pomijane.
 */
public class JavaScriptCodeGenerator implements ICodeGenerator {
    
    private static final Logger LOG = Logger.getLogger(JavaScriptCodeGenerator.class.getSimpleName());
    private Formatter formatter;

    /**
     * Formatuje wygenerowany kod.
     * @see JavaScriptCodeGenerator#format(String)
     */
    private static class Formatter {
        private Context context;
        private Scriptable scope;
        private FileReader reader;

        Formatter() throws IOException {
            context = Context.enter();
            scope = context.initStandardObjects();
            reader = new FileReader("lib/beautify.js");
            context.evaluateReader(scope, reader, "beautify.js", 1, null);
            reader.close();
        }

        String format(String code) {
            scope.put("$CODE", scope, code);
            return (String) context.evaluateString(scope, "js_beautify($CODE)", "inline", 1, null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtension() {
        return "js";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String interpolate(String string) {
        return String.format("\"%s\"", string.replaceAll("\\$(\\w+)", "\" + $1 + \""));
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
     */
    @Override
    public String format(String code) {
        if (formatter == null) {
            try {
                formatter = new Formatter();
            } catch (IOException e) {
                LOG.warning(e.toString());
                return code;
            }
        }

        return formatter.format(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(Diagram diagram) {
        StringBuilder code = new StringBuilder();
        for (DiagramNode n : diagram.getNodes()) {
           if (n instanceof StartNode) {
               code.append(generate((StartNode) n));
           }
        }
        
        code.append(generateInputDialog());
        
        return code.toString();
    }

    /**
     * {@inheritDoc}
     *
     * Każdy ciąg bloków zaczynający się od bloku startowego reprezentowany jest jako funkcja
     * o nazwie i parametrach, tego bloku.
     */
    @Override
    public String generate(StartNode start) {
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
     * {@inheritDoc}
     */
    @Override
    public String generate(ConditionalNode node) {
        StringBuilder code = new StringBuilder();
        code.append("if").append(" (").append(node.getCondition()).append(") ");
        DiagramLink thenLink = node.getThenLink();
        code.append(emitNext(thenLink == null ? null : (BaseNode) thenLink.getDestination()));
        DiagramLink elseLink = node.getElseLink();
        code.append("else ");
        code.append(emitNext(elseLink == null ? null : (BaseNode) elseLink.getDestination()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(EndNode node) {
        StringBuilder code = new StringBuilder(emitReturn(node.getReturnValue()));
        code.append(emitStop());
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(InputNode node) {
        StringBuilder code = new StringBuilder();
        code.append(String.format(
                "var %s = $inputDialog(%s);", node.getVariable(), interpolate(escape(node.getMessage()))));
        code.append(emitNext(node.next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(OutputNode node) {
        StringBuilder code = new StringBuilder();
        code.append(String.format("alert(%s);", interpolate(escape(node.getMessage()))));
        code.append(emitNext(node.next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(ProcessingNode node) {
        StringBuilder code = new StringBuilder();
        String operations = Compiler.ignoreTypeDeclarations(node.getOperations());
        code.append(operations.trim()).append(" ");
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
     * Generuje kod wykorzystywany do wprowadzania danych przez użytkownika.
     * @see JavaScriptCodeGenerator#generate(pk.dyplom.diagram.InputNode)
     *
     * @return Kod
     */
    private String generateInputDialog() {
        return
            "function $inputDialog(message) {" +
                "var input = prompt(message);" +
                "if (input === null) throw 'InputCancelled';" +
                "var number = parseFloat(input);" +
                "return isNaN(number) ? String(input) : number" +
            "}";
    }

    /**
     * Generuje kod funkcji odpowiadającej danemu blokowi startowemu.
     *
     * @param startNode Blok startowy
     * @return Kod funkcji
     */
    private StringBuilder emitFunctionBody(StartNode startNode) {
        StringBuilder body = new StringBuilder();

        DiagramLinkList outgoingLinks = startNode.getAllOutgoingLinks();
        if (outgoingLinks.isEmpty()) {
            return body;
        }

        body.append("var ").append(emitNext(startNode));
        body.append("var $RETURN; ");
        body.append("try {");
        body.append("while ($NEXT !== null) {");
        body.append("switch ($NEXT) {");
        emitNodeCases(startNode, body, null);
        body.append("}} ");
        body.append("} catch (e) { if (e != 'InputCancelled') throw e }");
        body.append("return $RETURN; ");

        return body;
    }

    /**
     * Generuje kod dla kolejnych bloków diagramu.
     * Poszczególne bloki odwiedzane są rekurencyjnie - każdy co najwyżej raz.
     *
     * @param node Aktualnie odwiedzany blok
     * @param body Dotychczas wygenerowany kod, kod dl kolejnych bloków jest do niego dopisywany
     * @param visited Zbiór już odwiedzonych bloków
     */
    private void emitNodeCases(BaseNode node, StringBuilder body, Set<DiagramNode> visited) {
        if (visited == null) {
            visited = new HashSet<DiagramNode>();
        }
        if (node == null || visited.contains(node)) {
            return;
        }
        visited.add(node);

        int index = node.getParent().getNodes().indexOf(node);
        body.append("case ").append(index).append(": ");
        body.append(generate(node));
        body.append("break; ");

        for (DiagramLink link : node.getAllOutgoingLinks()) {
            emitNodeCases((BaseNode) link.getDestination(), body, visited);
        }
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
        return String.format("/* %s */", node.getComment());
    }

    /**
     * Generuje nagłowek funkcji odpowiadającej danemu blokowi startowemu.
     *
     * @param startNode Blok startowy
     * @return Nagłówek funkcji
     */
    private StringBuilder emitFunctionHeader(StartNode startNode) {
        StringBuilder comment = new StringBuilder(emitComment(startNode));
        String params = Compiler.ignoreTypeDeclarations(emitFunctionParams(startNode));
        return comment.append(emitFunctionHeader(startNode.getName(), params));
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
        header.append(paramsStr);
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
    private String emitNext(BaseNode next) {
        if (next == null) {
            return emitStop();
        }
        int index = next.getParent().getNodes().indexOf(next);
        return "$NEXT = " + index + "; ";
    }

    /**
     * Oznacza aktualny blok jako ostatni.
     *
     * @return Kod oznaczający aktualny blok jako ostatni.
     */
    private String emitStop() {
        return "$NEXT = null; ";
    }

    /**
     * Przypisuje daną wartość do zmiennej zwracanej z funkcji odpowiadającej blokowi startowemu.
     *
     * @param returnValue Zwracana wartość
     * @return Kod przypisujący daną wartość do zmiennej zwracanej z funkcji
     */
    private String emitReturn(String returnValue) {
        StringBuilder code = new StringBuilder();
        if (returnValue != null && !returnValue.isEmpty()) {
            code.append("$RETURN = ").append(returnValue).append("; ");
        }
        return code.toString();
    }
}
