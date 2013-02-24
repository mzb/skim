package pk.dyplom.editor.export;

import com.mindfusion.diagramming.Diagram;

import java.io.File;

/**
 * Interfejs wspólny dla eksporterów diagramów.
 */
public interface IDiagramExporter {

    /**
     * Eksportuje diagram i zapisuje w podanym pliku.
     *
     * @param diagram Diagram do eksportu
     * @param file Plik, gdzie ma być zapisany rezultat
     * @return Plik po zapisie
     */
    public File export(Diagram diagram, File file);
}
