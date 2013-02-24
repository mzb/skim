package pk.dyplom;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.PropertyResourceBundle;

/**
 * Klasa wspierająca internacjonalizację (wersje językowe).
 * Przechowuje tłumaczenia (pary klucz-wartość) zawarte w plikach *.properties
 * (@see java.util. Properties)
 */
public class I18n {

    /**
     * Zbiór tłumaczeń zawartych w pliku *.properties dla aktualnego języka
     * (patrz zmienna konfiguracyjna `lang').
     */
    private static PropertyResourceBundle bundle;

    static {
        try {
            bundle = new PropertyResourceBundle(new InputStreamReader(new FileInputStream(
                    String.format("config/%s.properties", Config.DEFAULT.getString("lang", "pl"))),
                    "UTF8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Alias dla metody translate.
     * @see I18n#translate
     */
    public static String t(String key, Object... params) {
        return translate(key, params);
    }

    /**
     * Zwraca tłumaczenie dla danego klucza.
     *
     * @param key Klucz
     * @param params Wartości parametrów, które maja być wstawione w kolejne miejsca w tłumaczeniu
     *               {0}, {1}, itd
     * @return Tłumaczenie dla podanego klucza lub klucz jeśli tłumaczenie nie istnieje
     *
     * @code {
     *      // plik z tłumaczeniami
     *      log.diagramSaved = Diagram {0} został zapisany
     *
     *      I18n.translate("log.diagramSaved", "diagram1"); // => "Diagram diagram1 został zapisany"
     * }
     */
    public static String translate(String key, Object... params) {
        String text = null;
        try {
            text = MessageFormat.format(bundle.getString(key), params);
        } catch (Exception e) {}
        if (text == null || "".equals(text)) {
            text = key;
        }
        return text;
    }
}
