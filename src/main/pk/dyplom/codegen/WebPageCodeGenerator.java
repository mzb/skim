package pk.dyplom.codegen;

import com.mindfusion.diagramming.Diagram;
import net.sf.jtpl.Template;
import org.apache.commons.codec.binary.Base64;
import pk.dyplom.I18n;
import pk.dyplom.diagram.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Generator strony internetowej (HTML).
 * 
 * Umieszcza na stronie kod w JavaScript ({@link JavaScriptCodeGenerator}) oraz graficzną reprezentację diagramu
 * w formie obrazka (zagnieżdżonego bezpośrednio na stronie).
 * Strona generowana jest wg szablonu `template/webpage.html`.
 */
public class WebPageCodeGenerator implements ICodeGenerator {

    private JavaScriptCodeGenerator jsGenerator = new JavaScriptCodeGenerator();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtension() {
        return "html";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(String code) {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(Diagram diagram) {
        Template template;
        try {
            template = new Template(new InputStreamReader(WebPageCodeGenerator.class.getResourceAsStream(
                    "template/webpage.html")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        template.assign("TITLE", diagram.getToolTip() == null ? "" : diagram.getToolTip());
        template.assign("IMAGE", getImageDataURI(diagram, "png"));
        template.assign("CODE", jsGenerator.format(jsGenerator.generate(diagram)));
        template.assign("RUN_TEXT", I18n.t("action.runDiagram"));
        template.assign("RUN_PROMPT", I18n.t("action.runDiagramPrompt"));

        template.parse("main");
        return template.out();
    }

    /**
     * Zwraca diagram jako obrazek zakodowany w `Base64` w formie `data:image`.
     *
     * @param diagram Diagram
     * @param format Format obrazka (PNG, JPEG, GIF)
     * @return Obrazek zakodowany jako `data:image`
     */
    private String getImageDataURI(Diagram diagram, String format) {
        diagram.resizeToFitItems(8, true);
        BufferedImage image = diagram.createImage();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.format("data:image/%s;base64,%s", format,
                Base64.encodeBase64String(stream.toByteArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String interpolate(String s) {
        return s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String escape(String s) {
        return s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(StartNode node) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(EndNode node) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(ProcessingNode node) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(ConditionalNode node) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(InputNode node) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(OutputNode node) {
        return null;
    }
}
