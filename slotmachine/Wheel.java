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
    
    private static final int SYMBOL_OFFSET_X = 15;
    private static final int SYMBOL_OFFSET_Y = 45;
    private boolean isVisible;
    
    
    private List<Symbol> symbols;
    private Symbol currentSymbol;
    private int position;
    private boolean locked;
    private Random randomGenerator;
    private Rectangle wheel;

    /**
     * Constructor para inicializar una rueda.
     * @param position posicion de la rueda en la maquina.
     */
    public Wheel(int position) {
        this.position = position;
        this.symbols = new ArrayList<>();
        this.locked = false;
        this.isVisible = false;
        this.randomGenerator = new Random();
        this.wheel = new Rectangle();
        this.wheel.changeSize(120,60);
        this.wheel.changeColor("lightGray");
    }

    /**
     * Añade un simbolo a la rueda.
     * @param symbol simbolo a agregar.
     */
    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
        showSymbol(symbol);
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
            rotate(1 + randomGenerator.nextInt(symbols.size()));
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
        this.isVisible = true;
        wheel.makeVisible();
        centerSymbol();
        if (currentSymbol != null) {
            currentSymbol.show();
        }
    }

    /**
     * Hace invisible los simbolos de la rueda
     */
    public void makeInvisible() {
        this.isVisible = false;
        wheel.makeInvisible();
        for (Symbol s : symbols) {
            s.hide();
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
    
    /**
     * Metodo para que Eje coloque la rueda en sus coordenadas X
     */
    public void changePositionX(int newX) {
        wheel.moveHorizontal(newX - wheel.xPosition);
        centerSymbol();
    }
    
    /**
     * Metodo para que Eje coloque la rueda en sus coordenadas Y
     */

    public void changePositionY(int newY) {
        wheel.moveVertical(newY - wheel.yPosition);
        centerSymbol();
    }
    
    /**
     * Rota la rueda un número de pasos sobre su secuencia de símbolos.
     * Pasos positivos avanzan y negativos retroceden. El desplazamiento es cíclico,
     * por lo que |steps| se reduce módulo el número de símbolos.
     * Si la rueda es visible, el movimiento se visualiza paso a paso.
     * Invariante: la rueda fija (locked) nunca se mueve.
     * @param steps cantidad de pasos a rotar.
     */
    public void rotate(int steps) {
        if (symbols.isEmpty() || locked) {
            return;
        }
        int total = symbols.size();
        int index = symbols.indexOf(currentSymbol);
        if (index < 0) {
            index = 0;
        }
        int direction = (steps < 0) ? -1 : 1;
        int movements = (int) (Math.abs((long) steps) % total);
        for (int i = 0; i < movements; i++) {
            index = ((index + direction) % total + total) % total;
            showSymbol(symbols.get(index));
        }
    }

    /**
     * Indica si la rueda contiene un símbolo del color dado.
     * @param color color buscado.
     * @return true si existe, false en caso contrario.
     */
    public boolean contains(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ubica la rueda sobre el símbolo del color indicado, sin agregar símbolos nuevos.
     * @param color color del símbolo que debe quedar a la vista.
     * @return true si el símbolo existía y quedó visible, false si no existe.
     */
    public boolean place(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equals(color)) {
                showSymbol(s);
                return true;
            }
        }
        return false;
    }

    /**
     * Cambia el símbolo a la vista: oculta el anterior, centra y dibuja el nuevo.
     * @param symbol símbolo que pasa a ser el actual.
     */
    private void showSymbol(Symbol symbol) {
        if (currentSymbol != null && currentSymbol != symbol) {
            currentSymbol.hide();
        }
        currentSymbol = symbol;
        centerSymbol();
        if (isVisible && currentSymbol != null) {
            currentSymbol.show();
            Canvas.getCanvas().wait(80);
        }
    }

    /**
     * Centra el símbolo actual dentro del rectángulo contenedor de la rueda.
     */
    private void centerSymbol() {
        if (currentSymbol == null) {
            return;
        }
        Shapes shape = currentSymbol.getShape();
        shape.moveHorizontal(wheel.xPosition + SYMBOL_OFFSET_X - shape.xPosition);
        shape.moveVertical(wheel.yPosition + SYMBOL_OFFSET_Y - shape.yPosition);
    }
}