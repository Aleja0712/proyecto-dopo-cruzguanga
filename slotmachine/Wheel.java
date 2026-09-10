import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * la clase wheel representa cada una de las ruedas que forman parte de 
 * la maquina tragamonedas
 * 
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 2.0
 */
public class Wheel {
    private List<Symbol> symbols;
    private Symbol currentSymbol;
    private int position;
    private boolean locked;
    private Random randomGenerator;

    /**
     * Constructor para inicializar una rueda.
     * @param position posicion de la rueda en la maquina.
     */
    public Wheel(int position) {
        this.position = position;
        this.symbols = new ArrayList<>();
        this.locked = false;
        this.randomGenerator = new Random();
    }

    /**
     * Anade un simbolo a la rueda.
     * @param symbol simbolo a agregar.
     */
    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
        if (currentSymbol == null) {
            currentSymbol = symbol;
        }
    }

    /**
     * Elimina un simbolo de la rueda.
     * @param symbol simbolo a eliminar.
     */
    public void removeSymbol(Symbol symbol) {
        symbols.remove(symbol);
        if (currentSymbol == symbol) {
            if (symbols.size() == 0) {
                currentSymbol = null;
            } else {
                currentSymbol = symbols.get(0);
            }
        }
    }

    /**
     * Elimina un simbolo de la rueda por su color.
     * @param color nombre del color del simbolo a eliminar.
     */
    public void delSymbol(String color) {
        symbols.removeIf(s -> s.getColor().equals(color));
        if (currentSymbol != null && currentSymbol.getColor().equals(color)) {
            if (symbols.size() == 0) {
                currentSymbol = null;
            } else {
                currentSymbol = symbols.get(0);
            }
        }
    }

    /**
     * Gira la rueda seleccionando un simbolo al azar de la lista.
     * 
     */
    public void spin() {
        if (symbols.size() > 0 && !locked) {
            int randomIndex = randomGenerator.nextInt(symbols.size());
            this.currentSymbol = symbols.get(randomIndex);
        }
    }

    /**
     * Retorna todos los simbolos contenidos en la rueda.
     * @return lista de simbolos.
     */
    public List<Symbol> getSymbols() {
        return symbols;
    }

    /**
     * Retorna el simbolo actual.
     * @return simbolo actual en la rueda.
     */
    public Symbol getCurrentSymbol() {
        return currentSymbol;
    }

    /**
     * Hace visible los simbolos de la rueda
     */
    public void makeVisible() {
        for (Symbol s : symbols) {
            s.makeVisible();
        }
    }

    /**
     * Hace invisible los simbolos de la rueda
     */
    public void makeInvisible() {
        for (Symbol s : symbols) {
            s.makeInvisible();
        }
    }

    /**
     * Muestra la rueda
     */
    public void show() {
        makeVisible();
    }

    /**
     * Oculta la rueda
     */
    public void hide() {
        makeInvisible();
    }

    /**
     * Retorna el estado de bloqueo de la rueda.
     * @return true si esta bloqueada, false de lo contrario.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Fija la rueda
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * Libera la rueda
     */
    public void unlock() {
        this.locked = false;
    }
}