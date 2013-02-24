package pk.dyplom.runtime;

import com.mindfusion.diagramming.*;
import pk.dyplom.Config;
import pk.dyplom.diagram.InputNode;
import pk.dyplom.diagram.ProcessingNode;
import pk.dyplom.eval.Environment;
import pk.dyplom.event.*;
import pk.dyplom.runtime.action.StartDiagramExecAction;
import pk.dyplom.runtime.action.StopDiagramExecAction;
import pk.dyplom.ui.DiagramPanel;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class DiagramRunner {

    public final Logger logger = Logger.getLogger("DiagramRunner");
    
    public final GUI gui;
    public final EventManager eventBus;

    public StartDiagramExecAction startDiagramExecAction;
    public StopDiagramExecAction stopDiagramExecAction;

    private Environment environment = new Environment();
    private final RunEngine runEngine;
    private RunnerThread runnerThread;
    private ErrorHandler errorHandler;

    private static final int STEP_DELAY = Config.DEFAULT.getInt("runtime.stepDelay", 1000);
    private static final Brush DEFAULT_NODE_BRUSH = new SolidBrush(Color.WHITE);
    private static final Brush RUNNING_NODE_BRUSH = new SolidBrush(Config.DEFAULT.getColor("runtime.currentNodeColor", Color.GREEN));

    public DiagramRunner(GUI gui, EventManager eventBus) {
        this.gui = gui;
        this.eventBus = eventBus;
        
        runEngine = new RunEngine(environment);
        errorHandler = new ErrorHandler(this);

        registerEventHandlers();
        initActions();
        initComponents();
    }

    private void registerEventHandlers() {
        eventBus.addHandler(DiagramClosedEvent.class, new EventHandler<DiagramClosedEvent>() {
            public void handle(DiagramClosedEvent event) {
                if (!hasOpenedDiagram()) {
                    startDiagramExecAction.setEnabled(false);
                }
            }
        });

        eventBus.addHandler(DiagramAddedEvent.class, new EventHandler<DiagramAddedEvent>() {
            public void handle(DiagramAddedEvent e) {
                startDiagramExecAction.setEnabled(true);
                initVariablesTable(e.diagramPanel.diagramVariables);
            }
        });

        eventBus.addHandler(DiagramRunEvent.class, new EventHandler<DiagramRunEvent>() {
            public void handle(DiagramRunEvent e) {
                startDiagramExecAction.setEnabled(false);
                stopDiagramExecAction.setEnabled(true);
                for (DiagramNode n : e.diagram.getNodes()) {
                    n.setLocked(true);
                    n.setBrush(DEFAULT_NODE_BRUSH);
                }
                for (DiagramLink l : e.diagram.getLinks()) {
                    l.setLocked(true);
                }
            }
        });

        eventBus.addHandler(DiagramStoppedEvent.class, new EventHandler<DiagramStoppedEvent>() {
            public void handle(DiagramStoppedEvent e) {
                startDiagramExecAction.setEnabled(true);
                stopDiagramExecAction.setEnabled(false);
                for (DiagramNode n : e.diagram.getNodes()) {
                    n.setLocked(false);
                    n.setBrush(DEFAULT_NODE_BRUSH);
                }
                for (DiagramLink l : e.diagram.getLinks()) {
                    l.setLocked(false);
                }
            }
        });
    }

    private boolean hasOpenedDiagram() {
        return gui.diagramsPanel.getSelectedIndex() >= 0;
    }

    private void initComponents() {
        gui.runTargetSelect.setEnabled(true);
        gui.runDiagramBtn.setAction(startDiagramExecAction);
        gui.stopDiagramBtn.setAction(stopDiagramExecAction);
        gui.startExecMenuItem.setAction(startDiagramExecAction);
        gui.stopExecMenuItem.setAction(stopDiagramExecAction);
    }

    private void initVariablesTable(JTable table) {
        table.setModel(new VariablesTableModel());
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
    }

    private void initActions() {
        startDiagramExecAction = new StartDiagramExecAction(this);
        startDiagramExecAction.setEnabled(false);
        stopDiagramExecAction = new StopDiagramExecAction(this);
        stopDiagramExecAction.setEnabled(false);
    }

    public DiagramPanel getCurrentDiagramPanel() {
        return (DiagramPanel) gui.diagramsPanel.getSelectedComponent();
    }

    public DiagramNode getCurrentDiagramNode() {
        return ((Tracer) runEngine.getTracer()).currentNode;
    }

    public void run() {
        final Diagram diagram = getCurrentDiagramPanel().diagramView.getDiagram();
        final String runConfig = (String) gui.runTargetSelect.getSelectedItem();
        final JTable variablesTable = getCurrentDiagramPanel().diagramVariables;

        newEnvironment();
        runEngine.setTracer(new Tracer(diagram, variablesTable));

        runnerThread = new RunnerThread(diagram, runConfig);
        if (!runnerThread.isAlive())
            runnerThread.start();
    }

    public void stop() {
        try {
            runnerThread.stop();
        } catch (ThreadDeath e) {}
        eventBus.triggerEvent(new DiagramStoppedEvent(runnerThread.diagram));
    }

    private void newEnvironment() {
        environment = new Environment();
        runEngine.setEnv(environment);
        runEngine.eval(
                "function $outputDialog(message, title) {" +
                    "javax.swing.JOptionPane.showMessageDialog(null, message, title || '', javax.swing.JOptionPane.PLAIN_MESSAGE);" +
                "}"
        );
        runEngine.eval(
                "function $inputDialog(message, title) {" +
                    "var input = javax.swing.JOptionPane.showInputDialog(null, message, title || '', javax.swing.JOptionPane.PLAIN_MESSAGE);" +
                    "if (input === null) {$ENV.set('$CANCEL', true); return;}" +
                    "var number = parseFloat(input);" +
                    "return isNaN(number) ? String(input) : number" +
                "}"
        );
    }


    public class Tracer implements ITracer {
        
        final Diagram diagram;
        final JTable variablesTable;
        DiagramNode currentNode;

        public Tracer(Diagram diagram, JTable variablesTable) {
            this.diagram = diagram;
            this.variablesTable = variablesTable;
        }

        public void before(int index) {
            currentNode = diagram.getNodes().get(index);
            currentNode.setBrush(RUNNING_NODE_BRUSH);
        }

        public void after(int index) {
            currentNode = diagram.getNodes().get(index);
            if (currentNode instanceof ProcessingNode || currentNode instanceof InputNode) {
                VariablesTableModel variables = (VariablesTableModel) variablesTable.getModel();
                variables.update(environment);
            }
            try {
                Thread.sleep(STEP_DELAY);
            } catch (InterruptedException e) {}
            currentNode.setBrush(DEFAULT_NODE_BRUSH);
        }
    }

    
    public class RunnerThread extends Thread {
        
        final Diagram diagram;
        final String runTarget;

        public RunnerThread(Diagram diagram, String runTarget) {
            this.diagram = diagram;
            this.runTarget = runTarget;
            setDaemon(true);
            setName(getClass().getSimpleName());
            setUncaughtExceptionHandler(errorHandler);
        }
        
        @Override
        public void run() {
            eventBus.triggerEvent(new DiagramRunEvent(diagram, runTarget));
            runEngine.run(diagram, runTarget);
            eventBus.triggerEvent(new DiagramStoppedEvent(diagram));
        }
    } 
}
