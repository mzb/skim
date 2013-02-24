package pk.dyplom.editor.export;

import pk.dyplom.I18n;
import pk.dyplom.Utils;
import pk.dyplom.ui.FileChooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Okno wybory pliku do zapisu wyeksportowanej wersji diagramu.
 */
public class ExportFileChooser extends FileChooser {

    /**
     * Inicjuj komponent
     */
    public ExportFileChooser() {
        super();

        setAcceptAllFileFilterUsed(false);
        addChoosableFileFilter(new ImageFileFilter());
        addChoosableFileFilter(new PDFFileFilter());
    }


    /**
     * Filtr dla obrazków.
     */
    public static class ImageFileFilter extends FileFilter {

        /**
         * Filtruj pliki nie będące obrazkami.
         *
         * @param f Plik
         * @return {@code true} jeśli plik jest obrazkiem lub {@code false} w przeciwnym razie
         */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String fileExt = Utils.getFileExtension(f);
            return "png".equalsIgnoreCase(fileExt) ||
                    "jpeg".equalsIgnoreCase(fileExt) ||
                    "gif".equalsIgnoreCase(fileExt);
        }

        /**
         * Zwróć opis typu pliku.
         *
         * @return Opis typu pliku
         */
        @Override
        public String getDescription() {
            return I18n.t("fileChooser.images") + " (*.png, *.jpeg, *.gif)";
        }
    }


    public static class PDFFileFilter extends FileFilter {

        /**
         * Filtruj pliki nie będące plikami PDF.
         *
         * @param f Plik
         * @return {@code true} jeśli plik jest plikiem PDF lub {@code false} w przeciwnym razie
         */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String fileExt = Utils.getFileExtension(f);
            return "pdf".equalsIgnoreCase(fileExt);
        }

        /**
         * Zwróć opis typu pliku.
         *
         * @return Opis typu pliku
         */
        @Override
        public String getDescription() {
            return "*.pdf";
        }
    }
}
