package pk.dyplom.codegen.action;

import pk.dyplom.I18n;
import pk.dyplom.codegen.CodeGeneration;
import pk.dyplom.codegen.ICodeGenerator;
import pk.dyplom.event.DiagramCodeGenerated;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Wygenerowanie kodu dla diagramu.
 */
public class GenerateCodeAction extends AbstractAction {

    /** Instancja komponentu do generowania kodu */
    public final CodeGeneration codeGeneration;

    /**
     * Inicjalizacja akcji.
     *
     * @param codeGeneration Instancja komponentu do generowania kodu
     */
    public GenerateCodeAction(CodeGeneration codeGeneration) {
        this.codeGeneration = codeGeneration;

        putValue(Action.NAME, I18n.t("action.generateCode"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.generateCodeDesc"));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/magic.png")));
    }

    /**
     * Generuje kod za pomocą generatora wybranego z listy.
     *
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedGeneratorName = (String) codeGeneration.form.codeGeneratorSelect.getSelectedItem();
        ICodeGenerator selectedGenerator = codeGeneration.GENERATORS.get(selectedGeneratorName);
        if (selectedGeneratorName == null || selectedGenerator == null)
            return;

        String code = selectedGenerator.generate(codeGeneration.getCurrentDiagramView().getDiagram());
        code = selectedGenerator.format(code);

        codeGeneration.eventBus.triggerEvent(new DiagramCodeGenerated(code, selectedGenerator));
    }
}
