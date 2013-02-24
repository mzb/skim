package pk.dyplom;

import java.io.*;

/**
 * Klasa zawierająca zbiór różnych metod użytkowych, wykorzystywanych
 * przez pozostałe komponenty aplikacji.
 */
public class Utils {

    /**
     * Połącz podane wartości w łańcuch rozdzielony podanym separatorem.
     * Wartości przy łączeniu konwertowane są do typu String za pomocą metody @see Object#toString.
     *
     * @param values Tablica wartości
     * @param separator Separator
     * @return Wartości połączone w jeden łancuch znakowy
     *
     * @code {
     *      Utils.join(new Integer[]{ 1,2,3 }, ", "); // => "1, 2, 3"
     * }
     */
    public static String join(Object[] values, String separator) {
        if (values == null || values.length == 0) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            str.append(values[i].toString());
            if (i < values.length - 1) {
                str.append(separator);
            }
        }
        return str.toString();
    }

    /**
     * Rozdziel podany łańcuch znaków na tablicę łańcuchów na podstawie separatora.
     * Działa podobnie do standardowego String#split, ale dla pustego łańcucha zwracana jest pusta tablica.
     *
     * @param str Łańcuch znaków do podziału
     * @param separator Separator
     * @return Tablica łańcuchów
     *
     * @code {
     *      Utils.split("1, 2, 3", ", "); // => ["1", "2", "3"]
     * }
     */
    public static String[] split(String str, String separator) {
        if (str.isEmpty()) {
            return new String[]{};
        }
        return str.split(separator);
    }

    /**
     * Zwróć rozszerzenie pliku (po kropce, na podstawie nazwy).
     *
     * @param file Plik
     * @return Rozszerzenie pliku (bez kropki, małymi literami)
     *
     * @code {
     *      Utils.getFileExtension(new File("abc.txt")); // => "txt"
     * }
     */
    public static String getFileExtension(File file) {
        String ext = null;
        String name = file.getName();
        int i = name.lastIndexOf('.');

        if (i > 0 &&  i < name.length() - 1) {
            ext = name.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /**
     * Zapisz plik tekstowy.
     *
     * @param file Plik
     * @param content Zawartość tekstowa do zapisania do pliku
     *
     * @code {
     *     Utils.writeTextFile(new File("abc.txt", "tekst");
     * }
     */
    public static void writeTextFile(File file, String content) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF8"));
            try {
                writer.print(content);
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dodaj podane rozszerzenie do nazwy pliku, jeśli plik go jeszcze nie ma.
     *
     * @param file Plik
     * @param extension Rozszerzenie (bez kropki)
     * @return Plik z dodanym rozszerzeniem lub oryginalny
     *
     * @code {
     *     Utils.forceFileExtension(new File("abc"), "txt"); // => new File("abc.txt")
     *     Utils.forceFileExtension(new File("abc.txt"), "txt"); // => new File("abc.txt")
     * }
     */
    public static File forceFileExtension(File file, String extension) {
        if (!extension.equals(getFileExtension(file))) {
            file = new File(String.format("%s.%s", file.getAbsolutePath(), extension));
        }
        return file;
    }
}
