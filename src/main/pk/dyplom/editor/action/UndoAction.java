package pk.dyplom.editor.action;

import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Cofnięcie ostatnio wykonanej akcji.
 */
public class UndoAction extends EditorAction {

    /**
     * {@inheritDoc}
     */
    public UndoAction(DiagramEditor editor) {
        super(editor);

        setEnabled(false);
        putValue(Action.NAME, I18n.t("action.undo"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/undo.png")));
    }

    /**
     * Cofnij ostatnio wykonaną akcję.
     *
     * @param event Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        editor.getCurrentDiagramView().getDiagram().getUndoManager().undo();
    }
}
