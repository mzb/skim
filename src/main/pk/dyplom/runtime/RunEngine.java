package pk.dyplom.runtime;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramNode;
import pk.dyplom.Utils;
import pk.dyplom.diagram.StartNode;
import pk.dyplom.eval.Compiler;
import pk.dyplom.eval.Environment;
import pk.dyplom.eval.EvalEngine;
import pk.dyplom.eval.error.EvalError;
import pk.dyplom.eval.error.ReferenceError;

import java.util.ArrayList;
import java.util.List;

public class RunEngine {

    private EvalEngine evalEngine = new EvalEngine();
    private Compiler compiler = new Compiler();
    private Environment env;
    private ITracer tracer;

    public RunEngine(Environment env) {
        this.setEnv(env);
    }

    public void setTracer(ITracer tracer) {
        if (tracer != null) {
            this.tracer = tracer;
            compiler.setTrace(true);
            env.set("$TRACE", this.tracer);
        }
    }

    public ITracer getTracer() {
        return tracer;
    }

    public void setEnv(Environment env) {
        this.env = env;
        env.set("$ENV", env);
    }

    public void run(Diagram diagram, String target) {
        List<StartNode> startNodes = new ArrayList<StartNode>();
        for (DiagramNode n : diagram.getNodes()) {
            if (n instanceof StartNode) {
                startNodes.add((StartNode) n);
            }
        }
        List<String> startNodeNames = new ArrayList<String>();
        for (StartNode n : startNodes) {
            startNodeNames.add(n.getName());
        }

        String startNodeToRun = target.replaceFirst("\\(.*\\)", "");
        if (!startNodeNames.contains(startNodeToRun)) {
            throw new RuntimeException(new ReferenceError(null, startNodeToRun));
        }

        for (StartNode n : startNodes) {
            eval(compiler.compile(n));
        }
        
        eval(target);
    }

    public Object eval(String code) {
        try {
            return evalEngine.eval(code, env);
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
    }


    @Deprecated
    public void run(StartNode start, String... args) {
        Diagram diagram = start.getParent();
        for (DiagramNode n : diagram.getNodes()) {
            if (n instanceof StartNode) {
                eval(compiler.compile((StartNode) n));
            }
        }
        String target = String.format("%s(%s)", start.getName(), Utils.join(args, ", "));
        eval(target);
    }

    @Deprecated
    public void run(String startNodeName, Diagram diagram, String... args) {
        for (DiagramNode n : diagram.getNodes()) {
            if (n instanceof StartNode) {
                if (((StartNode) n).getName().equals(startNodeName)) {
                    run((StartNode) n, args);
                    return;
                }
            }
        }
    }
}
