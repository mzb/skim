package pk.dyplom.editor.form;

import com.mindfusion.diagramming.ChangeItemCmd;
import com.mindfusion.diagramming.FitSize;
import pk.dyplom.I18n;
import pk.dyplom.diagram.*;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Bazowa klasa reprezentująca formularz edycyjny bloku diagramu.
 *
 * @param <N> Typ bloku
 */
public abstract class NodeForm<N extends BaseNode> extends JDialog implements ActionListener {

    /** Kolor pola zawierającego błąd */
    protected static final Color FIELD_ERROR_COLOR = new Color(255, 235, 232);
    /** Ramka pola zawierającego błąd */
    protected static final Border FIELD_ERROR_BORDER = BorderFactory.createLineBorder(new Color(221, 60, 16));
    /** Czcionka dla pola zawierającego kod */
    protected static final Font CODE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /** Domyślne atrybuty pól formularza (używane do przywracania po błędach) */
    protected Map<JComponent, FieldAttrs> defaultFieldAttrs = new HashMap<JComponent, FieldAttrs>();

    /** Edytowany blok diagramu */
    protected N node;
    /** Pole zawierające komentarz */
    protected JTextArea commentTextArea = new JTextArea();
    /** Przycisk do zapisania zmian */
    protected JButton saveButton = new JButton(I18n.t("btn.commit"));
    /** Przycisk do anulowania zmian */
    protected JButton cancelButton = new JButton(I18n.t("btn.cancel"));

    /** Zainicjuj elementy formularza */
    abstract protected void initComponents();

    /** Zwróc używane pola formularza */
    abstract protected JComponent[] fields();

    /**
     * Zwróć odpowiednią instancję formularza dla podanego typu bloku.
     *
     * @param node Blok, dla którego ma być stworzony formularz
     * @return Instancja formularza w zależności od typu danego bloku
     */
    public static NodeForm formFor(BaseNode node) {
        NodeForm form = null;

        if (node instanceof StartNode)
            form = new StartNodeForm((StartNode) node);
        if (node instanceof EndNode)
            form = new EndNodeForm((EndNode) node);
        if (node instanceof ProcessingNode)
            form = new ProcessingNodeForm((ProcessingNode) node);
        if (node instanceof ConditionalNode)
            form = new ConditionalNodeForm((ConditionalNode) node);
        if (node instanceof OutputNode)
            form = new OutputNodeForm((OutputNode) node);
        if (node instanceof InputNode)
            form = new InputNodeForm((InputNode) node);

        return form;
    }

    /**
     * Inicjuj (bez wyświetlania) formularz dla danego bloku.
     *
     * @param node Edytowany blok
     */
    public NodeForm(N node) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setResizable(false);
        initComponents();
        saveButton.setIcon(new ImageIcon(GUI.class.getResource("icon/ok.png")));
        cancelButton.setIcon(new ImageIcon(GUI.class.getResource("icon/remove.png")));

        this.node = node;

        commentTextArea.setText(node.getComment());
        saveButton.setActionCommand("node.save");
        saveButton.addActionListener(this);
        cancelButton.setActionCommand("node.cancel");
        cancelButton.addActionListener(this);

        for (JComponent f : fields()) {
            defaultFieldAttrs.put(f, new FieldAttrs(f.getBackground(), f.getBorder(), f.getToolTipText()));
        }
    }

    /**
     * Sprawdź poprawność danych formularza.
     *
     * @return {@code true} jeśli dane są poprawne, {@code false} w przeciwnym wypadku.
     */
    protected boolean verify() {
        return true;
    }

    /**
     * Oznacz dane pole formularza jako zawierające błąd.
     *
     * @param field Pole formularza
     * @param errorMessage Komunikat o błędzie (wyświetlany jako tooltip).
     */
    protected void highlightFieldError(JComponent field, String errorMessage) {
        field.setBackground(FIELD_ERROR_COLOR);
        field.setBorder(FIELD_ERROR_BORDER);
        field.setToolTipText(errorMessage);
        field.requestFocusInWindow();
    }

    /**
     * Przywróc domyślne atrybuty danego pola.
     *
     * @param field Pole formularza
     */
    protected void unhiglightField(JComponent field) {
        FieldAttrs defaultAttrs = defaultFieldAttrs.get(field);
        if (defaultAttrs != null) {
            field.setBackground(defaultAttrs.color);
            field.setBorder(defaultAttrs.border);
            field.setToolTipText(defaultAttrs.tooltip);
        }
    }

    /**
     * Wykonaj dodatkowe akcje po wybraniu zapisania zmian.
     */
    protected void onSave() {

    }

    /**
     * Wykonaj dodatkowe akcje po wybraniu anulowania zmian.
     */
    protected void onCancel() {

    }

    /**
     * Wyświetl formularz.
     *
     * @param parent Nadrzędny kontener
     */
    public void open(Container parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Zamknij formularz.
     */
    public void close() {
        dispose();
    }

    /**
     * Obsłuż akcję użytkownika (wybranie zapisu lub anulowania zmian).
     *
     * @param e Zdarzenie reprezentujące akcję użytkownika
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("node.save".equals(cmd)) {
            boolean valid = verify();
            if (valid) {
                // Zarejestruj na potrzeby Confnij/Wykonaj ponownie
                ChangeItemCmd changeItemCmd = new ChangeItemCmd(node, "Edit node");
                    node.setCommentWithTooltip(commentTextArea.getText());
                    onSave();
                    node.resizeToFitText(FitSize.KeepHeight);
                changeItemCmd.execute();
                close();
            }
        }

        if ("node.cancel".equals(cmd)) {
            onCancel();
            close();
        }
    }
    
    
    static class FieldAttrs {
        final Color color;
        final Border border;
        final String tooltip;
        FieldAttrs(Color color, Border border, String tooltip) {
            this.color = color;
            this.border = border;
            this.tooltip = tooltip;
        }
    }
}
