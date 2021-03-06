package pk.dyplom.editor.action;

import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Wklej ze schowka.
 */
public class PasteFromClipboardAction extends EditorAction {

    public PasteFromClipboardAction(DiagramEditor editor) {
        super(editor);
        
        putValue(Action.NAME, I18n.t("action.pasteFromClipboard"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/notes.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            editor.getCurrentDiagramView().pasteFromClipboard(0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
