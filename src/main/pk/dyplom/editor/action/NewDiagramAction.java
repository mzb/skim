package pk.dyplom.editor.action;

import com.mindfusion.diagramming.Diagram;
import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Dodanie nowego diagramu do edytora.
 */
public class NewDiagramAction extends EditorAction {

    /** Numer kolejnego diagramu - używany w nazwie nowej zakładki */
    private static int nextNewDiagramIndex = 1;

    /**
     * {@inheritDoc}
     */
    public NewDiagramAction(DiagramEditor editor) {
        super(editor);
        putValue(Action.NAME, I18n.t("action.newDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.newDiagramDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/plus.png")));
    }

    /**
     * Dodaj nową zakładkę z diagramem i kolejnym numerem w nazwie.
     *
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Diagram diagram = new Diagram();
        editor.addDiagram(diagram, I18n.t("action.newDiagram.fileName", (nextNewDiagramIndex++)));
    }
}
