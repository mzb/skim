package pk.dyplom.eval;

import java.util.Arrays;

/**
 * Typ reprezentujący listę/tablicę - kontener elementów o jednakowym typie.
 */
public class Array extends Type {

    /** Typ pojedynczego elementu */
    public final Primitive elementType;
    /**
     * Ilość elementów na poszczególnych wymiarach, np
     *
     *      sizes = new Integer[]{ 2, 3 }
     *
     * oznacza tablicę dwuwymiarową o rozmiarze 2x3
     */
    public final Integer[] sizes;

    /**
     * Tworzy tablicę o elementach podanego typu i rozmiarach.
     * 
     * @param elementType Typ elementów
     * @param sizes wymiary
     * @return Instancja tablicy
     */
    public static Array of(Primitive elementType, Integer... sizes) {
        return new Array(elementType, sizes);
    }

    /**
     * Tworzy jednowymiarową tablicę o elementach podanego typu.
     * 
     * @param elementType Typ elementów
     * @return Instancja tablicy
     * @see Array#of(Primitive, Integer...) 
     */
    public static Array of(Primitive elementType) {
        return new Array(elementType, 1);
    }

    /**
     * Inicjalizacja.
     * **Do użytku wewnętrznego**.
     * 
     * @param elementType Typ elementów
     * @param sizes Rozmiary
     * @see Array#of(Primitive, Integer...)
     */
    protected Array(Primitive elementType, Integer... sizes) {
        super("array");
        this.elementType = elementType;
        this.sizes = sizes;
    }

    /**
     * Sprawdza czy dwa typy tablicowe są sobie równe - mają ten sam typ elementów oraz wymiary.
     *
     * @param obj Typ tablicowy
     * @return `true` jeśli równe, `false` w przeciwnym wypadku
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Array))
            return false;
        Array other = (Array) obj;
        return elementType.equals(other.elementType) &&
               Arrays.equals(sizes, other.sizes);
    }

    /**
     * Zwraca tekstową reprezentację.
     *
     * @return Tekstowa reprezentacja typu w formie *typ-elementów*[*rozmiar-0*][*rozmiar-1*]*...*
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(elementType);
        for (int i = 0; i < sizes.length; i++) {
            sb.append("[").append(sizes[i]).append("]");
        }
        return sb.toString();
    }
}
