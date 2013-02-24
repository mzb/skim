package pk.dyplom.diagram;

import com.mindfusion.diagramming.*;
import org.w3c.dom.Element;
import pk.dyplom.runtime.RunEngine;

import javax.xml.transform.TransformerException;
import java.awt.*;
import java.util.List;

/**
 * Bazowa klasa dla wszystkich bloków diagramu.
 */
abstract public class BaseNode extends ShapeNode {

    protected static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 3);
    protected static final TextFormat TEXT_FORMAT = new TextFormat(Align.Center, Align.Center);

    static {
        TEXT_FORMAT.setWrapAtCharacter(false);
    }

    /** Komentarz */
    protected String comment;

    /**
     * Inicjalizacja bloku.
     */
    public BaseNode() {
        setConstraints(constraints());
        resize(getConstraints().getMinWidth(), getConstraints().getMinHeight());
        setTextFormat(TEXT_FORMAT);
        setEnableStyledText(true);
    }

    /**
     * Renderuje blok.
     * @param g Contekst graficzny
     * @param opts Opcje {zob. @link RenderOptions}
     */
    @Override
    public void draw(Graphics2D g, RenderOptions opts) {
        setFont(FONT);
        super.draw(g, opts);
    }

    /**
     * Definiuje ograniczenia graficznej reprezentacji bloku.
     *
     * @return Ograniczenia
     */
    protected NodeConstraints constraints() {
        NodeConstraints constraints = new NodeConstraints();
        constraints.setMinHeight(20);
        constraints.setMinWidth(20);
        return constraints;
    }

    /**
     * Dopasowywuje rozmiar bloku do rozmiaru tekstu z uwzględnieniem ograniczeń zdefiniowanych
     * w metodzie `constraints`.
     *
     * @param fitSize Metoda dopasowania
     * @return
     */
    @Override
    public boolean resizeToFitText(FitSize fitSize) {
        boolean resized = super.resizeToFitText(fitSize);
        // super.resizeToFitText nie uwzglednia limitow okreslanych w constraints()
        if (getBounds().getWidth() < getConstraints().getMinWidth()) {
            resize(getConstraints().getMinWidth(), (float) getBounds().getHeight());
        }
        if (getBounds().getHeight() < getConstraints().getMinHeight()) {
            resize((float) getBounds().getWidth(), getConstraints().getMinHeight());
        }
        return resized;
    }

    /**
     * Dokonuje ewaulacji bloku.
     * Używane tylko do tesów.
     *
     * @param runEngine Ewaluator
     * @return Rezultat ewaluacji
     * @throws RuntimeException W przypadku błędu wykonania
     */
    public abstract BaseNode eval(RunEngine runEngine) throws RuntimeException;

    /**
     * Kompiluje blok do formy wykonywalnej przez ewaluator.
     *
     * @param compiler Instancja kompilatora
     * @return Skompilowana forma bloku (kod)
     */
    public abstract String compile(pk.dyplom.eval.Compiler compiler);

    /**
     * Zwraca blok bezpośrednio następujący po tym.
     *
     * @return Następny blok
     */
    public BaseNode next() {
        DiagramLinkList outgoingLinks = getAllOutgoingLinks();
        return outgoingLinks.isEmpty() ? null : (BaseNode) outgoingLinks.get(0).getDestination();
    }

    /**
     * Zwraca komentarz.
     *
     * @return Komentarz
     */
    public String getComment() {
        return comment;
    }

    /**
     * Ustawia nowy komentarz oraz tooltip.
     *
     * @param comment Nowy komentarz
     */
    public void setCommentWithTooltip(String comment) {
        this.comment = comment;
        setToolTip(this.comment);
    }

    /**
     * Ustawia nowy komentarz.
     *
     * @param comment Nowy komentarz
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Metoda wywoływana ptzy tworzeniu połączenia wychodzącego z bloku.
     *
     * @param event Zdarzenie
     */
    public void onLinkCreating(LinkValidationEvent event) {

    }

    /**
     * Dodaje etykietę do danego połączenia.
     *
     * @param label Etykieta
     * @param link Połącznie między blokami
     */
    protected void addLabelToLink(LinkLabel label, DiagramLink link) {
        List<LinkLabel> linkLabels = link.getLabels();
        if (linkLabels == null || !linkLabels.contains(label)) {
            link.addLabel(label);
        }
    }

    /**
     * Konstruktor na potrzebny serializacji.
     *
     * @param diagram Instancja diagramu
     */
    public BaseNode(Diagram diagram) {
        super(diagram);
    }

    /**
     * Zapisuje blok do pliku XML.
     *
     * @param element Tag XML reprezentujący blok
     * @param xml Contekst XML
     */
    @Override
    protected void saveToXml(Element element, XmlPersistContext xml) {
        Element data = xml.addChildElement("Data", element);
        saveDataToXml(data, xml);
        xml.addChildElement("Comment", data, getComment());

        super.saveToXml(element, xml);
    }

    /**
     * Zapisuje dane specyficzne dla danego bloku do pliku XML.
     *
     * @param data Tag XML zawierający dane
     * @param xml Kontekst XML
     */
    protected abstract void saveDataToXml(Element data, XmlPersistContext xml);

    /**
     * Wczytuje blok z pliku XML.
     *
     * @param element Tag XML reprezentujący blok
     * @param xml Kontekst XML
     * @throws TransformerException
     * @throws XmlException
     */
    @Override
    protected void loadFromXml(Element element, XmlPersistContext xml) throws TransformerException, XmlException {
        Element data = xml.getChildElement(element, "Data");
        setCommentWithTooltip(xml.getChildElement(data, "Comment").getTextContent());
        loadDataFromXml(data, xml);

        super.loadFromXml(element, xml);
    }

    /**
     * Wczytuje z pliku XML dane specyficzne dla danego bloku.
     *
     * @param data Tag XML reprezentujący dane
     * @param xml Kontekst XML
     * @throws TransformerException
     * @throws XmlException
     */
    protected abstract void loadDataFromXml(Element data, XmlPersistContext xml) throws TransformerException, XmlException;
}
