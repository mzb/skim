package pk.dyplom.diagram;

import com.mindfusion.diagramming.*;
import org.w3c.dom.Element;
import pk.dyplom.runtime.RunEngine;

import javax.xml.transform.TransformerException;

/**
 * Blok warunkowy.
 * Pozwala uzależnić sciężkę wykonania od zadanego warunku.
 */
public class ConditionalNode extends BaseNode {

    /** Układ punktów styku dla linków */
    static final AnchorPattern ANCHOR_PATTERN = new AnchorPattern(
            new AnchorPoint[]{
                    new AnchorPoint(0, 50, false, true),
                    new AnchorPoint(50, 0, true, false),
                    new AnchorPoint(100, 50, false, true)
            }, "ConditionalNode");

    /** Wyrażenie warunkowe */
    private String condition;

    /**
     * Inicjalizacja.
     * 
     * @param condition Wyrażenie warunkowe
     */
    public ConditionalNode(String condition) {
        this.condition = condition;
        setText(condition);
        setShape(Shape.fromId("Decision"));
        setAnchorPattern(ANCHOR_PATTERN);
    }

    /**
     * Inicjalizacja.
     */
    public ConditionalNode() {
        this("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseNode eval(RunEngine runEngine) throws RuntimeException {
        boolean isTrue = (Boolean) runEngine.eval(condition);

        DiagramLink link;
        if (isTrue) {
            link = getThenLink();
        } else {
            link = getElseLink();
        }
        return link != null ? (BaseNode) link.getDestination() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String compile(pk.dyplom.eval.Compiler compiler) {
        StringBuilder code = new StringBuilder();
        code.append(compiler.emitIf(condition, null));
        DiagramLink thenLink = getThenLink();
        code.append(compiler.emitNext(thenLink == null ? null : (BaseNode) thenLink.getDestination()));
        DiagramLink elseLink = getElseLink();
        code.append("else ");
        code.append(compiler.emitNext(elseLink == null ? null : (BaseNode) elseLink.getDestination()));
        return code.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * Następny blok określany jest na podstawie wyrażenia warunkowego.
     */
    @Override
    public BaseNode next() {
        return null;
    }

    /**
     * Zwraca połączenie {@link ThenLink} (`TAK`) wykonywane jeśli wyrażenie warunkowe jest prawdziwe.
     * 
     * @return Połączenie {@link ThenLink} lub `null`
     */
    public DiagramLink getThenLink() {
        DiagramLinkList links = getAllOutgoingLinks();
        for (DiagramLink link : links) {
            for (LinkLabel label : link.getLabels()) {
                if (label instanceof ThenLink.Label || ThenLink.Label.TEXT.equals(label.getText())) {
                    return link;
                }
            }
        }
        return null;
    }

    /**
     * Zwraca połączenie {@link ElseLink} (`NIE`) wykonywane jeśli wyrażenie warunkowe jest fałszywe.
     *
     * @return Połączenie {@link ElseLink} lub `null`
     */
    public DiagramLink getElseLink() {
        DiagramLinkList links = getAllOutgoingLinks();
        for (DiagramLink link : links) {
            for (LinkLabel label : link.getLabels()) {
                if (label instanceof ElseLink.Label || ElseLink.Label.TEXT.equals(label.getText())) {
                    return link;
                }
            }
        }
        return null;
    }

    /**
     * Zwraca wyrażenie warunkowe.
     *
     * @return Wyrażenie warunkowe
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Ustawia wyrażenie warunkowe.
     *
     * @param condition Nowe wyrażenie warunkowe
     */
    public void setCondition(String condition) {
        this.condition = condition;
        setText(condition);
    }

    /**
     * {@inheritDoc}
     *
     * Tworzy połączenie typu `ThenLink` lub `ElseLink` jeśli to pierwsze już istnieje.
     */
    @Override
    public void onLinkCreating(LinkValidationEvent event) {
        DiagramLink thenLink = getThenLink();
        DiagramLink elseLink = getElseLink();
        DiagramLink newLink = event.getLink();

        if (thenLink != null && elseLink != null) {
            event.setCancel(true);
            return;
        }
        if (thenLink == null) {
            addLabelToLink(new ThenLink.Label(newLink), newLink);
            return;
        }
        if (elseLink == null) {
            addLabelToLink(new ElseLink.Label(newLink), newLink);
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    public ConditionalNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDataToXml(Element data, XmlPersistContext xml) {
        xml.addChildElement("Condition", data, getCondition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException {
        setCondition(xml.getChildElement(data, "Condition").getTextContent());
    }
}
