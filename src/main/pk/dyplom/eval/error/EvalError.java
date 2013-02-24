package pk.dyplom.eval.error;

/**
 * Ogólny wyjątek ewaluacji.
 */
public class EvalError extends Exception {

    /** Parametry do przekazania przy raportowaniu */
    public final String[] params;

    /**
     * Inicjalizacja.
     *
     * @param cause Powód błędu
     * @param params Parametry
     */
    public EvalError(Throwable cause, String... params) {
        super(cause);
        this.params = params;
    }
}
