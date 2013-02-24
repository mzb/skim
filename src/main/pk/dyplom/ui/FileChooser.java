package pk.dyplom.ui;

import pk.dyplom.I18n;

import javax.swing.*;

public class FileChooser extends JFileChooser {

    public FileChooser() {
        super();
        
        setFileHidingEnabled(true);

        // TODO: Dokonczyc lokalizacje
        UIManager.put("FileChooser.openDialogTitleText", I18n.t("fileChooser.title.open"));
        UIManager.put("FileChooser.saveDialogTitleText", I18n.t("fileChooser.title.save"));
    }
}
