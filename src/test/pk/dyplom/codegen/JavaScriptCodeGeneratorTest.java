package pk.dyplom.codegen;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import org.junit.Test;
import pk.dyplom.diagram.*;

import static org.junit.Assert.assertEquals;

public class JavaScriptCodeGeneratorTest {

    private ICodeGenerator cg = new JavaScriptCodeGenerator();
    private Diagram diagram = new Diagram();

    @Test
    public void generatesDiagram() {
        StartNode start1 = new StartNode("Proc1");
        diagram.add(start1);
        EndNode end1 = new EndNode();
        diagram.add(end1);
        diagram.add(new DiagramLink(diagram, start1, end1));
        StartNode start2 = new StartNode("Proc2");
        diagram.add(start2);
        EndNode end2 = new EndNode();
        diagram.add(end2);
        diagram.add(new DiagramLink(diagram, start2, end2));

        assertEquals(
                "function Proc1() {" +
                    "var $NEXT = 0; " +
                    "var $RETURN; " +
                    "try {" +
                    "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: $NEXT = null; break; " +
                        "}" +
                    "} " +
                    "} catch (e) { if (e != 'InputCancelled') throw e }" +
                    "return $RETURN; " +
                "}" +
                "function Proc2() {" +
                    "var $NEXT = 2; " +
                    "var $RETURN; " +
                    "try {" +
                    "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 2: $NEXT = 3; break; " +
                        "case 3: $NEXT = null; break; " +
                        "}" +
                    "} " +
                    "} catch (e) { if (e != 'InputCancelled') throw e }" +
                    "return $RETURN; " +
                "}" +
                "function $inputDialog(message) {" +
                    "var input = prompt(message);" +
                    "if (input === null) throw 'InputCancelled';" +
                    "var number = parseFloat(input);" +
                    "return isNaN(number) ? String(input) : number" +
                "}",
                cg.generate(diagram));
    }

    @Test
    public void generatesEmptyProcedure() {
        StartNode start = new StartNode("Proc");
        assertEquals("function Proc() {}", cg.generate(start));

        diagram.add(start);
        EndNode end = new EndNode();
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, end));
        assertEquals(
                "function Proc() {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start));
    }

    @Test
    public void generatesProcedureWithParams() {
        assertEquals("function proc(a, b, c) {}", cg.generate(new StartNode("proc", "a:int", "b:string", "c:bool")));
    }

    @Test
    public void generatesProcedureWithProcessingNodes() {
        StartNode start = new StartNode("Proc");
        ProcessingNode body = new ProcessingNode("var X:int; X = 0;");
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(body);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, body));
        diagram.add(new DiagramLink(diagram, body, end));

        assertEquals(
                "function Proc() {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: var X; X = 0; $NEXT = 2; break; " +
                        "case 2: $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start)
        );
    }

    @Test
    public void generatesProcedureWith1WayConditionalNode() {
        StartNode start = new StartNode("Proc");
        ProcessingNode init = new ProcessingNode("var i:int; i = 0; ");
        ConditionalNode cond = new ConditionalNode("i < 3");
        ProcessingNode incr = new ProcessingNode("i++;");
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(init);
        diagram.add(cond);
        diagram.add(incr);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, init));
        diagram.add(new DiagramLink(diagram, init, cond));
        diagram.add(new ThenLink(diagram, cond, incr));
        diagram.add(new DiagramLink(diagram, incr, cond));

        assertEquals(
                "function Proc() {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: var i; i = 0; $NEXT = 2; break; " +
                        "case 2: if (i < 3) $NEXT = 3; else $NEXT = null; break; " +
                        "case 3: i++; $NEXT = 2; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start)
        );
    }

    @Test
    public void generatesProcedureWith2WayConditionalNode() {
        StartNode start = new StartNode("Proc");
        ProcessingNode init = new ProcessingNode("var i:int; i = 0;");
        ConditionalNode cond = new ConditionalNode("i < 3");
        ProcessingNode incr = new ProcessingNode("i++;");
        ProcessingNode els = new ProcessingNode("i;");
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(init);
        diagram.add(cond);
        diagram.add(incr);
        diagram.add(els);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, init));
        diagram.add(new DiagramLink(diagram, init, cond));
        diagram.add(new ThenLink(diagram, cond, incr));
        diagram.add(new DiagramLink(diagram, incr, cond));
        diagram.add(new ElseLink(diagram, cond, els));
        diagram.add(new DiagramLink(diagram, els, end));

        assertEquals(
                "function Proc() {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: var i; i = 0; $NEXT = 2; break; " +
                        "case 2: if (i < 3) $NEXT = 3; else $NEXT = 4; break; " +
                        "case 3: i++; $NEXT = 2; break; " +
                        "case 4: i; $NEXT = 5; break; " +
                        "case 5: $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start)
        );
    }

    @Test
    public void generatesProcedureWithNoEndNode() {
        StartNode start = new StartNode("Proc");
        ProcessingNode body = new ProcessingNode("var X:int; X = 0; ");
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(body);
        diagram.add(new DiagramLink(diagram, start, body));

        assertEquals(
                "function Proc() {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: var X; X = 0; $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start)
        );
    }

    @Test
    public void generatesProcedureReturnValue() {
        StartNode start = new StartNode("Proc", "X:int");
        EndNode end = new EndNode("X");
        diagram.add(start);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, end));

        assertEquals(
                "function Proc(X) {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: $RETURN = X; $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start)
        );
    }

    @Test
    public void interpolatesStrings() {
        assertEquals("\"foo\"", cg.interpolate("foo"));
        assertEquals("\"\" + foo + \"\"", cg.interpolate("$foo"));
        assertEquals("\"\" + foo1 + \"\"", cg.interpolate("$foo1"));
        assertEquals("\"\" + foo_bar + \"\"", cg.interpolate("$foo_bar"));
        assertEquals("\"foo = \" + foo + \"\"", cg.interpolate("foo = $foo"));
    }

    @Test
    public void escapesSpecialChars() {
        assertEquals("foo", cg.escape("foo"));
        assertEquals("'foo", cg.escape("'foo"));
        assertEquals("\\\"foo\\\"", cg.escape("\"foo\""));
    }

    @Test
    public void generatesNodeComments() {
        StartNode start = new StartNode("Proc");
        ProcessingNode body = new ProcessingNode("X = 0;");
        body.setComment("Init\nX");
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(body);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, body));
        diagram.add(new DiagramLink(diagram, body, end));

        assertEquals(
                "function Proc() {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "try {" +
                        "while ($NEXT !== null) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: /* Init\nX */X = 0; $NEXT = 2; break; " +
                        "case 2: $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "} catch (e) { if (e != 'InputCancelled') throw e }" +
                        "return $RETURN; " +
                        "}",
                cg.generate(start)
        );
    }

    @Test
    public void generatesStartNodeCommentBeforeFunction() {
        StartNode start = new StartNode("Proc");
        start.setComment("Komentarz");

        assertEquals("/* Komentarz */" +
                "function Proc() {}", cg.generate(start));
    }
}
