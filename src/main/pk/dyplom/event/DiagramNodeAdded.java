package pk.dyplom.event;

import pk.dyplom.diagram.BaseNode;

public class DiagramNodeAdded {

    public final BaseNode node;

    public DiagramNodeAdded(BaseNode node) {
        this.node = node;
    }
}
