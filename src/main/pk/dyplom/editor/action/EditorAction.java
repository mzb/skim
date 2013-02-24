package pk.dyplom.editor.action;

import pk.dyplom.editor.DiagramEditor;

import javax.swing.*;

/**
 * Bazowa klasa dla akcji edytora.
 */
public abstract class EditorAction extends AbstractAction {

    /** Instancja edytora */
    protected final DiagramEditor editor;

    /**
     * Inicjuj akcję.
     *
     * @param editor Instancja edytora
     */
    public EditorAction(DiagramEditor editor) {
        this.editor = editor;
    }
}
