package pk.dyplom.editor.action;

import pk.dyplom.I18n;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.editor.export.ExportFileChooser;
import pk.dyplom.editor.export.IDiagramExporter;
import pk.dyplom.editor.export.ImageDiagramExporter;
import pk.dyplom.editor.export.PDFDiagramExporter;
import pk.dyplom.event.DiagramExportedEvent;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Eksport diagramu.
 */
public class ExportDiagramAction extends EditorAction {

    /** Dostepne eksportery */
    private static final Map<Class, IDiagramExporter> EXPORTERS = new HashMap<Class, IDiagramExporter>();
    static {
        EXPORTERS.put(ExportFileChooser.ImageFileFilter.class, new ImageDiagramExporter());
        EXPORTERS.put(ExportFileChooser.PDFFileFilter.class, new PDFDiagramExporter());
    }        
    
    /** Dialog wyboru pliku do zapisu */
    private final ExportFileChooser fileChooser = new ExportFileChooser();

    /**
     * {@inheritDoc}
     */
    public ExportDiagramAction(DiagramEditor editor) {
        super(editor);

        putValue(Action.NAME, I18n.t("action.exportDiagram"));
        putValue(Action.SHORT_DESCRIPTION, I18n.t("action.exportDiagramDesc"));
        putValue(Action.SMALL_ICON, new ImageIcon(GUI.class.getResource("icon/export.png")));
    }

    /**
     * Eksportuj diagram uzywając odpowiedniego eksportera na podstawie wyboru formatu pliku.
     * Wywołuje zdarzenie {@link DiagramExportedEvent}.
     * 
     * @param e Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int option = fileChooser.showSaveDialog(editor.gui);
        if (JFileChooser.APPROVE_OPTION == option) {
            File file = fileChooser.getSelectedFile();

            IDiagramExporter exporter = EXPORTERS.get(fileChooser.getFileFilter().getClass());
            if (exporter == null) {
                editor.logger.warning("No exporter for file filter: " +
                        fileChooser.getFileFilter().getClass().getSimpleName());
                return;
            }

            file = exporter.export(editor.getCurrentDiagramView().getDiagram(), file);

            editor.logger.info("Diagram exported to `" + file.getAbsolutePath() + "'");

            editor.eventBus.triggerEvent(new DiagramExportedEvent(exporter, file));
        }
    }
}
