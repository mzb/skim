package pk.dyplom.eval;

import pk.dyplom.eval.error.EvalError;
import pk.dyplom.eval.error.ReferenceError;
import pk.dyplom.eval.error.SyntaxError;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ewaluator kodu wyegenrowanego przez {@link Compiler}.
 */
public class EvalEngine {

    private static final Pattern EVALUATOR_EXCEPTION_REGEX = Pattern.compile("EvaluatorException:");
    private static final Pattern REFERENCE_ERROR_REGEX = Pattern.compile(
            "ReferenceError: \"(\\w+)\" is not defined");
    private static final Pattern TYPE_ERROR_UNDEF_FUNCTION_REGEX = Pattern.compile(
            "TypeError: Cannot find function (\\w+)");

    private ScriptEngineManager manager = new ScriptEngineManager();
    private ScriptEngine engine;

    public EvalEngine() {
        engine = manager.getEngineByName("js");
    }

    /**
     * Ewaluuje podany kod w danym środowisku (kontekście).
     *
     * @param code Kod
     * @param env Środowisko wykonania
     * @return Wynik ewaluacji
     * @throws EvalError W przpadku błędu
     */
    public Object eval(String code, Environment env) throws EvalError {
        try {
            return engine.eval(code, env);
        } catch (ScriptException e) {
            throwProperError(e);
        }
        return null;
    }

    /**
     * Zwraca konkretny typ wyjątku wyrzuconego podczas eqaluacji.
     *
     * @param exception Wyjątek
     * @throws EvalError
     */
    private void throwProperError(Throwable exception) throws EvalError {
        String msg = exception.getMessage();
        Matcher matcher;

        matcher = REFERENCE_ERROR_REGEX.matcher(msg);
        if (matcher.find()) {
            throw new ReferenceError(exception, matcher.group(1));
        }
        matcher = TYPE_ERROR_UNDEF_FUNCTION_REGEX.matcher(msg);
        if (matcher.find()) {
            throw new ReferenceError(exception, matcher.group(1));
        }

        matcher = EVALUATOR_EXCEPTION_REGEX.matcher(msg);
        if (matcher.find()) {
            throw new SyntaxError(exception);
        }

        throw new EvalError(exception);
    }
}
