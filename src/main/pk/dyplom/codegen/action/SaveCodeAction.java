package pk.dyplom.codegen.action;

import pk.dyplom.I18n;
import pk.dyplom.Utils;
import pk.dyplom.codegen.CodeGeneration;
import pk.dyplom.ui.FileChooser;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Zapisanie wygenerowanego kodu.
 */
public class SaveCodeAction extends AbstractAction {

    /** Instancja komponentu do generowania kodu */
    public final CodeGeneration codeGeneration;
    /** Dialog wyboru pliku do zapisu */
    private final FileChooser fileChooser = new FileChooser();

    /**
     * Inicjalizacja akcji.
     *
     * @param codeGeneration Instancja komponentu do generowania kodu
     */
    public SaveCodeAction(CodeGeneration codeGeneration) {
        this.codeGeneration = codeGeneration;

        putValue(Action.NAME, I18n.t("action.saveGeneratedCode"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.saveGeneratedCodeDesc"));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/ok.png")));
    }

    /**
     * Zapisuje wygenerowany kod do wybranego pliku.
     *
     * @param event Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        int option = fileChooser.showSaveDialog(codeGeneration.form);
        if (JFileChooser.APPROVE_OPTION == option) {
            File file = fileChooser.getSelectedFile();
            String code = codeGeneration.form.codeEditorPane.getText();
            Utils.writeTextFile(file, code);

            codeGeneration.logger.info("Code saved to `" + file.getAbsolutePath() + "'");
        }
    }


}
