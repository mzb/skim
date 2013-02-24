package pk.dyplom.eval;

import sun.org.mozilla.javascript.internal.NativeArray;

import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.List;

/**
 * Środowisko wykonawcze przechowujące nazwy i wartości zmiennych.
 * Umożliwia śledzenie zmiennych w czasie wykonania.
 */
public class Environment extends SimpleBindings {

    /**
     * Przypisz wartość do nowej zmiennej.
     * 
     * @param name Nazwa zmiennej
     * @param value Wartość
     */
    public void set(String name, Object value) {
        put(name, value);
    }

    /**
     * Pobierz waortść przypisaną do danej nazwy.
     * 
     * @param name Nazwa zmiennej
     * @return Wartość zmiennej skonwertowana do odpowiedniego typu.
     */
    public Object get(String name) {
        return convert(super.get(name));
    }

    /**
     * Konwertuj podaną wartość na jak najbardziej konkrenty typ:
     * 
     * - liczby przechowywane są wewnętrznie jako {@link Double}, jeśli jednak
     * nie posiadają rozwinięcia dzisiętnego konwertowane są do {@link Integer}.
     * - tablice (przechowywane jako {@link NativeArray}) konwertowane są do {@link List}
     * 
     * @param value Wartość
     * @return
     */
    private Object convert(Object value) {
        if (value instanceof Double) {
            double d = (Double) value;
            if (d % 1 == 0) {
                return new Integer((int) d);
            }
        }

        if (value instanceof NativeArray) {
            NativeArray ary = (NativeArray) value;
            List list = new ArrayList();
            for (int i = 0; i < ary.getLength(); i++) {
                list.add(convert(ary.get(i, null)));
            }
            return list;
        }

        return value;
    }
}
