package pk.dyplom.editor.action;

import pk.dyplom.diagram.*;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.event.DiagramNodeAdded;
import pk.dyplom.ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa reprezentująca dodanie nowego bloku do diagramu.
 */
public class AddNodeAction extends EditorAction {

    /** Ikony dla poszczególnych typów bloków (wyświetlane na elementach, do których podpięta jest akcja) */
    private static final Map<Class, String> ICONS = new HashMap<Class, String>();
    static {
        ICONS.put(InputNode.class, "input.png");
        ICONS.put(StartNode.class, "start.png");
        ICONS.put(ProcessingNode.class, "processing.png");
        ICONS.put(ConditionalNode.class, "conditional.png");
        ICONS.put(OutputNode.class, "output.png");
        ICONS.put(EndNode.class, "end.png");
    }

    /** Typ bloku, który jest dodawany przez tą akcję */
    final Class<? extends BaseNode> nodeClass;

    /**
     * Inicjalizuj akcję.
     *
     * @param name Nazwa (wyświetlana na elementach, do których akcja jest podpięta)
     * @param nodeClass Typ bloku
     * @param editor Instancja edytora
     */
    public AddNodeAction(String name, Class<? extends BaseNode> nodeClass, DiagramEditor editor) {
        super(editor);

        this.nodeClass = nodeClass;

        putValue(Action.NAME, name);
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(
                GUI.class.getResource("icon/diagram/" + ICONS.get(nodeClass))));
    }

    /**
     * Dodaj nową instancję bloku do aktywnego diagramu.
     * Początkowa pozycja bloku jest ustawiana na lewy górny róg aktualnego widoku (z uwzględnieniem przewinięcia).
     * Wywołuje zdarzenie {@link DiagramNodeAdded}.
     *
     * @param event Zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        BaseNode node = null;
        try {
            node = nodeClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        float x = editor.getCurrentDiagramView().getScrollX();
        float y = editor.getCurrentDiagramView().getScrollY();
        node.moveTo(x, y);
        editor.getCurrentDiagramView().getDiagram().add(node);

        editor.eventBus.triggerEvent(new DiagramNodeAdded(node));
    }
}
