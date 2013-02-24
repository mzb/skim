package pk.dyplom.event;

import com.mindfusion.diagramming.Diagram;

public class DiagramRunEvent {

    public final Diagram diagram;
    public final String runTarget;

    public DiagramRunEvent(Diagram diagram, String runTarget) {
        this.diagram = diagram;
        this.runTarget = runTarget;
    }
}
