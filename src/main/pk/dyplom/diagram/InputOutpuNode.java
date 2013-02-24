package pk.dyplom.diagram;

import com.mindfusion.diagramming.*;
import org.w3c.dom.Element;
import pk.dyplom.runtime.RunEngine;

import javax.xml.transform.TransformerException;

/**
 * Wspólna klasa bazowa dla bloków wejścia/wyjścia.
 */
public abstract class InputOutpuNode extends BaseNode {

    /** Układ punktów styku dla linków */
    static final AnchorPattern ANCHOR_PATTERN = new AnchorPattern(
            new AnchorPoint[]{
                    new AnchorPoint(0, 50, true, false),
                    new AnchorPoint(50, 0, true, false),
                    new AnchorPoint(100, 50, true, false),
                    new AnchorPoint(50, 100, false, true)
            }, "InputOutputNode");

    /** Komunikat dla użytkownika */
    protected String message;

    protected InputOutpuNode() {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected NodeConstraints constraints() {
        NodeConstraints constraints = super.constraints();
        constraints.setMinWidth(40);
        return constraints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseNode eval(RunEngine runEngine) throws RuntimeException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public InputOutpuNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDataToXml(Element data, XmlPersistContext xml) {
        xml.addChildElement("Message", data, getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException {
        setMessage(xml.getChildElement(data, "Message").getTextContent());
    }

    /**
     * Zwraca komunikat dla użytkownika.
     *
     * @return Komunikat dla użytkownika
     */
    public String getMessage() {
        return message;
    }

    /**
     * Ustawia komunikat dla użytkownika.
     *
     * @param message Nowy komunikat
     */
    public void setMessage(String message) {
        this.message = message;
        setText(message);
    }

    /**
     * Formatuje komunikat.
     *
     * @param text Komunikat
     * @return Sformatowany komunikat
     */
    protected String formatMessage(String text) {
        return String.format("<i>%s</i>", pk.dyplom.eval.Compiler.interpolateWith(text, "\\$<b>$1</b>"));
    }
}
