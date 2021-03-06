package pk.dyplom.diagram;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.DiagramLink;
import com.mindfusion.diagramming.DiagramNode;
import com.mindfusion.diagramming.LinkLabel;
import pk.dyplom.I18n;

/**
 * Połącznie wychodzące z bloku warunkowego ({@link ConditionalNode}), wykonywane jeśli
 * wyrażenie warunkowe jest fałszywe.
 */
public class ElseLink extends DiagramLink {

    /**
     * Inicjalizacja.
     *
     * @param diagram Instancja diagramu
     * @param n1 Blok źródłowy (warunkowy)
     * @param n2 Blok docelowy
     */
    public ElseLink(Diagram diagram, DiagramNode n1, DiagramNode n2) {
        super(diagram, n1, n2);
        addLabel(new Label(this));
    }

    public ElseLink(Diagram diagram) {
        super(diagram);
    }

    /**
     * Etykieta identyfikująca połączenie.
     */
    public static class Label extends LinkLabel {
        public static final String TEXT = I18n.t("diagram.link.else");

        public Label(DiagramLink link) {
            super(link, TEXT);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LinkLabel))
                return false;
            if (obj instanceof Label)
                return true;
            LinkLabel other = (LinkLabel) obj;
            return TEXT.equals(other.getText());
        }
    }
}
