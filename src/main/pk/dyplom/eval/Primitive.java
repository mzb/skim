package pk.dyplom.eval;

/**
 * Typ reprezentujący pojedynczą wartość (liczba, napis itd).
 */
public class Primitive extends Type {

    /** Liczba całkowita */
    public static final Primitive INT = new Primitive("int");
    /** Liczba zmiennoprzecinkowa */
    public static final Primitive REAL = new Primitive("real");
    /** Zmianna boole'owska */
    public static final Primitive BOOL = new Primitive("bool");
    /** Łańcuch znakowy */
    public static final Primitive STRING = new Primitive("string");
    /** Nieznany typ */
    public static final Primitive UNKNOWN = new Primitive("unknown");

    /**
     * Zwraca instancję typu na podstawie nazwy.
     *
     * @param name Nazwa
     * @return Instancja typu
     */
    public static Primitive fromString(String name) {
        if (INT.name.equals(name))
           return INT;
        if (REAL.name.equals(name))
           return REAL;
        if (BOOL.name.equals(name))
           return BOOL;
        if (STRING.name.equals(name))
           return STRING;
        return UNKNOWN;
    }

    /**
     * {@inheritDoc}
     */
    protected Primitive(String name) {
       super(name);
    }

    /**
     * Porównuje dwa typy ze sobą.
     * Typy są uważane za równe wtw gdy ich *referencje* są takie same (są tym samym obiektem).
     * Każdy z typów jest reprezentowany w systemie przez dokładnie jedną instancję.
     *
     * @param obj Typ
     * @return `true` jeśli typy są takie same, `false` w przeciwnym wypadku
     */
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
