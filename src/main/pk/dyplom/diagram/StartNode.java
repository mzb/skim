package pk.dyplom.diagram;

import com.mindfusion.diagramming.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pk.dyplom.Utils;
import pk.dyplom.runtime.RunEngine;

import javax.xml.transform.TransformerException;

/**
 * Blok startowy.
 * Oznacza początek wykonania.
 */
public class StartNode extends BaseNode {

    /** Układ punków styku dla linków (tylko jeden link wychodzący) */
    static final AnchorPattern ANCHOR_PATTERN = new AnchorPattern(
            new AnchorPoint[]{
                    new AnchorPoint(50, 100, false, true)
            }, "StartNode");

    /** Nazwa */
    private String name;
    /** Tablica nazw parametrów formalnych */
    private String[] params;

    /**
     * Inicjalizacja.
     * 
     * @param name Nazwa
     * @param params Parametry formalne
     */
    public StartNode(String name, String... params) {
        this.name = name;
        this.params = params;
        setShape(Shape.fromId("Circle"));
        setAnchorPattern(ANCHOR_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    public StartNode() {
        this("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseNode eval(RunEngine runEngine) throws RuntimeException {
        DiagramLinkList outgoingLinks = getAllOutgoingLinks();
        return outgoingLinks.isEmpty() ? null : (BaseNode) outgoingLinks.get(0).getDestination();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String compile(pk.dyplom.eval.Compiler compiler) {
        return compiler.emitNext(next());
    }

    /**
     * Zwraca listę parametrów formalnych.
     * 
     * @return lista parametrów formalnych
     */
    public String[] getParams() {
        return params;
    }

    /**
     * Zwraca przypisaną nazwę.
     * 
     * @return Nazwa
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nową nazwę.
     * 
     * @param name Nowa nazwa
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca parametry formalne jako łańcuch znakowy
     * 
     * @param separator Znak rozdzielający poszczególne parametry
     * @return Lista parametrów połączona w łańcuch znakowy
     */
    public String getParamsAsString(String separator) {
        return Utils.join(params, separator);
    }

    /**
     * @see StartNode#getParamsAsString(String)
     * @return Zwraca listę parametrów rozdzieloną znakiem przecinka (`' `)
     */
    public String getParamsAsString() {
        return getParamsAsString(", ");
    }

    /**
     * Ustawia listę parametrów.
     *
     * @param str Lista param. w formie łańcucha znaków
     * @param separator Separator
     */
    public void setParamsFromString(String str, String separator) {
        params = parseParams(str);
    }

    /**
     * Parsuje podany łańcuch znaków jako listę parametrów.
     *
     * @param str Łańcuch znaków
     * @return Tablica parametrów formalnych
     */
    public static String[] parseParams(String str) {
        return Utils.split(str, ",\\s*");
    }

    /**
     * Ustawia listę parametrów.
     *
     * @param str Łańcuch znaków
     * @see StartNode#setParamsFromString(String, String)
     */
    public void setParamsFromString(String str) {
        setParamsFromString(str, ",\\s*");
    }

    /**
     * Zwraca tekstową reprezentację bloku w formie *Nazwa*(*lista-parametrów*).
     * Użyteczne przy debugowaniu.
     *
     * @return Tekstowa reprezentacja bloku
     */
    @Override
    public String toString() {
        return name + "(" + getParamsAsString() + ")";
    }

    /**
     * {@inheritDoc}
     */
    public StartNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDataToXml(Element data, XmlPersistContext xml) {
        xml.addChildElement("Name", data, name);
        Element paramsElem = xml.addChildElement("Params", data);
        for (String p : params) {
            xml.addChildElement("Param", paramsElem, p);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException {
        name = xml.getChildElement(data, "Name").getTextContent();
        Element paramsElem = xml.getChildElement(data, "Params");
        NodeList paramsElemNodes = paramsElem.getElementsByTagName("Param");
        params = new String[paramsElemNodes.getLength()];
        for (int i = 0; i < paramsElemNodes.getLength(); i++) {
            params[i] = paramsElemNodes.item(i).getTextContent();
        }
    }
}
