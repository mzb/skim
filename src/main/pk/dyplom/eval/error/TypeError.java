package pk.dyplom.eval.error;

/**
 * Błąd dot. typów.
 */
public class TypeError extends EvalError {

    public TypeError(Throwable cause, String... params) {
       super(cause, params);
    }
}
