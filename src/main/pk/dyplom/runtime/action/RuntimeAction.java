package pk.dyplom.runtime.action;

import pk.dyplom.runtime.DiagramRunner;

import javax.swing.*;

public abstract class RuntimeAction extends AbstractAction {

    protected final DiagramRunner runner;
    
    public RuntimeAction(DiagramRunner runner) {
        this.runner = runner;
    }
}
