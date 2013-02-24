package pk.dyplom.codegen;

import com.mindfusion.diagramming.DiagramView;
import pk.dyplom.I18n;
import pk.dyplom.codegen.action.CloseFormAction;
import pk.dyplom.codegen.action.GenerateCodeAction;
import pk.dyplom.codegen.action.OpenFormAction;
import pk.dyplom.codegen.action.SaveCodeAction;
import pk.dyplom.event.*;
import pk.dyplom.ui.DiagramPanel;
import pk.dyplom.ui.GUI;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Komponent pozwalający wygenerować oraz zapisać do pliku kod w wybranym języku programowania na podstawie diagramu.
 */
public class CodeGeneration {
    
    public static final Logger logger = Logger.getLogger(CodeGeneration.class.getSimpleName());

    /** Dostępne gemeratory kodu */
    public static final Map<String, ICodeGenerator> GENERATORS = new LinkedHashMap<String, ICodeGenerator>();
    static {
        GENERATORS.put(I18n.t("generator.C"), new CCodeGenerator());
        GENERATORS.put(I18n.t("generator.JavaScript"), new JavaScriptCodeGenerator());
        GENERATORS.put(I18n.t("generator.WebPage"), new WebPageCodeGenerator());
    }

    /** Interfejs uzytkownika */
    public final GUI gui;
    /** Manager zdarzeń */
    public final EventManager eventBus;

    /** Otwarcie formularza */
    public OpenFormAction openFormAction;
    /** Zamknięcie formularza */
    public CloseFormAction closeFormAction;
    /** Wygenerowanie kodu */
    public GenerateCodeAction generateCodeAction;
    /** Zapisanie wygenerowanego kodu do pliku */
    public SaveCodeAction saveCodeAction;

    /** Formularz do generowania i zapisu kodu */
    public Form form;

    /**
     * Inicjuje komponent.
     *
     * @param gui Interfejs użytkownika
     * @param eventBus Instancja managera zdarzeń
     */
    public CodeGeneration(GUI gui, EventManager eventBus) {
        this.gui = gui;
        this.eventBus = eventBus;

        registerEventHandlers();
        initActions();
        initComponents();
    }

    /**
     * Zwraca aktywny widok diagramu.
     *
     * @return Aktywny widok diagramu
     */
    public DiagramView getCurrentDiagramView() {
        DiagramPanel diagramPanel = (DiagramPanel) gui.diagramsPanel.getSelectedComponent();
        return diagramPanel.diagramView;
    }

    /**
     * Rejestruje obsługę globalnych zdarzeń aplikacji specyficznych dla tego komponentu.
     */
    private void registerEventHandlers() {
        eventBus.addHandler(DiagramAddedEvent.class, new EventHandler<DiagramAddedEvent>() {
            public void handle(DiagramAddedEvent event) {
                openFormAction.setEnabled(true);
            }
        });

        eventBus.addHandler(DiagramClosedEvent.class, new EventHandler<DiagramClosedEvent>() {
            public void handle(DiagramClosedEvent event) {
                if (gui.diagramsPanel.getSelectedIndex() < 0)
                    openFormAction.setEnabled(false);
            }
        });

        eventBus.addHandler(DiagramCodeGenerated.class, new EventHandler<DiagramCodeGenerated>() {
            public void handle(DiagramCodeGenerated event) {
                form.codeEditorPane.setText(event.code);
                form.codeEditorPane.setCaretPosition(0);
                saveCodeAction.setEnabled(true);
            }
        });
    }

    /**
     * Inicjuje akcje.
     */
    private void initActions() {
        openFormAction = new OpenFormAction(this);
        openFormAction.setEnabled(false);
        closeFormAction = new CloseFormAction(this);
        generateCodeAction = new GenerateCodeAction(this);
        saveCodeAction = new SaveCodeAction(this);
        saveCodeAction.setEnabled(false);
    }

    /**
     * Inicjuje komponenty.
     */
    private void initComponents() {
        gui.generateCodeBtn.setAction(openFormAction);

        form = new Form();
        form.generateCodeBtn.setAction(generateCodeAction);
        for (String name : GENERATORS.keySet())
            form.codeGeneratorSelect.addItem(name);
        generateCodeAction.setEnabled(form.codeGeneratorSelect.getItemCount() > 0);
        form.saveCodeBtn.setAction(saveCodeAction);
        form.closeBtn.setAction(closeFormAction);
        gui.generateCodeMenuItem.setAction(openFormAction);
    }
}
