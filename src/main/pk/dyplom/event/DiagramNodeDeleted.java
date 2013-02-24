package pk.dyplom.event;

import pk.dyplom.diagram.BaseNode;

public class DiagramNodeDeleted {

    public final BaseNode node;

    public DiagramNodeDeleted(BaseNode node) {
        this.node = node;
    }
}
