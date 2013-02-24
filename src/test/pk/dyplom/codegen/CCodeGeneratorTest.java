package pk.dyplom.codegen;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import org.junit.Test;
import pk.dyplom.diagram.*;
import pk.dyplom.eval.Array;
import pk.dyplom.eval.Primitive;
import pk.dyplom.eval.Variable;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class CCodeGeneratorTest {

    private CCodeGenerator cg = new CCodeGenerator();
    private Diagram diagram = new Diagram();

    @Test
    public void interpolatesStrings() {
        assertEquals("\"foo\"", cg.interpolate("foo"));
        assertEquals("\"\" << foo << \"\"", cg.interpolate("$foo"));
        assertEquals("\"\" << foo1 << \"\"", cg.interpolate("$foo1"));
        assertEquals("\"\" << foo_bar << \"\"", cg.interpolate("$foo_bar"));
        assertEquals("\"foo = \" << foo << \"\"", cg.interpolate("foo = $foo"));
    }

    @Test
    public void escapesSpecialChars() {
        assertEquals("foo", cg.escape("foo"));
        assertEquals("'foo", cg.escape("'foo"));
        assertEquals("\\\"foo\\\"", cg.escape("\"foo\""));
    }

    @Test
    public void generatesFunctionForEmptyStartNode() {
        StartNode start = new StartNode("F");
        assertEquals("void F() {\n}\n\n", cg.generate(start));
    }

    @Test
    public void generatesCodeForMinimalDiagram() {
        StartNode start = new StartNode("F");
        diagram.add(start);
        EndNode end = new EndNode();
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, end));

        assertEquals(
                "void F() {\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1: _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n",
                cg.generate(start));
    }

    @Test
    public void generatesCodeForProcessingNodes() {
        ProcessingNode p = new ProcessingNode(
                "var i:int;" +
                "s = \"test\";" +
                "i = 2 + 2; "
        );

        assertEquals(
                "s = \"test\";" +
                "i = 2 + 2;" +
                " _next = -1;",
                cg.generate(p));
    }

    @Test
    public void generatesCodeForCondtionalNodes() {
        ConditionalNode cond = new ConditionalNode("i < 3");
        ProcessingNode incr = new ProcessingNode();
        ProcessingNode els = new ProcessingNode();
        diagram.add(cond);
        diagram.add(incr);
        diagram.add(els);
        diagram.add(new ThenLink(diagram, cond, incr));
        diagram.add(new DiagramLink(diagram, incr, cond));
        diagram.add(new ElseLink(diagram, cond, els));
        
        assertEquals(
                "if (i < 3) _next = 1; else _next = 2;",
                cg.generate(cond)
        );
    }
    
    @Test
    public void generatesCodeForOutputNodes() {
        OutputNode out = new OutputNode("Wynik: $a + $b = $c");
        assertEquals(
                "cout << \"Wynik: \" << a << \" + \" << b << \" = \" << c << \"\" << \"\\n\"; _next = -1;",
                cg.generate(out)
        );
    }
    
    @Test
    public void generatesCodeForInputNodes() {
        InputNode in = new InputNode("Podaj a: ", "a");
        assertEquals(
                "cout << \"Podaj a: \"; cin >> a; _next = -1;",
                cg.generate(in)
        );
    }

    @Test
    public void generatesVariableDeclarationsBasedOnTypeInfo() {
        StartNode start = new StartNode("F");
        
        assertEquals("", cg.emitVariableDeclarations(start));

        cg.compiler.variables.put(start, new LinkedHashMap<String, Variable>());
        cg.compiler.variables.get(start).put("i", new Variable("i", Primitive.INT));
        cg.compiler.variables.get(start).put("r", new Variable("r", Primitive.REAL));
        cg.compiler.variables.get(start).put("b", new Variable("b", Primitive.BOOL));
        cg.compiler.variables.get(start).put("s", new Variable("s", Primitive.STRING));
        cg.compiler.variables.get(start).put("a", new Variable("a", Array.of(Primitive.INT, 10)));
        cg.compiler.variables.get(start).put("aa", new Variable("aa", Array.of(Primitive.STRING, 2, 5)));
        
        assertEquals(
                "  int i;\n" +
                "  float r;\n" +
                "  bool b;\n" +
                "  string s;\n" +
                "  vector<int> a(10);\n" +
                "  vector<vector<string> > aa(2);\n",
                cg.emitVariableDeclarations(start)
        );
    }
    
    @Test
    public void generatesVariableDeclarationsAtTheBeginningOfStartNode() {
        StartNode start = new StartNode("F");
        ProcessingNode p1 = new ProcessingNode(
                "var i:int;" +
                "i = 0;"
        );
        ProcessingNode p2 = new ProcessingNode(
                "var a:string[10];" +
                "a[0] = \"test\";"
        );
        EndNode end = new EndNode();
        diagram.add(start);
        diagram.add(p1);
        diagram.add(p2);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, p1));
        diagram.add(new DiagramLink(diagram, p1, p2));
        diagram.add(new DiagramLink(diagram, p2, end));

        assertEquals(
                "void F() {\n" +
                "  int i;\n" +
                "  vector<string> a(10);\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1: i = 0; _next = 2; break;\n" +
                "    case 2: a[0] = \"test\"; _next = 3; break;\n" +
                "    case 3: _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n",
                cg.generate(start)
        );
    }

    @Test
    public void generatesParamDeclarations() {
        StartNode start = new StartNode("F", "i:int", "a:string[2]");

        assertEquals(
                "void F(int i, vector<string> a) {\n}\n\n",
                cg.generate(start));
    }

    @Test
    public void generatesPrimitiveReturnTypeDeclarationForStartBlock() {
        StartNode start = new StartNode("Sum", "range:int");
        ProcessingNode var = new ProcessingNode("var i:int; var s:int;");
        ProcessingNode init = new ProcessingNode("i = s = 0;");
        ConditionalNode check = new ConditionalNode("i < range");
        ProcessingNode add = new ProcessingNode("s = s + i; i = i + 1;");
        EndNode end = new EndNode("s");
        diagram.add(start);
        diagram.add(var);
        diagram.add(init);
        diagram.add(check);
        diagram.add(add);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, var));
        diagram.add(new DiagramLink(diagram, var, init));
        diagram.add(new DiagramLink(diagram, init, check));
        diagram.add(new ThenLink(diagram, check, add));
        diagram.add(new DiagramLink(diagram, add, check));
        diagram.add(new ElseLink(diagram, check, end));

        assertEquals(
                "int Sum(int range) {\n" +
                "  int i;\n" +
                "  int s;\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1:  _next = 2; break;\n" +
                "    case 2: i = s = 0; _next = 3; break;\n" +
                "    case 3: if (i < range) _next = 4; else _next = 5; break;\n" +
                "    case 4: s = s + i; i = i + 1; _next = 3; break;\n" +
                "    case 5: return s; _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n",
                cg.generate(start));
    }

    @Test
    public void generatesArrayReturnTypeForStartBlockFunctionHeader() {
        StartNode start = new StartNode("F");
        EndNode end = new EndNode("a");
        ProcessingNode var = new ProcessingNode("var a:int[10];");
        diagram.add(start);
        diagram.add(var);
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, start, var));
        diagram.add(new DiagramLink(diagram, var, end));

        assertEquals(
                "vector<int> F() {\n" +
                "  vector<int> a(10);\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1:  _next = 2; break;\n" +
                "    case 2: return a; _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n",
                cg.generate(start));
    }

    @Test
    public void convertsArrayLengthToSizeCall() {
        ProcessingNode p = new ProcessingNode(
                "i = a.length;"
        );
        assertEquals("ProcessingNode",
                "i = a.size();" +
                " _next = -1;",
                cg.generate(p)
        );

        ConditionalNode c = new ConditionalNode(
                "i < a.length"
        );
        assertEquals("ConditionalNode",
                "if (i < a.size()) _next = -1; else _next = -1;",
                cg.generate(c)
        );

        p = new ProcessingNode(
                "a.lengths;"
        );
        assertEquals("No conversion",
                "a.lengths;" +
                " _next = -1;",
                cg.generate(p)
        );
    }

    @Test
    public void convertsArrayLevelInitializationsWithVectorConstructors() {
        StartNode start = new StartNode("F");
        ProcessingNode p = new ProcessingNode(
                "var b:string[1][2][3];" +
                "b[i] = []; " +
                "b[i][0] = []; "
        );
        diagram.add(start);
        diagram.add(p);
        diagram.add(new DiagramLink(diagram, start, p));
        
        assertEquals(
                "void F() {\n" +
                "  vector<vector<vector<string> > > b(1);\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1: " +
                            "b[i] = vector<string>(2); " +
                            "b[i][0] = vector<string>(3); " +
                            "_next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n",
                cg.generate(start)
        );
    }

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
                "#include <iostream>\n" +
                "#include <string>\n" +
                "#include <vector>\n" +
                "using namespace std;\n" +
                "\n" +
                "void Proc1();\n\n" +
                "void Proc2();\n\n" +
                "void Proc1() {\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1: _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "void Proc2() {\n" +
                "  int _next = 2;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 2: _next = 3; break;\n" +
                "    case 3: _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "\n" +
                "int main() {\n" +
                "  \n" +
                "  return 0;\n" +
                "}\n",
                cg.generate(diagram));
    }

    @Test
    public void generatesDiagramWithMainAutoCall() {
        StartNode main = new StartNode("Main");
        diagram.add(main);
        EndNode end = new EndNode();
        diagram.add(end);
        diagram.add(new DiagramLink(diagram, main, end));

        assertEquals(
                "#include <iostream>\n" +
                "#include <string>\n" +
                "#include <vector>\n" +
                "using namespace std;\n" +
                "\n" +
                "void Main();\n\n" +
                "void Main() {\n" +
                "  int _next = 0;\n" +
                "  while (_next > -1) {\n" +
                "    switch (_next) {\n" +
                "    case 0: _next = 1; break;\n" +
                "    case 1: _next = -1; break;\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "\n" +
                "int main() {\n" +
                "  Main();\n" +
                "  return 0;\n" +
                "}\n",
                cg.generate(diagram));
    }
}
