package pk.dyplom.eval;

import pk.dyplom.eval.error.EvalError;
import pk.dyplom.eval.error.SyntaxError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Komponent sprawdzający poprawność składniową kodu użytego w blokach diagramu.
 * Wykorzystywany w formularzach edycyjnych poszczególnych bloków.
 */
public class SyntaxValidator {

    /** Ewaluator */
    private EvalEngine evalEngine = new EvalEngine();
    /** Kompilator */
    private Compiler compiler = new Compiler();

    /**
     * Sprawdź, czy podana nazwa jest poprawną nazwą funkcji.
     *
     * @param name Potencjalna nazwa funkcji.
     * @return {@code true} jeśli podana nazwa może być poprawną nazwą funkcji lub {@code false} w przeciwnym wypadku.
     */
    public boolean isValidFunctionName(String name) {
        if (name.trim().isEmpty())
            return false;

        return isValid(compiler.functionSyntax(name, "", ""));
    }

    /**
     * Sprawdź, czy podana nazwa jest poprawną nazwą zmiennej.
     *
     * @param name Potencjalna nazwa zmiennej.
     * @return {@code true} jeśli podana nazwa może być poprawną nazwą zmiennej lub {@code false} w przeciwnym wypadku.
     */
    public boolean isValidVariableName(String name) {
        if (name.trim().isEmpty())
            return false;
        
        return isValid(compiler.varSyntax(name));
    }

    /**
     * Sprawdź, czy podana lista jest poprawną listą parametrów funkcji.
     *
     * @param params Lista parametrów
     * @return {@code true} jeśli podana lista jest poprawną listą param. funkcji lub {@code false} w przeciwnym wypadku.
     *
     * @code {
     *     validator.isValidParamList("a"); // => true
     *     validator.isValidParamList(""); // => true
     *     validator.isValidParamList("1, 2"); // => false
     *     validator.isValidParamList(","); // => false
     * }
     */
    public boolean isValidParamList(String params) {
        return isValid(compiler.functionSyntax("", params, "")) && 
               Compiler.PARAMS_LIST_REGEX.matcher(params).matches();
    }

    /**
     * Sprawdź, czy podane wyrażenie (np. warunkowe) jest poprawne składniowo.
     *
     * @param expr Wyrażenie
     * @return {@code true} jeśli podane wyrażenie jest poprawne lub {@code false} w przeciwnym wypadku.
     *
     * @code {
     *     validator.isValidExpression("a == 5"); // => true
     *     validator.isValidExpression(""); // false
     * }
     */
    public boolean isValidExpression(String expr) {
        return isValid(compiler.expressionSyntax(expr));
    }

    /**
     * Sprawdź, czy podane wyrażenie może być użyte jako {@code return} w bloku {@link pk.dyplom.diagram.EndNode}.
     *
     * @param expr Wyrażenie
     * @return {@code true} jeśli podane wyrażenie może być użyter lub {@code false} w przeciwnym wypadku.
     */
    public boolean isValidReturnExpression(String expr) {
        if (expr.isEmpty())
            return true;
        return isValid(compiler.returnSyntax(expr)) && 
               isValid(compiler.varSyntax(expr));
    }

    /**
     * Sprawdź, czy podany kod jest poprawny składniowo wykonując ewaluację.
     *
     * @param code Kod
     * @return {@code true} jeśli podany kod jest poprawny składniowo lub {@code false} w przeciwnym wypadku.
     */
    public boolean isValid(String code) {
        try {
            evalEngine.eval(compiler.ignoreTypeDeclarations(code), new Environment());
            return true;
        } catch (EvalError e) {
            return e instanceof SyntaxError ? false : true;
        }
    }

    /**
     * Sprawdź, czy podany kod zawiera poprawne składniowo deklaracje zmiennych.
     *
     * @param code Kod
     * @return {@code true} jeśli podany kod zawiera poprawne deklaracje zmiennych lub {@code false} w przeciwnym wypadku.
     */
    public boolean containsValidVarDeclarations(String code) {
        final Pattern varRegex = Pattern.compile(
                "var\\s+" + "(" + Compiler.NAME_REGEX + ")(" + Compiler.NAME_TYPE_SEPARATOR_REGEX + ")?" +
                        "(" + Compiler.NAME_REGEX + ")?((" + Compiler.ARRAY_TYPE_REGEX + ")+)?" + "(\\s*;\\s*)?"
        );
        Matcher matcher = varRegex.matcher(code);
        while (matcher.find()) {
            String typeSeparator = matcher.group(2);
            if (typeSeparator == null || typeSeparator.isEmpty())
                return false;
            String type = matcher.group(3);
            if (type == null || !type.matches(Compiler.TYPE_REGEX.toString()))
                return false;
            if (!matcher.group(0).matches(".*;\\s*$"))
                return false;
        }
        return true;
    }
}
