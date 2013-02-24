package pk.dyplom.editor.export;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.export.PdfExporter;
import com.mindfusion.pdf.AutoScale;
import pk.dyplom.Utils;

import java.io.File;

/**
 * Eksport diagramu do pliku PDF.
 */
public class PDFDiagramExporter implements IDiagramExporter {

    PdfExporter exporter = new PdfExporter();

    /**
     * {@inheritDoc}
     *
     * Diagram jest automatycznie dopasowywany do strony (z obcinaniem zbędnych marginesów).
     * Rozszerzenie PDF jest automatycznie dodawane w razie potrzeby.
     */
    @Override
    public File export(Diagram diagram, File file) {
        diagram.resizeToFitItems(8, true);
        exporter.setAutoScale(AutoScale.FitToPage);
        exporter.setAutoOrientation(true);
        exporter.setDefaultCharset("UTF8");

        file = Utils.forceFileExtension(file, "pdf");
        exporter.export(diagram, file.getAbsolutePath());
        return file;
    }
}
