package pk.dyplom.editor;

import pk.dyplom.Utils;
import pk.dyplom.ui.FileChooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Okno do wyboru pliku do odczytu/zapisu.
 */
public class DiagramFileChooser extends FileChooser {

    /** Rozszerzenie pliku diagramu */
    public static final String DIAGRAM_FILE_EXTENSION = "xml";

    /**
     * Inicjuj komponent.
     */
    public DiagramFileChooser() {
        super();

        setAcceptAllFileFilterUsed(false);
        addChoosableFileFilter(new DiagramFileFilter());
    }


    /**
     * Filtr plików.
     */
    public static class DiagramFileFilter extends FileFilter {

        /**
         * Filtruj pliki widoczne w oknie wg rozszerzenia.
         *
         * @param f Plik
         * @return {@code true} jeśli plik jest plikiem diagramu, {@code false} w przeciwnym wypadku.
         */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            return DIAGRAM_FILE_EXTENSION.equals(Utils.getFileExtension(f));
        }

        /**
         * Zwróć opis typu pliku.
         *
         * @return Opis typu pliku
         */
        @Override
        public String getDescription() {
            return "*." + DIAGRAM_FILE_EXTENSION;
        }
    }
}
