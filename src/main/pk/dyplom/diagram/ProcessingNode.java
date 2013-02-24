package pk.dyplom.diagram;

import com.mindfusion.diagramming.*;
import org.w3c.dom.Element;
import pk.dyplom.runtime.RunEngine;

import javax.xml.transform.TransformerException;

/**
 * Blok przetwarzania.
 */
public class ProcessingNode extends BaseNode {

    /** Układ punktów styku dla linków */
    static final AnchorPattern ANCHOR_PATTERN = new AnchorPattern(
            new AnchorPoint[]{
                    new AnchorPoint(0, 50, true, false),
                    new AnchorPoint(50, 0, true, false),
                    new AnchorPoint(100, 50, true, false),
                    new AnchorPoint(50, 100, false, true)
            }, "ProcessingNode");

    /** Operacje */
    private String operations;

    /**
     * Inicjalizacja.
     *
     * @param operations Operacje
     */
    public ProcessingNode(String operations) {
        this.operations = operations;
        setText(operations);
        setShape(Shape.fromId("RoundRect"));
        setAnchorPattern(ANCHOR_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    public ProcessingNode() {
        this("");
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
        runEngine.eval(operations);
        DiagramLinkList links = getAllOutgoingLinks();
        return links.isEmpty() ? null : (BaseNode) links.get(0).getDestination();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String compile(pk.dyplom.eval.Compiler compiler) {
        StringBuilder code = new StringBuilder();
        code.append(operations.trim()).append(" ");
        code.append(compiler.emitNext(next()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     */
    public ProcessingNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDataToXml(Element data, XmlPersistContext xml) {
        xml.addChildElement("Operations", data, getOperations());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException {
        setOperations(xml.getChildElement(data, "Operations").getTextContent());
    }

    /**
     * Zwraca operacje
     *
     * @return Operacje
     */
    public String getOperations() {
        return operations;
    }

    /**
     * Definiuje nowe operacje do wykonania.
     *
     * @param operations Operacje
     */
    public void setOperations(String operations) {
        this.operations = operations;
        setText(operations);
    }
}
