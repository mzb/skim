package pk.dyplom.eval;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import org.junit.Test;
import pk.dyplom.diagram.*;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CompilerTest {

  private Compiler compiler = new Compiler();
  private Diagram diagram = new Diagram();

  @Test
  public void compilesEmptyProcedure() {
    StartNode start = new StartNode("Proc");
    assertEquals("function Proc() {}", compiler.compile(start));
   
    diagram.add(start);
    EndNode end = new EndNode();
    diagram.add(end);
    diagram.add(new DiagramLink(diagram, start, end));
    assertEquals(
            "function Proc() {" +
               "var $NEXT = 0; " +
               "var $RETURN; " +
               "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                    "switch ($NEXT) {" +
                    "case 0: $NEXT = 1; break; " +
                    "case 1: $NEXT = null; break; " +
                    "}" +
               "} " +
               "return $RETURN; " +
            "}",
            compiler.compile(start));
  }

  @Test
  public void compilesProcedureWithParams() {
    assertEquals("function proc(a, b, c) {}", compiler.compile(new StartNode("proc", "a:int", "b:real", "c:string")));
  }

  @Test
  public void compilesProcedureWithActionNodes() {
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
                    "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                      "switch ($NEXT) {" +
                      "case 0: $NEXT = 1; break; " +
                      "case 1: var X; X = 0; $NEXT = 2; break; " +
                      "case 2: $NEXT = null; break; " +
                      "}" +
                    "} " +
                    "return $RETURN; " +
            "}",
            compiler.compile(start)
    );
  }

  @Test
  public void compilesProcedureWith1WayConditionalNode() {
    StartNode start = new StartNode("Proc");
    ProcessingNode init = new ProcessingNode("var i:int; i = 0;");
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
                    "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                      "switch ($NEXT) {" +
                      "case 0: $NEXT = 1; break; " +
                      "case 1: var i; i = 0; $NEXT = 2; break; " +
                      "case 2: if (i < 3) $NEXT = 3; else $NEXT = null; break; " +
                      "case 3: i++; $NEXT = 2; break; " +
                      "}" +
                    "} " +
                    "return $RETURN; " +
            "}",
            compiler.compile(start)
    );
  }
  
  @Test
  public void compilesProcedureWith2WayConditionalNode() {
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
                    "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                      "switch ($NEXT) {" +
                      "case 0: $NEXT = 1; break; " +
                      "case 1: var i; i = 0; $NEXT = 2; break; " +
                      "case 2: if (i < 3) $NEXT = 3; else $NEXT = 4; break; " +
                      "case 3: i++; $NEXT = 2; break; " +
                      "case 4: i; $NEXT = 5; break; " +
                      "case 5: $NEXT = null; break; " +
                      "}" +
                    "} " +
                    "return $RETURN; " +
            "}",
            compiler.compile(start)
    );
  }

  @Test
  public void compilesProcedureWithNoEndNode() {
    StartNode start = new StartNode("Proc");
    ProcessingNode body = new ProcessingNode("var X:int; X = 0;");
    EndNode end = new EndNode();
    diagram.add(start);
    diagram.add(body);
    diagram.add(new DiagramLink(diagram, start, body));

    assertEquals(
            "function Proc() {" +
                    "var $NEXT = 0; " +
                    "var $RETURN; " +
                    "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                      "switch ($NEXT) {" +
                      "case 0: $NEXT = 1; break; " +
                      "case 1: var X; X = 0; $NEXT = null; break; " +
                      "}" +
                    "} " +
                    "return $RETURN; " +
            "}",
            compiler.compile(start)
    );
  }

  @Test
  public void emitsProcedureReturnValue() {
    StartNode start = new StartNode("Proc", "X:int");
    EndNode end = new EndNode("X");
    diagram.add(start);
    diagram.add(end);
    diagram.add(new DiagramLink(diagram, start, end));

    assertEquals(
            "function Proc(X) {" +
                    "var $NEXT = 0; " +
                    "var $RETURN; " +
                    "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                      "switch ($NEXT) {" +
                      "case 0: $NEXT = 1; break; " +
                      "case 1: $RETURN = X; $NEXT = null; break; " +
                      "}" +
                    "} " +
                    "return $RETURN; " +
            "}",
            compiler.compile(start)
    );
  }

  @Test
  public void emitsTraceCodeWhenTracingIsSet() {

    StartNode start = new StartNode("Proc", "s:string");
    ProcessingNode body = new ProcessingNode("var i:int; i = 0;");
    EndNode end = new EndNode();
    diagram.add(start);
    diagram.add(body);
    diagram.add(end);
    diagram.add(new DiagramLink(diagram, start, body));
    diagram.add(new DiagramLink(diagram, body, end));

    compiler.setTrace(true);

    assertEquals(
            "function Proc(s) {" +
                    "var $NEXT = 0; " +
                    "var $RETURN; " +
                    "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                      "switch ($NEXT) {" +
                      "case 0: $TRACE.before(0); $NEXT = 1; $ENV.set('var.Proc.s', s); $TRACE.after(0); break; " +
                      "case 1: $TRACE.before(1); var i; i = 0; $NEXT = 2; $ENV.set('var.Proc.s', s); $ENV.set('var.Proc.i', i); $TRACE.after(1); break; " +
                      "case 2: $TRACE.before(2); $NEXT = null; $ENV.set('var.Proc.s', s); $ENV.set('var.Proc.i', i); $TRACE.after(2); break; " +
                      "}" +
                    "} " +
                    "return $RETURN; " +
            "}",
            compiler.compile(start)
    );
  }
    
    @Test
    public void interpolatesStrings() {
        assertEquals("\"foo\"", compiler.interpolate("foo"));
        assertEquals("\"\" + foo + \"\"", compiler.interpolate("$foo"));
        assertEquals("\"\" + foo1 + \"\"", compiler.interpolate("$foo1"));
        assertEquals("\"\" + foo_bar + \"\"", compiler.interpolate("$foo_bar"));
        assertEquals("\"foo = \" + foo + \"\"", compiler.interpolate("foo = $foo"));
    }

    @Test
    public void escapesSpecialChars() {
        assertEquals("foo", compiler.escape("foo"));
        assertEquals("'foo", compiler.escape("'foo"));
        assertEquals("\\\"foo\\\"", compiler.escape("\"foo\""));
    }
    
    @Test
    public void detectsPrimitiveTypeDeclarations() {
        assertEquals("int", Arrays.asList(new Variable("i", Primitive.INT)), compiler.detectTypes("var i:int"));
        assertEquals("real", Arrays.asList(new Variable("r", Primitive.REAL)), compiler.detectTypes("var r:real"));
        assertEquals("bool", Arrays.asList(new Variable("b", Primitive.BOOL)), compiler.detectTypes("var b:bool"));
        assertEquals("string", Arrays.asList(new Variable("s", Primitive.STRING)), compiler.detectTypes("var s:string"));

        assertEquals("unknown", Arrays.asList(), compiler.detectTypes("var x:YYY"));
        assertEquals("no type", Arrays.asList(), compiler.detectTypes("var x"));

        assertEquals("func args", Arrays.asList(
                new Variable("i", Primitive.INT),
                new Variable("s", Primitive.STRING)),
                compiler.detectTypes("i:int, s:string"));
    }
    
    @Test
    public void detectsFlatArrayDeclarations() {
       assertEquals(Arrays.asList(new Variable("ai", Array.of(Primitive.INT, 2))),
               compiler.detectTypes("var ai:int[2]"));
       assertEquals(Arrays.asList(new Variable("as", Array.of(Primitive.STRING, 10))),
               compiler.detectTypes("var as:string[10]"));

       assertEquals("func args", Arrays.asList(
               new Variable("ai", Array.of(Primitive.INT, 2)),
               new Variable("as", Array.of(Primitive.STRING, 10))),
               compiler.detectTypes("ai:int[2], as:string[10]"));
    }

    @Test
    public void detectsNestedArrayDeclarations() {
        assertEquals(Arrays.asList(new Variable("ai", Array.of(Primitive.INT, 2, 10))),
                compiler.detectTypes("var ai:int[2][10]"));
        assertEquals(Arrays.asList(new Variable("as", Array.of(Primitive.STRING, 10, 10, 10))),
                compiler.detectTypes("var as:string[10][10][10]"));
    }

    @Test
    public void ignoresTypeDeclarationsInRuntimeCode() {
        StartNode start = new StartNode("Proc", "r:real", "list:int[10]");
        ProcessingNode body = new ProcessingNode(
                "var i:int = 0; " +
                "var s:string; " +
                "var a:bool[2]; "
        );
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(body);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, body));
        diagram.add(new DiagramLink(diagram, body, end));

        assertEquals(
                "function Proc(r, list) {" +
                        "var $NEXT = 0; " +
                        "var $RETURN; " +
                        "while ($NEXT !== null && $ENV.get('$CANCEL') != true) {" +
                        "switch ($NEXT) {" +
                        "case 0: $NEXT = 1; break; " +
                        "case 1: var i = 0; var s; var a = []; $NEXT = 2; break; " +
                        "case 2: $NEXT = null; break; " +
                        "}" +
                        "} " +
                        "return $RETURN; " +
                        "}",
                compiler.compile(start)
        );
    }
}
