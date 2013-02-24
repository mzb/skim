package pk.dyplom.event;

import com.mindfusion.diagramming.Diagram;

public class DiagramClosedEvent {

    public final Diagram diagram;

    public DiagramClosedEvent(Diagram diagram) {
        this.diagram = diagram;
    }
}
