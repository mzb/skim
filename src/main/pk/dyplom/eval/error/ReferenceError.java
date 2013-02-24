package pk.dyplom.eval.error;

/**
 * Błąd referencji, np odwołanie do niezadeklarowanej zmiennej.
 */
public class ReferenceError extends EvalError {

    public ReferenceError(Throwable cause, String... params) {
       super(cause, params);
    }
}
