package pk.dyplom.editor.action;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.XmlException;
import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.editor.DiagramFileChooser;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Wczytanie diagramu z pliku.
 */
public class OpenDiagramAction extends EditorAction {

    /** Dialog wyboru pliku z diagramem */
    private final JFileChooser fileChooser = new DiagramFileChooser();

    /**
     * {@inheritDoc}
     */
    public OpenDiagramAction(DiagramEditor editor) {
        super(editor);

        putValue(Action.NAME, I18n.t("action.openDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.openDiagramDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/folder_open.png")));
    }

    /**
     * Wczytaj diagram z pliku i dodaj w nowej zakładce do edytora.
     *
     * @param event Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        int option = fileChooser.showOpenDialog(editor.gui);

        if (JFileChooser.APPROVE_OPTION == option) {
            File file = fileChooser.getSelectedFile(); 
            Diagram diagram = new Diagram();
            try {
                diagram.loadFromXml(new FileInputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (XmlException e) {
                throw new RuntimeException(e);
            }

            editor.addDiagram(diagram, file.getName());
        }
    }
}
