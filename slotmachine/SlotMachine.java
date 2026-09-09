import java.util.ArrayList;
import java.util.LinkedHashSet;
import javax.swing.JOptionPane;

/**
 * Simula una máquina tragamonedas compuesta por una o más ruedas
 * 
 * Invariante de clase:
 * - wheels != null
 * - machine != null
 * 
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 2.0
 */
public class SlotMachine {

    // ATRIBUTOS Y CONSTANTES
    private static final int START_X = 50;
    private static final int Y = 100;
    private static final int SPACING = 80;
    private static final int NORMAL_SIZE = 30;
    private static final int JACKPOT_SIZE = 50;
    private static final int FRAME_MARGIN = 30;
    private static final String FRAME_COLOR = "darkGray";

    // Ciclo 2
    private static final int STEP_DELAY = 200;

    private ArrayList<Wheel> wheels;
    private boolean isOK;
    private boolean visible;
    private Rectangle machine;

    /**
     * Construye una máquina tragamonedas sin ruedas y en estado correcto.
     */
    public SlotMachine() {
        this.isOK = true;
        this.visible = false;
        this.wheels = new ArrayList<Wheel>();
        this.machine = new Rectangle();
        this.machine.changeColor(FRAME_COLOR);
    }

    // CICLO 1

    /**
     * Adiciona una nueva rueda a la máquina en la posición deseada.
     * @param pos posición donde insertar la rueda (1-based).
     */
    public void addWheel(int pos) {
        int p = clamp(pos, wheels.size() + 1);
        Wheel wheel = new Wheel();
        
        int newPosX = START_X + (p - 1) * SPACING;
        int newPosY = Y;
        wheel.changePositionX(newPosX);
        wheel.changePositionY(newPosY);
        
        // Insercion de la rueda en la lista
        wheels.add(p - 1, wheel);
        succeed();
        redraw();
    }

    /**
     * Elimina la rueda ubicada en la posición indicada.
     * @param pos posición de la rueda a eliminar.
     */
    public void delWheel(int pos) {
        Wheel w = getValidWheel(pos);
        if (w == null) return;

        if (w.isLocked()) {
            fail("La rueda indicada esta bloqueada.");
            return;
        }

        w.hide();
        wheels.remove(w);
        succeed();
        redraw();
    }

    /**
     * Añade un nuevo símbolo a una rueda en la posición y color indicados.
     * @param pos posición de la rueda receptora
     * @param color color CSS del símbolo a insertar
     */
    public void addSymbol(int pos, String color) {
        Wheel w = getValidWheel(pos);
        if (w == null) return;

        int symbolPos = clamp(pos, w.size() + 1);
        w.addSymbol(symbolPos, color);
        succeed();
        redraw();
    }

    /**
     * Elimina un símbolo de todas las ruedas que lo contengan.
     * @param symbol color CSS del símbolo a eliminar.
     */
    public void delSymbol(String symbol) {
        boolean removed = false;
        
        for (Wheel w : wheels) {
            // 1.1: delSymbol(symbol: String) : void
            if (w.delSymbol(symbol)) {
                removed = true;
            }
        }
        if (removed) {
            succeed();
            redraw();
        } else {
            fail("El simbolo indicado no existe en ninguna rueda.");
        }
    }

    /**
     * Posiciona un símbolo específico como visible en la rueda indicada.
     * @param wheel posición de la rueda.
     * @param symbol color CSS del símbolo a mostrar.
     */
    public void placeSymbol(int wheel, String symbol) {
        Wheel targetWheel = getValidWheel(wheel);
        if (targetWheel == null) return;
    
        if (targetWheel.isLocked()) {
            fail("La rueda indicada esta bloqueada.");
            return;
        }
    
        boolean found = false;
        for (Wheel w : wheels) {
            if (w == targetWheel) {
                // 2: addSymbol(symbol: String) : void
                if (w.setCurrentByColor(symbol)) {
                    found = true;
                    break;
                }
            }
        }
    
        if (found) {
            succeed();
            redraw();
        } else {
            fail("El simbolo indicado no existe en la rueda.");
        }
    }

    /**
     * Requisito 4: Gira la rueda especificada al azar.
     * @param wheel posición de la rueda a girar.
     */
    public void spin(int wheel) {
        Wheel w = getValidWheel(wheel);
        if (w == null) return;

        if (w.isLocked()) {
            fail("La rueda indicada esta bloqueada.");
            return;
        }
        if (w.isEmpty()) {
            fail("La rueda indicada no tiene simbolos.");
            return;
        }

        w.spin();
        succeed();
        redraw();
    }

    /**
     * Requisito 4: Gira todas las ruedas de la máquina que no estén 
     * fijas ni vacías.
     */
    public void spin() {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }

        boolean any = false;
        for (Wheel w : wheels) {
            if (!w.isEmpty() && !w.isLocked()) {
                w.spin();
                any = true;
            }
        }

        if (any) {
            succeed();
            redraw();
        } else {
            fail("Ninguna rueda esta disponible para girar.");
        }
    }

    /**
     * Requisito 5: Retorna los colores de los símbolos de la máquina rueda 
     * por rueda.
     * @return arreglo con todos los colores agregados en orden.
     */
    public String[] symbols() {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return new String[0];
        }
        ArrayList<String> all = new ArrayList<String>();
        for (Wheel w : wheels) {
            for (String color : w.allColors()) {
                all.add(color);
            }
        }
        succeed();
        return all.toArray(new String[0]);
    }

    /**
     * Requisito 5: Retorna la cantidad de símbolos distintos en toda la máquina.
     * @return total de colores únicos presentes.
     */
    public int distinctSymbols() {
        LinkedHashSet<String> distinct = new LinkedHashSet<String>();
        for (Wheel w : wheels) {
            for (String color : w.allColors()) {
                distinct.add(color);
            }
        }
        succeed();
        return distinct.size();
    }

    /**
     * Requisito 5: Retorna los colores de los símbolos visibles ordenados
     * de izquierda a derecha.
     * @return arreglo con los colores visibles actuales.
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).currentColor();
        }
        succeed();
        return config;
    }

    /**
     * Requisito 6: Evalúa si la configuración actual es ganadora (Jackpot).
     * @return true si todas las ruedas muestran el mismo símbolo.
     */
    public boolean isJackpot() {
        String[] config = configuration();
        if (config.length == 0) {
            return false;
        }

        String first = config[0];
        if (first == null) {
            return false;
        }

        for (int i = 1; i < config.length; i++) {
            if (!first.equals(config[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Requisito 7: Hace visible la interfaz gráfica del simulador.
     */
    public void makeVisible() {
        if (!visible) {
            visible = true;
            redraw();
            succeed();
        }
    }

    /**
     * Requisito 7: Oculta la interfaz gráfica del simulador.
     */
    public void makeInvisible() {
        if (!visible) return;
        for (Wheel w : wheels) {
            w.hide();
        }
        machine.makeInvisible();
        visible = false;
        succeed();
    }

    /**
     * Requisito 8: Cierra el simulador, limpia las ruedas y apaga la vista 
     * gráfica.
     */
    public void exit() {
        makeInvisible();
        wheels.clear();
        isOK = true;
    }

    /**
     * Requisito de Diseño: Indica si la última operación se realizó 
     * exitosamente.
     * @return true si la última operación fue válida.
     */
    public boolean ok() {
        return isOK;
    }

    // CICLO 2

    /**
     * Requisito 9: Intercambia de posición dos ruedas de la máquina.
     * @param wheel1 posición de la primera rueda (1-based)
     * @param wheel2 posición de la segunda rueda (1-based)
     */
    public void swap(int wheel1, int wheel2) {
        int p1 = clamp(wheel1, wheels.size());
        int p2 = clamp(wheel2, wheels.size());
    
        if (wheels.size() >= 2 && p1 != p2) {
            Wheel w1 = wheels.get(p1 - 1);
            Wheel w2 = wheels.get(p2 - 1);
    
            if (w1.isLocked() || w2.isLocked()) {
                fail("No se puede intercambiar una rueda fija.");
                return;
            }
    
            // 1.2 y 1.2.1: swap entre ruedas en la coleccion
            wheels.set(p1 - 1, w2);
            wheels.set(p2 - 1, w1);
    
            succeed();
            redraw();
        } else {
            fail("No se cumplen las condiciones para intercambiar ruedas.");
        }
    }

    /**
     * Requisito 10: Bloquea una rueda para impedir giros o intercambios.
     * @param wheel posición de la rueda a bloquear.
     */
    public void lock(int wheel) {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
    
        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        boolean locked = w.isLocked();

        if (!locked) {
            w.lock();
            succeed();
        } else {
            fail("La rueda indicada ya esta bloqueada.");
        }
    }

    /**
     * Requisito 10: Desbloquea una rueda que estaba fija.
     * @param wheel posición de la rueda a desbloquear.
     */
    public void unlock(int wheel) {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
    
        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        boolean locked = w.isLocked();
  
        if (locked) {
            w.unlock();
            succeed();
        } else {
            fail("La rueda indicada no esta bloqueada.");
        }
    }

    /**
     * Requisito 11: Rota una rueda un número de pasos con animación
     * visual paso a paso.
     * @param wheel posición de la rueda a rotar.
     * @param steps pasos a avanzar (positivo: adelante, negativo: reverso).
     */
    public void spin(int wheel, int steps) {
        Wheel w = getValidWheel(wheel);
        if (w == null) return;

        if (w.isEmpty()) {
            fail("La rueda indicada no tiene simbolos.");
            return;
        }
        if (w.isLocked()) {
            fail("La rueda indicada esta fija.");
            return;
        }

        int direction = steps < 0 ? -1 : 1;
        int total = Math.abs(steps);
        for (int i = 0; i < total; i++) {
            w.rotate(direction);
            if (visible) {
                redraw();
                Canvas.getCanvas().wait(STEP_DELAY);
            }
        }
        succeed();
        redraw();
    }

    /**
     * Requisito 12: Establece la máquina en una configuración fija de símbolos.
     * @param setSymbols arreglo con el color que debe mostrar cada rueda.
     */
    public void spin(String[] setSymbols) {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        if (setSymbols == null || setSymbols.length != wheels.size()) {
            fail("La configuracion debe indicar un simbolo por cada rueda.");
            return;
        }

        for (int i = 0; i < wheels.size(); i++) {
            Wheel w = wheels.get(i);
            if (!w.contains(setSymbols[i])) {
                fail("La rueda " + (i + 1) + " no tiene el simbolo indicado.");
                return;
            }
            if (w.isLocked() && !setSymbols[i].equals(w.currentColor())) {
                fail("La rueda " + (i + 1) + " esta fija y no puede cambiar de simbolo.");
                return;
            }
        }

        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).setCurrentByColor(setSymbols[i]);
        }
        succeed();
        redraw();
    }

    // Métodos auxiliares

    /**
     * Limita un índice dentro del rango [1, max]
     */
    private int clamp(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    /**
     * Registra exclusivamente el estado exitoso de la operación sin redibujar.
     */
    private void succeed() {
        isOK = true;
    }

    /**
     * Actualiza el estado a fallido y muestra un cuadro de diálogo si está visible.
     */
    private void fail(String message) {
        isOK = false;
        if (visible) {
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida que la máquina contenga ruedas y retorna la rueda correspondiente.
     */
    private Wheel getValidWheel(int pos) {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return null;
        }
        int p = clamp(pos, wheels.size());
        return wheels.get(p - 1);
    }
    
    private boolean notEmpty() {
        for (Wheel w : wheels) {
            if (w.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Redibuja la carcasa exterior y las ruedas sincronizándolas con el Canvas.
     */
    private void redraw() {
        if (!visible) return;

        int size = isJackpot() ? JACKPOT_SIZE : NORMAL_SIZE;
        int slots = Math.max(wheels.size(), 1);
        int frameX = START_X - FRAME_MARGIN;
        int frameY = Y - FRAME_MARGIN;
        int frameWidth = (slots - 1) * SPACING + size + 2 * FRAME_MARGIN;
        int frameHeight = size + 2 * FRAME_MARGIN;

        Canvas.getCanvas().resize(frameX + frameWidth + FRAME_MARGIN,
                                  frameY + frameHeight + FRAME_MARGIN);

        machine.changeSize(frameHeight, frameWidth);
        machine.moveTo(frameX, frameY);

        if (wheels.isEmpty()) {
            machine.makeInvisible();
        } else {
            machine.makeVisible();
        }

        int x = START_X;
        for (Wheel w : wheels) {
            w.showAt(x, Y, size);
            x += SPACING;
        }
    }
}