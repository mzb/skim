package pk.dyplom.editor.action;

import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Skopiowanie do schowka.
 */
public class CopyToClipboardAction extends EditorAction {

    public CopyToClipboardAction(DiagramEditor editor) {
        super(editor);
        
        putValue(Action.NAME, I18n.t("action.copyToClipboard"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/copy.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            editor.getCurrentDiagramView().copyToClipboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
