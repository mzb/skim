package pk.dyplom.editor.action;

import com.mindfusion.diagramming.XmlException;
import pk.dyplom.I18n;
import pk.dyplom.Utils;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.editor.DiagramFileChooser;
import pk.dyplom.event.DiagramSavedEvent;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Zapisanie diagramu do pliku.
 */
public class SaveDiagramAction extends EditorAction {

    /** Dialog wyboru pliku do zapisu */
    private final JFileChooser fileChooser = new DiagramFileChooser();

    /**
     * {@inheritDoc} 
     */
    public SaveDiagramAction(DiagramEditor editor) {
        super(editor);

        putValue(Action.NAME, I18n.t("action.saveDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.saveDiagramDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/ok.png")));
    }

    /**
     * Zapisz diagram do wybranego pliku.
     * Wywołuje zdarzenie {@link DiagramSavedEvent} jeśli zapis się powiódł.
     * 
     * @param event Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        int option = fileChooser.showSaveDialog(editor.gui);
        if (JFileChooser.APPROVE_OPTION == option) {
            File file = fileChooser.getSelectedFile();
            try {
                file = Utils.forceFileExtension(file, DiagramFileChooser.DIAGRAM_FILE_EXTENSION);
                editor.getCurrentDiagramView().getDiagram().saveToXml(new FileOutputStream(file), false);
                editor.eventBus.triggerEvent(new DiagramSavedEvent(file.getName()));

                editor.logger.info("Diagram saved to `" + file.getAbsolutePath() + "'");

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (XmlException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
