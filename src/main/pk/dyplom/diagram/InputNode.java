package pk.dyplom.diagram;

import com.mindfusion.diagramming.Diagram;
import com.mindfusion.diagramming.Shape;
import com.mindfusion.diagramming.XmlException;
import com.mindfusion.diagramming.XmlPersistContext;
import org.w3c.dom.Element;
import pk.dyplom.I18n;
import pk.dyplom.eval.Compiler;

import javax.xml.transform.TransformerException;

/**
 * Blok wejścia.
 * Pozwala na wprowadzenie danych przez użytkownika.
 */
public class InputNode extends InputOutpuNode {

    /** Zmienna do której ma zostać przypisana wprowadzona wartość */
    private String variable;

    /**
     * Inicjalizacja.
     *
     * @param message Komunikat wyświetlony dla użytkownika
     * @param variable Nazwa zmiennej, do której ma zostać przypisana wartość
     */
    public InputNode(String message, String variable) {
        this.message = message;
        this.variable = variable;
        setText(formatText(message, variable));
        setShape(Shape.fromId("RSave"));
        setAnchorPattern(ANCHOR_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    public InputNode() {
        this("", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String compile(Compiler compiler) {
        StringBuilder code = new StringBuilder();
        code.append(String.format(
                "var %s = $inputDialog(%s, '%s');",
                variable, Compiler.interpolate(Compiler.escape(message)), I18n.t("diagram.node.InputNode")));
        code.append(compiler.emitNext(next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    public InputNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDataToXml(Element data, XmlPersistContext xml) {
        super.saveDataToXml(data, xml);
        xml.addChildElement("Variable", data, getVariable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException {
        super.loadDataFromXml(data, xml);
        setVariable(xml.getChildElement(data, "Variable").getTextContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessage(String message) {
        super.setMessage(message);
        setText(formatText(message, variable));
    }

    /**
     * Zwraca nazwę zmiennej.
     *
     * @return Nazwa zmiennej
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Ustawia nazwę zmiennej do której ma być przypisana wprwadzona wartość.
     *
     * @param variable Nazwa zmiennej
     */
    public void setVariable(String variable) {
        this.variable = variable;
        setText(formatText(message, variable));
    }

    /**
     * Formatuje tekst wewnątrz bloku.
     *
     * @param message Komunikat
     * @param variable Nazwa zmiennej
     * @return Sformatowany tekst
     */
    private String formatText(String message, String variable) {
        return String.format("%s\n%s", formatMessage(message), formatVariable(variable));
    }

    /**
     * Formatuje nazwę zmiennej.
     *
     * @param variable Nazwa zmiennej
     * @return Sformatowana nazwa zmiennej
     */
    private String formatVariable(String variable) {
        return String.format("<b>%s</b> <<", variable);
    }
}
