package pk.dyplom.eval;

/**
 * Klasa reprezentująca zmienną lub parametr formalny funkcji.
 */
public class Variable {

    /** Nazwa */
    public final String name;
    /** Typ */
    public final Type type;

    /**
     * Inicjalizacja.
     *
     * @param name Nazwa
     * @param type Typ
     */
    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Porównanie z inną zmienną.
     * Dwie zmienne są równe wtw gdy mają tą samą nazwą oraz ten sam typ.
     *
     * @param obj Zmienna
     * @return `true` jeśli zmienne równe, `false` w przeciwnym wypadku.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable))
            return false;
        Variable other = (Variable) obj;
        return name.equals(other.name) && type.equals(other.type);
    }

    /**
     * Zwraca tekstową reprezentację zmiennej.
     *
     * @return *nazwa*:*typ*
     */
    @Override
    public String toString() {
        return String.format("%s:%s", name, type);
    }
}
