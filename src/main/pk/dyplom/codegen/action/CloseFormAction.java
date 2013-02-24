package pk.dyplom.codegen.action;

import pk.dyplom.I18n;
import pk.dyplom.codegen.CodeGeneration;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Zamknięcie formularza dla generowania i zapisu kodu.
 */
public class CloseFormAction extends AbstractAction {

    /** Instancja komponentu do generowania kodu */
    public final CodeGeneration codeGeneration;

    /**
     * Inicjalizacja akcji.
     *
     * @param codeGeneration Instancja komponentu do generowania kodu
     */
    public CloseFormAction(CodeGeneration codeGeneration) {
        this.codeGeneration = codeGeneration;

        putValue(Action.NAME, I18n.t("action.closeCodeGenerationForm"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.closeCodeGenerationFormDesc"));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/remove.png")));
    }

    /**
     * Zamyka formularz.
     *
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        codeGeneration.form.close();
    }
}
