package pk.dyplom.codegen;


import com.mindfusion.diagramming.Diagram;
import pk.dyplom.diagram.*;

/**
 * Interfejs wspólny dla generatorów kodu.
 * Generator kodu jest komponentem przpekształcającym diagram na kod w danym języku programowania.
 */
public interface ICodeGenerator {

    /**
     * Zwraca rozszerzenie dla plików zawierających wygenerowany kod.
     *
     * @return Rozszerzenie pliku (bez kropki)
     */
    public String getExtension();

    /**
     * Zastepuje nazwy zmiennych użytych w danym literale łańcuchowym ich wartościami.
     *
     * **Przykład:**
     *
     *     // Dla generatora kodu C++
     *     cppGenerator.interpolate("x = $x"); // => "\"x = \" << x << \"\"";
     *
     * @param s Literał łańcuchowy zawierający nazwy zmiennych
     * @return Łańcuch z nazwami zmiennych zastąpionym przez ich wartości
     */
    public String interpolate(String s);

    /**
     * Zamienia znaki mające specjalne znaczenie w danym języku na literalne odpowiedniki.
     *
     * **Przykład:**
     *
     *     // Dla generatora kodu C++
     *     cppGenerator.escape("\"abc\""); // => "\\\"abc\\\"";
     *
     * @param s Łańcuch zawierający specjalne znaki
     * @return Łańcuch z zamienionymi specjalnymi znakami
     */
    public String escape(String s);

    /**
     * Generuje kod dla podanego diagramu.
     *
     * @param diagram Diagram dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(Diagram diagram);

    /**
     * Generuje kod dla bloku startowego.
     *
     * @param node Blok startowy dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(StartNode node);

    /**
     * Generuje kod dla bloku końcowego.
     *
     * @param node Blok końcowy dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(EndNode node);

    /**
     * Generuje kod dla bloku przetwarzania.
     *
     * @param node Blok przetwarzania dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(ProcessingNode node);

    /**
     * Generuje kod dla bloku warunkowego.
     *
     * @param node Blok warunkowy dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(ConditionalNode node);

    /**
     * Generuje kod dla bloku wejścia.
     *
     * @param node Blok wejścia dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(InputNode node);

    /**
     * Generuje kod dla bloku wyjścia.
     *
     * @param node Blok wyjścia dla którego ma być wygenerowany kod
     * @return Kod
     */
    public String generate(OutputNode node);

    /**
     * Formatuje wygenerowany kod.
     *
     * @param code Wygenerowany kod do sformatowania
     * @return Sformatowany kod
     */
    public String format(String code);
}
