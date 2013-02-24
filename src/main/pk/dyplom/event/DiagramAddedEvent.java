package pk.dyplom.event;

import pk.dyplom.ui.DiagramPanel;

public class DiagramAddedEvent {

    public final DiagramPanel diagramPanel;

    public DiagramAddedEvent(DiagramPanel diagramPanel) {
        this.diagramPanel = diagramPanel;
    }
}
