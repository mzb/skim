package pk.dyplom.editor.action;

import com.mindfusion.diagramming.DiagramNode;
import pk.dyplom.editor.DiagramEditor;

/**
 * Bazowa klasa reprezentująca akcję na pojedynczym bloku.
 */
public abstract class NodeAction extends EditorAction {

    /** Blok, którego dotyczy akcja */
    protected DiagramNode node;

    /**
     * Inicjuj zdarzenie.
     *
     * @param node Blok, na którym wykonywana jest akcja
     * @param editor Instancja edytora
     */
    public NodeAction(DiagramNode node, DiagramEditor editor) {
        super(editor);
        setNode(node);
    }

    /**
     * Ustaw blok, na którym ma być wykonana akcja.
     * Aktywuje akcję (i wszystkie elementy, które są do niej podpięte).
     *
     * @param node Blok
     */
    public void setNode(DiagramNode node) {
        this.node = node;
        setEnabled(node != null);
    }
}
