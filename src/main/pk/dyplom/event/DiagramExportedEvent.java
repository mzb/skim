package pk.dyplom.event;

import pk.dyplom.editor.export.IDiagramExporter;

import java.io.File;

public class DiagramExportedEvent {
    
    public final IDiagramExporter exporter;
    public final File file;

    public DiagramExportedEvent(IDiagramExporter exporter, File file) {
        this.exporter = exporter;
        this.file = file;
    }
}
