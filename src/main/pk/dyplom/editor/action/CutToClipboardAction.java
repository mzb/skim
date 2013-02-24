package pk.dyplom.editor.action;

import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Przeniesienie do schowka.
 */
public class CutToClipboardAction extends EditorAction {

    public CutToClipboardAction(DiagramEditor editor) {
        super(editor);
        
        putValue(Action.NAME, I18n.t("action.cutToClipboard"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/scissors.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            editor.getCurrentDiagramView().cutToClipboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
