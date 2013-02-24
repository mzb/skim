package pk.dyplom.editor.action;

import com.mindfusion.diagramming.DiagramNode;
import pk.dyplom.I18n;
import pk.dyplom.diagram.BaseNode;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.event.DiagramNodeDeleted;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Usunięcie bloku.
 */
public class DeleteNodeAction extends NodeAction {

    /**
     * Inicjalizuj akcję.
     *
     * @param node Usuwany blok
     * @param editor Instancja edytora
     */
    public DeleteNodeAction(DiagramNode node, DiagramEditor editor) {
        super(node, editor);
        putValue(Action.NAME, I18n.t("action.deleteNode"));

        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/bin.png")));
    }

    /**
     * Usuń dany blok z aktywnego diagramu.
     * Wywołuje zdarzenie {@link DiagramNodeDeleted}.
     * 
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        DiagramNode nodeToDelete = node;
        editor.getCurrentDiagramView().getDiagram().getNodes().remove(node);

        editor.eventBus.triggerEvent(new DiagramNodeDeleted((BaseNode) nodeToDelete));
    }
}
