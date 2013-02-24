package pk.dyplom.runtime;

import com.mindfusion.diagramming.Brush;
import com.mindfusion.diagramming.DiagramNode;
import com.mindfusion.diagramming.SolidBrush;
import pk.dyplom.Config;
import pk.dyplom.I18n;
import pk.dyplom.eval.error.EvalError;
import pk.dyplom.eval.error.ReferenceError;
import pk.dyplom.eval.error.SyntaxError;
import pk.dyplom.eval.error.TypeError;
import pk.dyplom.event.DiagramRuntimeErrorEvent;

import javax.swing.*;
import java.awt.*;

public class ErrorHandler implements Thread.UncaughtExceptionHandler {

    private static final Brush ERROR_NODE_BRUSH = new SolidBrush(Config.DEFAULT.getColor("runtime.errorNodeColor", Color.RED));

    final DiagramRunner runner;

    public ErrorHandler(DiagramRunner runner) {
        this.runner = runner;
    }

    @Override
    public void uncaughtException(Thread t, Throwable exception) {
        runner.logger.warning(exception.toString());
        if (exception instanceof ThreadDeath)
            return;

        runner.stop();

        if (exception instanceof RuntimeException)
            exception = exception.getCause();
        String errorMessage = getErrorMessage(exception);

        DiagramNode currentNode = runner.getCurrentDiagramNode();
        if (currentNode != null) {
            currentNode.setBrush(ERROR_NODE_BRUSH);
            currentNode.setToolTip(errorMessage);
        }

        JOptionPane.showMessageDialog(null, errorMessage, I18n.t("error.title"), JOptionPane.ERROR_MESSAGE);

        runner.eventBus.triggerEvent(new DiagramRuntimeErrorEvent(errorMessage, exception));
    }
    
    private String getErrorMessage(Throwable error) {
        if (error instanceof EvalError) {
            EvalError evalError = (EvalError) error;
            if (error instanceof SyntaxError)
                return I18n.t("error.SyntaxError");
            if (error instanceof ReferenceError)
                return I18n.t("error.ReferenceError", evalError.params[0]);
            if (error instanceof TypeError)
                return I18n.t("error.TypeError", evalError.params[0]);
        }

        return I18n.t(error.getMessage());
    }
}
