package domain;

/**
 * Simbolo efimero: cada vez que la rueda lo selecciona disminuye su tamaño
 * hasta quedar como un punto.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class EphemeralSymbol extends Symbol {
    public static final int STEP = 5;
    public static final int POINT = 2;   // tamaño mínimo: "un punto"

    /**
     * Crea un simbolo efimero con el tamaño normal.
     * @param name identificador del simbolo.
     * @param color color del simbolo.
     */
    public EphemeralSymbol(String name, String color) {
        super(name, color);
    }

    @Override
    public String getType() {
        return "ephemeral";
    }

    /**
     * Reduce el tamaño STEP pixeles, sin bajar de POINT.
     */
    @Override
    public void selected() {
        setSize(Math.max(POINT, getSize() - STEP));
    }
}
