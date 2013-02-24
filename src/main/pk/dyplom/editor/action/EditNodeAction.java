package pk.dyplom.editor.action;

import com.mindfusion.diagramming.DiagramNode;
import pk.dyplom.I18n;
import pk.dyplom.diagram.BaseNode;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.editor.form.NodeForm;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Edycja bloku.
 */
public class EditNodeAction extends NodeAction {

    /**
     * Inicjalizuj zdarzenie.
     *
     * @param node Edytowany blok
     * @param editor Instancja edytora
     */
    public EditNodeAction(DiagramNode node, DiagramEditor editor) {
        super(node, editor);
        putValue(Action.NAME, I18n.t("action.editNode"));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/edit.png")));
    }

    /**
     * Wyświetl formularz edycyjny dla danego bloku.
     *
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (node.getLocked())
            return;

        NodeForm form = NodeForm.formFor((BaseNode) node);

        if (form != null) {
            editor.getCurrentDiagramView().getDiagram().setActiveItem(node);
            form.open(editor.gui);
        }
    }
}
