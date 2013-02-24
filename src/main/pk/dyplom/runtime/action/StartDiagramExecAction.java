package pk.dyplom.runtime.action;

import pk.dyplom.I18n;
import pk.dyplom.runtime.DiagramRunner;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StartDiagramExecAction extends RuntimeAction {

    public StartDiagramExecAction(DiagramRunner runner) {
        super(runner);

        putValue(Action.NAME, I18n.t("action.runDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.runDiagramDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/play.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedTarget = (String) runner.gui.runTargetSelect.getSelectedItem();
        if (selectedTarget == null || selectedTarget.trim().isEmpty())
            return;

        runner.run();

        boolean shouldAddToList = true;
        for (int i = 0; i < runner.gui.runTargetSelect.getItemCount(); i++) {
            String item = (String) runner.gui.runTargetSelect.getItemAt(i);
            if (item.equals(selectedTarget)) {
                shouldAddToList = false;
                break;
            }
        }

        if (shouldAddToList)
            runner.gui.runTargetSelect.addItem(selectedTarget);
    }
}
