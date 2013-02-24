package pk.dyplom.eval;

/**
 * Bazowa klasa dla typów.
 */
abstract public class Type {

    /** Nazwa używana w kodzie */
    public final String name;

    /**
     * Inicjalizacja.
     *
      * @param name Nazwa
     */
    protected Type(String name) {
        this.name = name;
    }

    /**
     * Zwraca tekstową reprezentację typu - jego nazwę.
     *
     * @return Nazwa typu
     */
    public String toString() {
        return name;
    }
}
