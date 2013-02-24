package pk.dyplom.editor.action;

import com.mindfusion.diagramming.Diagram;
import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.event.DiagramClosedEvent;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Zamknięcie diagramu.
 */
public class CloseDiagramAction extends EditorAction {

    /**
     * Inicjalizuj akcję.
     * 
     * @param editor Instancja edytora
     */
    public CloseDiagramAction(DiagramEditor editor) {
        super(editor);
        putValue(Action.NAME, I18n.t("action.closeDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.closeDiagramDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/remove.png")));
    }

    /**
     * Zamknij zakładkę zawierającą aktualny diagram.
     * Wywołuje zdarzenie {@link DiagramClosedEvent}.
     * 
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Diagram diagram = editor.getCurrentDiagramView().getDiagram();
        editor.gui.diagramsPanel.remove(editor.gui.diagramsPanel.getSelectedIndex());

        editor.eventBus.triggerEvent(new DiagramClosedEvent(diagram));
    }
}
