package pk.dyplom.runtime.action;

import pk.dyplom.I18n;
import pk.dyplom.runtime.DiagramRunner;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StopDiagramExecAction extends RuntimeAction {

    public StopDiagramExecAction(DiagramRunner runner) {
        super(runner);

        putValue(Action.NAME, I18n.t("action.stopDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.stopDiagramDesc"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/stop.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        runner.stop();
    }
}
