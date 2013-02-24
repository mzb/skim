package pk.dyplom;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Klasa obsługująca zmienne konfiguracyjne przechowywane w plikach *.properties.
 */
public class Config {

    /**
     * Domyslna konfiguracja (`config/default.properties')
     */
    public static final Config DEFAULT = new Config("config/default.properties");
    
    private Properties properties = new Properties();

    /**
     * Załaduj zmienne konfiguracyjne z podanego pliku *.properties.
     * 
     * @param filename Nazwa pliku
     */
    public Config(String filename) {
        try {
            properties.load(new InputStreamReader(new FileInputStream(filename), "UTF8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Zwróć wartość zmiennej o podanej nazwie jako {@code String}.
     * @see Config#get(String, Object) 
     * @return Wartość zmiennej jako {@code String}
     */
    public String getString(String key, String defaultValue) {
        Object v = get(key, defaultValue);
        return  v != null ? (String) v : null;
    }

    /**
     * Zwróć wartość zmiennej o podanej nazwie jako {@code Integer}.
     * @see Config#get(String, Object) 
     * @return Wartość zmiennej jako {@code Integer}
     */
    public Integer getInt(String key, Integer defaultValue) {
        Object v = get(key, defaultValue);
        if (v == null) return null;
        return Integer.parseInt((String) v);
    }

    /**
     * Zwróć wartość zmiennej o podanej nazwie jako {@code Color}.
     * @see Config#get(String, Object)
     * @return Wartość zmiennej jako {@code Color}
     */
    public Color getColor(String key, Color defaultValue) {
        Object v = get(key, defaultValue);
        if (v == null) return null;
        return Color.decode((String) v);
    }

    /**
     * Zwróć wartość zmiennej o podanej nazwie.
     *
     * @param key Nazwa zmiennej
     * @param defaultValue Domyślna wartość jeśli zmienna nie ma przypisanej wartości lub nie istnieje
     * @return  Wartość zmiennej
     */
    private Object get(String key, Object defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        return value;
    }
}
