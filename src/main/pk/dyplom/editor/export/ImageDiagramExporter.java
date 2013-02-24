package pk.dyplom.editor.export;

import com.mindfusion.diagramming.Diagram;
import pk.dyplom.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Eksporter diagramu do pliku graficznego.
 * Obsługiwane formaty: PNG, JPEG, GIF.
 */
public class ImageDiagramExporter implements IDiagramExporter {

    /**
     * {@inheritDoc}
     *
     * Format obrazka wybierany jest na podstawie rozszerzenia pliku.
     */
    @Override
    public File export(Diagram diagram, File file) {
        diagram.resizeToFitItems(8, true);
        BufferedImage image = diagram.createImage();
        String format = Utils.getFileExtension(file);
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
