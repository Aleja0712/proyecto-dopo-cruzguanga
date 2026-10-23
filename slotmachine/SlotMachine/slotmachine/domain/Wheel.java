package domain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * La clase Wheel representa cada una de las ruedas que forman parte de
 * la maquina tragamonedas. Contiene un conjunto circular de simbolos de
 * colores diferentes y la posicion del simbolo que esta a la vista.
 *
 * Invariante: si la rueda tiene simbolos, 0 <= position < symbols.size();
 *             si esta vacia, position == -1. Una rueda fija nunca se mueve.
 *
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class Wheel {
    private final List<Symbol> symbols;
    private int position;
    private boolean locked;
    private final Random randomGenerator;

    /**
     * Constructor para inicializar una rueda vacia y libre.
     */
    public Wheel() {
        this.symbols = new ArrayList<Symbol>();
        this.position = -1;
        this.locked = false;
        this.randomGenerator = new Random();
    }

    /**
     * Fabrica de ruedas segun su tipo.
     * @param type "normal", "lefty", "rebel" o "reverse".
     * @return la rueda creada, o null si el tipo no existe.
     */
    public static Wheel create(String type) {
        if (type == null) {
            return null;
        }
        switch (type.toLowerCase()) {
            case "normal":  return new Wheel();
            case "lefty":   return new LeftyWheel();
            case "rebel":   return new RebelWheel();
            case "reverse": return new ReverseWheel();
            default:        return null;
        }
    }

    /**
     * @return tipo de la rueda; la presentacion lo usa para distinguirla visualmente.
     */
    public String getType() {
        return "normal";
    }

    /**
     * Añade un simbolo al final de la rueda. Si la rueda estaba vacia,
     * el nuevo simbolo queda a la vista; si no, la vista no cambia.
     * @param symbol simbolo a agregar.
     * @return false si ya existe un simbolo de ese color.
     */
    public boolean addSymbol(Symbol symbol) {
        if (symbol == null || contains(symbol.getColor())) {
            return false;
        }
        symbols.add(symbol);
        if (position < 0) {
            position = 0;
        }
        return true;
    }

    /**
     * Elimina el simbolo del color indicado.
     * Si era el simbolo a la vista, pasa a la vista el primero de la rueda.
     * @param color color del simbolo a eliminar.
     * @return true si se elimino.
     */
    public boolean delSymbol(String color) {
        int index = indexOf(color);
        if (index < 0) {
            return false;
        }
        symbols.remove(index);
        if (symbols.isEmpty()) {
            position = -1;
        } else if (index == position) {
            position = 0;
        } else if (index < position) {
            position--;
        }
        return true;
    }

    /**
     * Gira la rueda una cantidad aleatoria de pasos.
     */
    public void spin() {
        if (!symbols.isEmpty()) {
            spin(1 + randomGenerator.nextInt(symbols.size()));
        }
    }

    /**
     * Gira la rueda steps pasos de forma circular:
     * nuevaPosicion = (posicionActual + pasos) mod totalSimbolos.
     * Pasos positivos avanzan y negativos retroceden.
     * Si el simbolo a la vista cambia, se le avisa que fue seleccionado.
     * @param steps cantidad de pasos a girar.
     */
    public void spin(int steps) {
        if (symbols.isEmpty() || locked) {
            return;
        }
        int total = symbols.size();
        int real = effectiveSteps(steps);
        int newPosition = ((position + real % total) % total + total) % total;
        moveTo(newPosition);
    }

    /**
     * Pasos que realmente avanza la rueda cuando se le piden steps.
     * La rueda normal avanza exactamente lo pedido.
     * @param steps pasos pedidos.
     * @return pasos reales (positivos avanzan, negativos retroceden).
     */
    public int effectiveSteps(int steps) {
        return steps;
    }

    /**
     * Ubica la rueda sobre el simbolo del color indicado, sin agregar simbolos.
     * @param color color del simbolo que debe quedar a la vista.
     * @return true si el simbolo existe, false si no.
     */
    public boolean place(String color) {
        int index = indexOf(color);
        if (index < 0 || locked) {
            return false;
        }
        moveTo(index);
        return true;
    }

    /**
     * Se invoca despues de que la rueda gira, con la rueda de su izquierda
     * (null si no tiene). Las ruedas normales no hacen nada.
     * @param left rueda inmediatamente a la izquierda.
     */
    public void copyLeft(Wheel left) { }

    /**
     * Indica si la rueda contiene un símbolo del color dado.
     * @param color color buscado.
     * @return true si existe, false en caso contrario.
     */
    public boolean contains(String color) {
        return indexOf(color) >= 0;
    }

    /**
     * @return copia de solo lectura de los simbolos de la rueda.
     */
    public List<Symbol> getSymbols() {
        return Collections.unmodifiableList(symbols);
    }

    /**
     * @return el simbolo a la vista, o null si la rueda esta vacia.
     */
    public Symbol getCurrentSymbol() {
        return (position < 0) ? null : symbols.get(position);
    }

    /**
     * @return indice del simbolo a la vista (-1 si la rueda esta vacia).
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return true si esta fija, false de lo contrario.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Fija la rueda (si su tipo lo permite).
     */
    public void lock() {
        if (canLock()) {
            this.locked = true;
        }
    }

    /**
     * Libera la rueda.
     */
    public void unlock() {
        this.locked = false;
    }

    /**
     * @return true si la rueda se deja fijar.
     */
    public boolean canLock()   { return true; }

    /**
     * @return true si la rueda se deja intercambiar.
     */
    public boolean canSwap()   { return true; }

    /**
     * @return true si la rueda se deja eliminar.
     */
    public boolean canDelete() { return true; }

    // MÉTODOS AUXILIARES

    /**
     * Deja a la vista el simbolo de la posicion dada; si cambio, lo selecciona.
     */
    private void moveTo(int newPosition) {
        if (newPosition != position) {
            position = newPosition;
            symbols.get(position).selected();
        }
    }

    /**
     * @return indice del simbolo con ese color, o -1.
     */
    private int indexOf(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equals(color)) {
                return i;
            }
        }
        return -1;
    }
}
