package pk.dyplom.event;

public class DiagramRuntimeErrorEvent {

    public final String errorMessage;
    public final Throwable exception;
   
    public DiagramRuntimeErrorEvent(String errorMessage, Throwable exception) {
        this.errorMessage = errorMessage;
        this.exception = exception;
    }
}
