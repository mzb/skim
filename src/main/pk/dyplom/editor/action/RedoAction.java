package pk.dyplom.editor.action;

import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Powtórzenie ostatnio wykonanej akcji.
 */
public class RedoAction extends EditorAction {

    /**
     * {@inheritDoc}
     */
    public RedoAction(DiagramEditor editor) {
        super(editor);

        setEnabled(false);
        putValue(Action.NAME, I18n.t("action.redo"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/redo.png")));
    }

    /**
     * Powtórz ostatnio wykonaną akcję.
     *
     * @param event Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        editor.getCurrentDiagramView().getDiagram().getUndoManager().redo();
    }
}
