package pk.dyplom.editor;

import com.mindfusion.diagramming.*;
import pk.dyplom.I18n;
import pk.dyplom.diagram.*;
import pk.dyplom.editor.action.*;
import pk.dyplom.event.*;
import pk.dyplom.ui.DiagramPanel;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Edytor diagramów.
 * Każdy diagram edytowany jest w odrębnej zakładce,
 * w danym momencie tylko jedna zakładka jest aktywna.
 */
public class DiagramEditor extends DiagramAdapter implements ChangeListener {

    public final Logger logger = Logger.getLogger("DiagramEditor");

    /** Interfejs użytkownika */
    public final GUI gui;
    /** Manager zdarzeń */
    public final EventManager eventBus;

    /** Akcja reprezentująca dodanie nowego diagramu */
    public NewDiagramAction newDiagramAction;
    /** Akcja reprezentująca zamknięcie aktywnego diagramu */
    public CloseDiagramAction closeDiagramAction;
    /** Akcja reprezentująca zapisanie diagramu */
    public SaveDiagramAction saveDiagramAction;
    /** Akcja reprezentująca wczytanie diagramu z pliku */
    public OpenDiagramAction openDiagramAction;
    /** Akcja reprezentująca wyeksportowanie diagramu */
    public ExportDiagramAction exportDiagramAction;

    /** Cofnij */
    public UndoAction undoAction;
    /** Wykonaj ponownie */
    public RedoAction redoAction;

    /** Zbiór akcji reprezentujących dodanie bloku do diagramu, po jednej dla każdego typu bloku */
    public Map<String, AddNodeAction> addNodeActions = new LinkedHashMap<String, AddNodeAction>();
    /** Edycja bloku */
    public EditNodeAction editNodeAction;
    /** Usunięcie bloku */
    public DeleteNodeAction deleteNodeAction;

//    public CopyToClipboardAction copyToClipboardAction;
//    public PasteFromClipboardAction pasteFromClipboardAction;
//    public CutToClipboardAction cutToClipboardAction;

    /**
     * Inicjuj komponent.
     *
     * @param gui Interfejs użytkownika aplikacji
     * @param eventBus Manager zdarzeń aplikacji
     */
    public DiagramEditor(GUI gui, EventManager eventBus) {
        this.gui = gui;
        this.eventBus = eventBus;

        registerDiagramElements();
        registerEventHandlers();
        initActions();
        initComponents();
    }

    /**
     * Inicjuj poszczególne komponenty edytora.
     */
    private void initComponents() {
        for (Map.Entry<String, AddNodeAction> addNode : addNodeActions.entrySet()) {
            JButton btn = new JButton(addNode.getValue());
            btn.setVerticalTextPosition(AbstractButton.BOTTOM);
            btn.setHorizontalTextPosition(AbstractButton.CENTER);
            btn.setMaximumSize(new Dimension(200, 80));
            gui.nodeButtons.add(btn);
            gui.addNodeMenu.add(new JMenuItem(addNode.getValue()));
        }

        gui.newDiagramMenuItem.setAction(newDiagramAction);
        gui.openDiagramMenuItem.setAction(openDiagramAction);
        gui.saveDiagramMenuItem.setAction(saveDiagramAction);
        gui.exportDiagramMenuItem.setAction(exportDiagramAction);
        gui.closeDiagramMenuItem.setAction(closeDiagramAction);

        gui.undoMenuItem.setAction(undoAction);
        gui.redoMenuItem.setAction(redoAction);
        gui.addNodeMenu.setText(I18n.t("action.addNode"));
        gui.editNodeMenuItem.setAction(editNodeAction);
        gui.deleteNodeMenuItem.setAction(deleteNodeAction);
        //gui.copyToClipboardMenuItem.setAction(copyToClipboardAction);
        //gui.pasteFromClipboardMenuItem.setAction(pasteFromClipboardAction);
        //gui.cutToClipboardMenuItem.setAction(cutToClipboardAction);

        gui.newDiagramBtn.setAction(newDiagramAction);
        gui.closeDiagramBtn.setAction(closeDiagramAction);
        gui.saveDiagramBtn.setAction(saveDiagramAction);
        gui.openDiagramBtn.setAction(openDiagramAction);

        gui.diagramsPanel.addChangeListener(this);
    }

    /**
     * Inicjuj poszczególne akcje edytora.
     */
    private void initActions() {
        newDiagramAction = new NewDiagramAction(this);
        openDiagramAction = new OpenDiagramAction(this);
        closeDiagramAction = new CloseDiagramAction(this);
        saveDiagramAction = new SaveDiagramAction(this);
        exportDiagramAction = new ExportDiagramAction(this);

        addNodeActions.put("start", new AddNodeAction(I18n.t("diagram.node.StartNode"), StartNode.class, this));
        addNodeActions.put("action", new AddNodeAction(I18n.t("diagram.node.ProcessingNode"), ProcessingNode.class, this));
        addNodeActions.put("conditional", new AddNodeAction(I18n.t("diagram.node.ConditionalNode"), ConditionalNode.class, this));
        addNodeActions.put("input", new AddNodeAction(I18n.t("diagram.node.InputNode"), InputNode.class, this));
        addNodeActions.put("output", new AddNodeAction(I18n.t("diagram.node.OutputNode"), OutputNode.class, this));
        addNodeActions.put("end", new AddNodeAction(I18n.t("diagram.node.EndNode"), EndNode.class, this));

        editNodeAction = new EditNodeAction(null, this);
        deleteNodeAction = new DeleteNodeAction(null, this);

        if (!hasOpenedDiagram()) {
            for (AddNodeAction action : addNodeActions.values()) {
                action.setEnabled(false);
            }
            closeDiagramAction.setEnabled(false);
            saveDiagramAction.setEnabled(false);
            gui.addNodeMenu.setEnabled(false);
            exportDiagramAction.setEnabled(false);
        }
        
        undoAction = new UndoAction(this);
        redoAction = new RedoAction(this);

        //copyToClipboardAction = new CopyToClipboardAction(this);
        //pasteFromClipboardAction = new PasteFromClipboardAction(this);
        //cutToClipboardAction = new CutToClipboardAction(this);
    }

    /**
     * Zarejestruj obsługę wybranych zdarzeń aplikacji interesujących z punktu widzenia edytora
     * (dodanie diagramu, zapisanie diagramu itd)
     */
    private void registerEventHandlers() {
        eventBus.addHandler(DiagramAddedEvent.class, new EventHandler<DiagramAddedEvent>() {
            public void handle(DiagramAddedEvent event) {
                gui.addNodeMenu.setEnabled(true);
                exportDiagramAction.setEnabled(true);
                for (AddNodeAction action : addNodeActions.values()) {
                    action.setEnabled(true);
                }
                closeDiagramAction.setEnabled(true);
                saveDiagramAction.setEnabled(true);
                undoAction.setEnabled(true);
                redoAction.setEnabled(true);
            }
        });

        eventBus.addHandler(DiagramClosedEvent.class, new EventHandler<DiagramClosedEvent>() {
            public void handle(DiagramClosedEvent event) {
                if (!hasOpenedDiagram()) {
                    gui.addNodeMenu.setEnabled(false);
                    exportDiagramAction.setEnabled(false);
                    for (AddNodeAction action : addNodeActions.values()) {
                        action.setEnabled(false);
                    }
                    closeDiagramAction.setEnabled(false);
                    saveDiagramAction.setEnabled(false);
                    editNodeAction.setNode(null);
                    deleteNodeAction.setNode(null);
                    undoAction.setEnabled(false);
                    redoAction.setEnabled(false);
                }
            }
        });
        
        eventBus.addHandler(DiagramSavedEvent.class, new EventHandler<DiagramSavedEvent>() {
            public void handle(DiagramSavedEvent event) {
                gui.diagramsPanel.setTitleAt(gui.diagramsPanel.getSelectedIndex(), event.name);
                getCurrentDiagramView().getDiagram().setToolTip(event.name);
            }
        });
    }

    /**
     * Zarejestruj poszczególne typy bloków (wymagane przez biliotekę JDiagram).
     */
    private void registerDiagramElements() {
        // http://mindfusion.eu/Forum/YaBB.pl?num=1320386783
        final int version = 100;

        Diagram.registerItemClass(StartNode.class, StartNode.class.getSimpleName(), version);
        Diagram.registerItemClass(EndNode.class, EndNode.class.getSimpleName(), version);
        Diagram.registerItemClass(ProcessingNode.class, ProcessingNode.class.getSimpleName(), version);
        Diagram.registerItemClass(ConditionalNode.class, ConditionalNode.class.getSimpleName(), version);
        Diagram.registerItemClass(OutputNode.class, OutputNode.class.getSimpleName(), version);
        Diagram.registerItemClass(InputNode.class, InputNode.class.getSimpleName(), version);
        Diagram.registerItemClass(DiagramLink.class, DiagramLink.class.getSimpleName(), version);
    }

    /**
     * Zwróć aktywny widok diagramu (zawartość aktualnie widocznej zakładki).
     * @return Aktywny widok diagramu
     */
    public DiagramView getCurrentDiagramView() {
        DiagramPanel diagramPanel = (DiagramPanel) gui.diagramsPanel.getSelectedComponent();
        return diagramPanel.diagramView;
    }


    /**
     * Zablokuj możliwość edycji podpisów na połączeniach między blokami.
     *
     * @param e Zdarzenie
     */
    @Override
    public void linkTextEditing(LinkValidationEvent e) {
        // Nie mozna edytowac tekstow na linkach
        e.setCancel(true);
    }

    /**
     * Edycja połączenia między blokami - wykonaj akcję zależną od typu bloku.
     * @see BaseNode#onLinkCreating
     *
     * @param e Zdarzenie
     */
    @Override
    public void linkCreating(LinkValidationEvent e) {
        BaseNode sourceNode = (BaseNode) e.getOrigin();
        sourceNode.onLinkCreating(e);
    }

    /**
     * Otwórz formularz edycji danego bloku przy podwójnym klknięciu na nim.
     *
     * @param e Zdarzenie
     */
    @Override
    public void nodeDoubleClicked(NodeEvent e) {
        if (e.getMouseButton() != MouseEvent.BUTTON1) {
            // Tylko lewy przycisk myszy
            return;
        }
        editNodeAction.setNode(e.getNode());
        editNodeAction.actionPerformed(null);
    }

    /**
     * Pokaż menu kontekstowe z akcjami dla danego bloku (Edytuj, Usuń) przy kliknięciu na nim prawym przycyskiem myszy.
     *
     * @param e Zdarzenie
     */
    @Override
    public void nodeClicked(NodeEvent e) {
        if (e.getNode().getLocked())
            return;
        if (e.getMouseButton() != MouseEvent.BUTTON3)  {
            // Tylko prawy przycisk myszy
            return;
        }
        editNodeAction.setNode(e.getNode());
        deleteNodeAction.setNode(e.getNode());
        JMenuItem edit = new JMenuItem(editNodeAction);
        JMenuItem delete = new JMenuItem(deleteNodeAction);
        showContextMenu(edit, delete);
    }

    /**
     * Aktywuj akcje dot. danego bloku, jeśli został zaznaczony.
     *
     * @param e Zdarzenie
     */
    @Override
    public void nodeSelected(NodeEvent e) {
        editNodeAction.setNode(e.getNode());
        deleteNodeAction.setNode(e.getNode());
    }

    /**
     * Deaktywuj akcje dot. danego bloku, jeśli został odzaczony.
     *
     * @param e Zdarzenie
     */
    @Override
    public void nodeDeselected(NodeEvent e) {
        editNodeAction.setNode(null);
        deleteNodeAction.setNode(null);
    }

    /**
     * Cofnięto ostatnią akcję.
     *
     * @param undoEvent Zdarzenie
     */
    @Override
    public void actionUndone(UndoEvent undoEvent) {
        boolean moreUndos = getCurrentDiagramView().getDiagram().getUndoManager().getHistory().getNextUndo() != null;
        undoAction.setEnabled(moreUndos);
        redoAction.setEnabled(true);
    }

    /**
     * Powtórzono ostatnią akcję.
     *
     * @param undoEvent Zdarzenie
     */
    @Override
    public void actionRedone(UndoEvent undoEvent) {
        boolean moreRedos = getCurrentDiagramView().getDiagram().getUndoManager().getHistory().getNextRedo() != null;
        redoAction.setEnabled(moreRedos);
    }

    /**
     * Ostatnia akcja została zarejestrowana na potrzeby "Cofnij/Wykonaj ponownie".
     *
     * @param undoEvent Zdarzenie
     */
    @Override
    public void actionRecorded(UndoEvent undoEvent) {
        undoAction.setEnabled(true);
    }

    /**
     * Obsłuż zmianę stanu interfejsu użytkownika.
     *
     * @param e Zdarzenie
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (gui.diagramsPanel == e.getSource() && hasOpenedDiagram())
            diagramPanelTabChanged(e);
    }

    /**
     * Obsłuż zmianę aktywnej zakładki - aktualizuj zaznaczenie bloków.
     *
     * @param e Zdarzenie
     */
    private void diagramPanelTabChanged(ChangeEvent e) {
        Selection currSelection = getCurrentDiagramView().getDiagram().getSelection();
        DiagramNode selectedNode = currSelection.getNodes().isEmpty() ? null : currSelection.getNodes().get(0);
        nodeSelected(new NodeEvent(e.getSource(), selectedNode));
    }

    /**
     * Wyświetl menu kontekstowe z podanymi elementami.
     * Pozycja menu jest ustawiana zgodnie z aktualną pozycją kursora.
     *
     * @param items Lista elementów menu
     */
    private void showContextMenu(JMenuItem... items) {
        JPopupMenu menu = new JPopupMenu();
        for (JMenuItem i : items)
            menu.add(i);

        menu.show(gui, (int) gui.getMousePosition().getX(), (int) gui.getMousePosition().getY());
    }

    /**
     * Dodaj nową zakładkę z diagramem (nowym lub wczytanym z pliku).
     * Wywołuje zdarzenie {@link DiagramAddedEvent}.
     * @see NewDiagramAction
     * @see OpenDiagramAction
     *
     * @param diagram Diagram
     * @param name Nazwa zkładki
     */
    public void addDiagram(Diagram diagram, String name) {
        diagram.setToolTip(name);
        diagram.getUndoManager().setUndoEnabled(true);
        diagram.setAllowSelfLoops(false);
        diagram.setAllowUnanchoredLinks(false);
        diagram.setAlignToGrid(true);
        diagram.setAutoResize(AutoResize.AllDirections);
        diagram.setLinkStyle(LinkStyle.Bezier);
        diagram.setSnapToAnchor(SnapToAnchor.OnCreateOrModify);
        diagram.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 3));
        diagram.setEnableStyledText(true);

        diagram.addDiagramListener(this);

        DiagramPanel diagramPanel = new DiagramPanel();
        DiagramView diagramView = diagramPanel.diagramView;

        diagramView.setDiagram(diagram);
        
        diagramView.setBehavior(Behavior.DrawLinks);
        diagramView.setAllowInplaceEdit(false);

        gui.diagramsPanel.addTab(name, diagramPanel);

        eventBus.triggerEvent(new DiagramAddedEvent(diagramPanel));
    }

    /**
     * Sprawdź, czy jakiekolwiek zakładki z diagramami są otwarte.
     *
     * @return {@code true} jeśli otwarte jakieś zakładki z diagramami lub {@code false} w przyciwnym wypadku
     */
    private boolean hasOpenedDiagram() {
        return gui.diagramsPanel.getSelectedIndex() >= 0;
    }
}
