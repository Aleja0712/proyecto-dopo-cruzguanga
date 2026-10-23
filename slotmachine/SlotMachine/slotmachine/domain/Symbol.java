package domain;

/**
 * La clase Symbol representa cada uno de los símbolos que pueden aparecer dentro de una rueda.
 * Solo guarda el estado lógico del símbolo (nombre, color, tamaño, si está oculto);
 * la forma de dibujarlo la decide la capa de presentación.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class Symbol {
    public static final int DEFAULT_SIZE = 30;

    private final String name;
    private final String color;
    private int size;

    /**
     * Constructor para inicializar los datos base del simbolo.
     * @param name identificador del simbolo.
     * @param color color del simbolo (identifica al simbolo dentro de la rueda).
     */
    public Symbol(String name, String color) {
        this.name = name;
        this.color = color;
        this.size = DEFAULT_SIZE;
    }

    /**
     * Fabrica de simbolos segun su tipo.
     * @param type "normal", "ephemeral" o "shy".
     * @param color color del simbolo.
     * @return el simbolo creado, o null si el tipo no existe.
     */
    public static Symbol create(String type, String color) {
        if (type == null) {
            return null;
        }
        switch (type.toLowerCase()) {
            case "normal":    return new Symbol(color, color);
            case "ephemeral": return new EphemeralSymbol(color, color);
            case "shy":       return new ShySymbol(color, color);
            default:          return null;
        }
    }

    /**
     * @return nombre del simbolo.
     */
    public String getName() {
        return name;
    }

    /**
     * @return color del simbolo.
     */
    public String getColor() {
        return color;
    }

    /**
     * @return tamaño actual del simbolo en pixeles.
     */
    public int getSize() {
        return size;
    }

    /**
     * Cambia el tamaño del simbolo.
     * @param size nuevo tamaño (>= 0).
     */
    protected void setSize(int size) {
        this.size = Math.max(0, size);
    }

    /**
     * @return tipo del simbolo; la presentacion lo usa para distinguirlo visualmente.
     */
    public String getType() {
        return "normal";
    }

    /**
     * Se invoca cada vez que la rueda deja este simbolo a la vista
     * (al girar o al ubicarlo). El simbolo normal no cambia.
     */
    public void selected() { }

    /**
     * @return true si el simbolo esta oculto. El simbolo normal nunca se oculta.
     */
    public boolean isConcealed() {
        return false;
    }
}
