package pk.dyplom.diagram;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.Shape;
import pk.dyplom.I18n;
import pk.dyplom.eval.Compiler;

/**
 * Blok wyjścia.
 * Pozwala na wyświetlenie komunikatu użytkownikowi.
 */
public class OutputNode extends InputOutpuNode {

    /**
     * Inicjalizacja.
     *
     * @param message Komunikat
     */
    public OutputNode(String message) {
        setMessage(message);
        setShape(Shape.fromId("Save"));
        setAnchorPattern(ANCHOR_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    public OutputNode() {
        this("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String compile(Compiler compiler) {
        StringBuilder code = new StringBuilder();
        code.append(String.format("$outputDialog(%s, '%s');",
                Compiler.interpolate(Compiler.escape(message)), I18n.t("diagram.node.OutputNode")));
        code.append(compiler.emitNext(next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    public OutputNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * Ustawia tekst wewnątrz bloku jako sformatowany komunikat.
     *
     * @param text Tekst komunikatu
     */
    @Override
    public void setText(String text) {
        super.setText(formatMessage(text));
    }
}
