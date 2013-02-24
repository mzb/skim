package pk.dyplom.runtime;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import org.junit.Test;
import pk.dyplom.diagram.*;
import pk.dyplom.eval.Environment;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RunEngineTest {

  private Environment env = new Environment();
  private RunEngine runEngine = new RunEngine(env);
  private Diagram diagram = new Diagram();
  private ITracer tracer = new ITracer() {
      public void before(int index) {
      }

      public void after(int index) {
      }
  };

  @Test
  public void runsActionNodes() {
    StartNode start = new StartNode("main");
    ProcessingNode n1 = new ProcessingNode("X = 0;");
    ProcessingNode n2 = new ProcessingNode("X++;");
    diagram.add(start);
    diagram.add(n1);
    diagram.add(n2);
    diagram.add(new DiagramLink(diagram, start, n1));
    diagram.add(new DiagramLink(diagram, n1, n2));

    runEngine.run(start);
    
    assertEquals(1, env.get("X"));
  }

  @Test
  public void runs1WayConditionalNodes() {
    StartNode start = new StartNode("main");
    ConditionalNode n1 = new ConditionalNode("X > 0");
    ProcessingNode n2 = new ProcessingNode("out = '> 0';");
    diagram.add(start);
    diagram.add(n1);
    diagram.add(n2);
    diagram.add(new DiagramLink(diagram, start, n1));
    diagram.add(new ThenLink(diagram,  n1, n2));

    env.set("X", 2);
    env.set("out", null);
    runEngine.run(start);
    assertEquals("> 0", env.get("out"));

    env.set("X", -2);
    env.set("out", null);
    runEngine.run(start);
    assertEquals(null, env.get("out"));
  }

  @Test
  public void runs2WayConditionalNodes() {
    StartNode start = new StartNode("main");
    ConditionalNode n1 = new ConditionalNode("X > 0");
    ProcessingNode n2 = new ProcessingNode("out = '> 0';");
    ProcessingNode n3 = new ProcessingNode("out = '<= 0';");
    diagram.add(start);
    diagram.add(n1);
    diagram.add(n2);
    diagram.add(n3);
    diagram.add(new DiagramLink(diagram, start, n1));
    diagram.add(new ThenLink(diagram, n1, n2));
    diagram.add(new ElseLink(diagram, n1, n3));

    env.set("X", 2);
    env.set("out", null);
    runEngine.run(start);
    assertEquals("> 0", env.get("out"));

    env.set("X", -2);
    env.set("out", null);
    runEngine.run(start);
    assertEquals("<= 0", env.get("out"));
  }
  
  @Test
  public void runsLoopedNodes() {
    StartNode start = new StartNode("main");
    ProcessingNode init = new ProcessingNode("i = 0;");
    ConditionalNode check = new ConditionalNode("i < 3");
    ProcessingNode incr = new ProcessingNode("i++;");
    diagram.add(start);
    diagram.add(init);
    diagram.add(check);
    diagram.add(incr);
    diagram.add(new DiagramLink(diagram, start, init));
    diagram.add(new DiagramLink(diagram, init, check));
    diagram.add(new ThenLink(diagram, check, incr));
    diagram.add(new DiagramLink(diagram, incr, check));
    
    runEngine.run(start);
    
    assertEquals(3, env.get("i"));
  }

  @Test
  public void runsProcedureOnCall() {
    StartNode mainStart = new StartNode("main");
    ProcessingNode call = new ProcessingNode("proc(true);");
    ProcessingNode afterCall = new ProcessingNode("after = true;");
    diagram.add(mainStart);
    diagram.add(call);
    diagram.add(afterCall);
    diagram.add(new DiagramLink(diagram, mainStart, call));
    diagram.add(new DiagramLink(diagram, call, afterCall));
    
    StartNode procStart = new StartNode("proc", "a");
    ProcessingNode procBody = new ProcessingNode("called = a;");
    diagram.add(procStart);
    diagram.add(procBody);
    diagram.add(new DiagramLink(diagram, procStart, procBody));
    
    runEngine.run(mainStart);
    
    assertEquals("Procedure not called", true, env.get("called"));
    assertEquals("After call not reached", true, env.get("after"));
  }

  @Test
  public void runsProcedureAndReturnsValueToCaller() {
    StartNode procStart = new StartNode("Max", "a", "b");
    ConditionalNode procBody = new ConditionalNode("a > b");
    EndNode procReturnA = new EndNode("a");
    EndNode procReturnB = new EndNode("b");
    diagram.add(procStart);
    diagram.add(procBody);
    diagram.add(procReturnA);
    diagram.add(procReturnB);
    diagram.add(new DiagramLink(diagram, procStart, procBody));
    diagram.add(new ThenLink(diagram, procBody, procReturnA));
    diagram.add(new ElseLink(diagram, procBody, procReturnB));

    StartNode mainStart = new StartNode("Main");
    ProcessingNode call = new ProcessingNode("max = Max(1, 20);");
    diagram.add(mainStart);
    diagram.add(call);
    diagram.add(new DiagramLink(diagram, mainStart, call));

    runEngine.run(mainStart);

    assertEquals(20, env.get("max"));
  }

  @Test
  public void canTraceProcedureVars() {
    StartNode start = new StartNode("main");
    ProcessingNode n1 = new ProcessingNode("var X:int; X = 1; var Y:int; Y = X;");
    ProcessingNode n2 = new ProcessingNode("X++;");
    diagram.add(start);
    diagram.add(n1);
    diagram.add(n2);
    diagram.add(new DiagramLink(diagram, start, n1));
    diagram.add(new DiagramLink(diagram, n1, n2));
 
    runEngine.setTracer(tracer);
    
    runEngine.run(start);

    assertEquals(2, env.get("var.main.X"));
    assertEquals(1, env.get("var.main.Y"));
  }

  @Test
  public void canTraceProcedureArgs() {
    StartNode start = new StartNode("Proc", "a:int");
    ProcessingNode n1 = new ProcessingNode("var X:int; X = 1;");
    ProcessingNode n2 = new ProcessingNode("X++; a = X;");
    diagram.add(start);
    diagram.add(n1);
    diagram.add(n2);
    diagram.add(new DiagramLink(diagram, start, n1));
    diagram.add(new DiagramLink(diagram, n1, n2));

    runEngine.setTracer(tracer);

    runEngine.run(start);

    assertEquals(2, env.get("var.Proc.a"));
  }

  @Test
  public void canTraceProcedureWithoutLocalVars() {
    StartNode start = new StartNode("Proc");
    ProcessingNode n1 = new ProcessingNode("X = 1;");
    ProcessingNode n2 = new ProcessingNode("X++;");
    diagram.add(start);
    diagram.add(n1);
    diagram.add(n2);
    diagram.add(new DiagramLink(diagram, start, n1));
    diagram.add(new DiagramLink(diagram, n1, n2));

    runEngine.setTracer(tracer);

    runEngine.run(start);

    assertEquals(2, env.get("X"));
  }

    @Test
    public void canTraceFloatingPointNumbers() {
        StartNode start = new StartNode("main");
        ProcessingNode n1 = new ProcessingNode("var f:real; f = 1.2; var fi:real; fi = 1.0;");
        diagram.add(start);
        diagram.add(n1);
        diagram.add(new DiagramLink(diagram, start, n1));

        runEngine.setTracer(tracer);

        runEngine.run(start);

        assertEquals(1.2, env.get("var.main.f"));
        assertTrue(env.get("var.main.f") instanceof Double);
        assertEquals(1, env.get("var.main.fi"));
        assertTrue(env.get("var.main.fi") instanceof Integer);
    }

    @Test
    public void canTraceArrays() {
        StartNode start = new StartNode("main");
        ProcessingNode n1 = new ProcessingNode("var list:int[10];");
        ProcessingNode n2 = new ProcessingNode("list[0] = 1; list[1] = 2.2;");
        diagram.add(start);
        diagram.add(n1);
        diagram.add(n2);
        diagram.add(new DiagramLink(diagram, start, n1));
        diagram.add(new DiagramLink(diagram, n1, n2));

        runEngine.setTracer(tracer);

        runEngine.run(start);

        assertEquals(Arrays.asList(1, 2.2), env.get("var.main.list"));
        assertEquals("[1, 2.2]", env.get("var.main.list").toString());
    }

    @Test
    public void runsEachNodeOnce() {
        StartNode start = new StartNode("Proc");
        ProcessingNode n1 = new ProcessingNode("var X:int; X = X || 1;");
        ProcessingNode n2 = new ProcessingNode("X++;");
        diagram.add(start);
        diagram.add(n1);
        diagram.add(n2);
        diagram.add(new DiagramLink(diagram, start, n1));
        diagram.add(new DiagramLink(diagram, n1, n2));

        runEngine.setTracer(tracer);

        runEngine.run(start);

        assertEquals(2, env.get("var.Proc.X"));
    }
}
