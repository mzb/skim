package pk.dyplom.codegen.action;

import pk.dyplom.I18n;
import pk.dyplom.codegen.CodeGeneration;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Otwarcie formularza do generowania kodu.
 */
public class OpenFormAction extends AbstractAction {

    /** Instancja komponentu do generowania kodu */
    public final CodeGeneration codeGeneration;

    /**
     * Inicjalizacja akcji.
     *
     * @param codeGeneration Instancja komponentu do generowania kodu
     */
    public OpenFormAction(CodeGeneration codeGeneration) {
        this.codeGeneration = codeGeneration;

        putValue(Action.NAME, I18n.t("action.openCodeGenerationForm"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.openCodeGenerationFormDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/magic.png")));
    }

    /**
     * Wyczyść i wyświetl formularz.
     *
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        codeGeneration.form.codeEditorPane.setText("");
        codeGeneration.saveCodeAction.setEnabled(false);
        codeGeneration.form.open(codeGeneration.gui);
    }
}
