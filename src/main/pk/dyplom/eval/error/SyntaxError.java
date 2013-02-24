package pk.dyplom.eval.error;

/**
 * Błąd składniowy.
 */
public class SyntaxError extends EvalError {

    public SyntaxError(Throwable cause, String... params) {
       super(cause, params);
    }
}
