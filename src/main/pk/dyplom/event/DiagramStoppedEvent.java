package pk.dyplom.event;

import com.mindfusion.diagramming.Diagram;

public class DiagramStoppedEvent {

    public final Diagram diagram;

    public DiagramStoppedEvent(Diagram diagram) {
        this.diagram = diagram;
    }
}
