package pk.dyplom.diagram;

import com.mindfusion.diagramming.*;
import org.w3c.dom.Element;
import pk.dyplom.runtime.RunEngine;

import javax.xml.transform.TransformerException;

/**
 * Blok końcowy.
 * Oznacza koniec ścieżki wykonywania.
 */
public class EndNode extends BaseNode {

    /** Układ punktów styku dla lików */
    static final AnchorPattern ANCHOR_PATTERN = new AnchorPattern(
            new AnchorPoint[]{
                    new AnchorPoint(50, 0, true, false)
            }, "EndNode");

    /** Zwracana wartość (nazwa zmiennej) */
    private String returnValue;

    /**
     * Inicjalizacja.
     *
     * @param returnValue Zwracana wartość (zmienna)
     */
    public EndNode(String returnValue) {
        this.returnValue = returnValue;
        setShape(Shape.fromId("Octagon"));
        setAnchorPattern(ANCHOR_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    public EndNode() {
        this("");
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
    @Override
    public String compile(pk.dyplom.eval.Compiler compiler) {
        StringBuilder code = new StringBuilder(compiler.emitReturn(returnValue));
        code.append(compiler.emitStop());
        return code.toString();
    }

    /**
     * Podaje zwracaną wartość.
     *
     * @return Zwracana wartość
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Ustawia zwracaną wartość.
     *
     * @param returnValue Nowa zwracana wartość
     */
    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
        setText(returnValue);
    }


    /**
     * {@inheritDoc}
     */
    public EndNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException {
        setReturnValue(xml.getChildElement(data, "Return").getTextContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDataToXml(Element data, XmlPersistContext xml) {
        xml.addChildElement("Return", data, getReturnValue());
    }
}
