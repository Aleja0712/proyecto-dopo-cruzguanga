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
    private static final int FRAME_HEIGHT = 180;
    private static final int WHEEL_HEIGHT = 120;
    private static final int MARGIN_HORIZONTAL = 60;
    private static final int SPACING_PER_WHEEL = 80;

    private Eje eje;
    private Rectangle machineFrame; // Carcasa exterior de la maquina
    private static final int BASE_HEIGHT = 20;
    private Rectangle machineBase; 
    private ArrayList<Wheel> wheels;
    private boolean isVisible;
    private boolean lastOperationOk;

    /**
     * Constructor del simulador
     */
    public SlotMachine() {
        this.eje = new Eje();
        this.wheels = eje.getWheels();
        this.isVisible = false;
        this.lastOperationOk = true;
        
        // Marco exterior
        this.machineFrame = new Rectangle();
        this.machineFrame.changeColor("purple");

        this.machineBase = new Rectangle();
        this.machineBase.changeColor("lightGray");

        updateMachineFrame();
    }
    /**
     * Ajusta dinámicamente tamaño y posición del marco exterior de la máquina.
    */
    private void updateMachineFrame() {
        int count = eje.getWheelCount();
        int calculatedWidth = (count == 0) ? 120 : (count * SPACING_PER_WHEEL) + MARGIN_HORIZONTAL;
        int targetX = eje.getStartX() - (MARGIN_HORIZONTAL / 2);
        int targetY = eje.getStartY() - ((FRAME_HEIGHT - WHEEL_HEIGHT) / 2);

        machineFrame.changeSize(FRAME_HEIGHT, calculatedWidth);
        machineFrame.moveHorizontal(targetX - machineFrame.xPosition);
        machineFrame.moveVertical(targetY - machineFrame.yPosition);
        
        int baseWidth = calculatedWidth - 20;
        machineBase.changeSize(BASE_HEIGHT, baseWidth);
        machineBase.moveHorizontal((targetX + 10) - machineBase.xPosition);
        machineBase.moveVertical((targetY + FRAME_HEIGHT) - machineBase.yPosition);
        redraw();
    }

    /**
     * Redibuja la máquina respetando el orden de profundidad:
     * primero el marco y luego las ruedas con sus símbolos.
     */
    private void redraw() {
        if (!isVisible) {
            return;
        }
        machineFrame.makeVisible();
        machineBase.makeVisible();
        for (Wheel w : eje.getWheels()) {
            w.makeVisible();
        }
    }
    
    /**
     * Añade una rueda
     */
    public void addWheel(int pos) {
        Wheel w = eje.addWheel(pos);
        if (w != null) {
            updateMachineFrame();
            succeed();
        } else {
            fail("No se pudo anadir la rueda.");
        }
    }
    
    /**
     * Elimina una rueda
     */
    public void delWheel(int pos) {
        Wheel w = eje.delWheel(pos);
        if (w != null) {
            updateMachineFrame(); // Encoge el rectangulo exterior
            succeed();
        } else {
            fail("No se pudo eliminar la rueda.");
        }
    }
    
    /**
     * Hace visible las ruedas
     */
    public void makeVisible() {
        this.isVisible = true;
        redraw();
    }
    
    /**
     * Hace invisible las ruedas
     */
    public void makeInvisible() {
        this.isVisible = false;
        machineFrame.makeInvisible();
        machineBase.makeInvisible();
        for (Wheel w : eje.getWheels()) {
            w.makeInvisible();
        }
    }

    /**
     * Adiciona un simbolo a una rueda especifica
     * @param pos posicion de la rueda.
     * @param color color del simbolo.
     */
    public void addSymbol(int pos, String color) {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        Wheel w = eje.getWheel(pos);
        if (w.contains(color)) {
            fail("La rueda ya contiene el simbolo " + color + ".");
            return;
        }
        w.addSymbol(new Symbol(color, color, "circle"));
        redraw();
        succeed();
    }
    
      /**
     * Elimina un simbolo especifico de todas las ruedas en la maquina
     * @param symbol color del simbolo a eliminar.
     */
    public void delSymbol(String symbol) {
        boolean removed = false;
        for (Wheel w : wheels) {
            int sizeBefore = w.getSymbols().size();
            w.delSymbol(symbol);
            if (w.getSymbols().size() < sizeBefore) {
                removed = true;
            }
        }
        
        if (removed) {
            succeed();
        } else {
            fail("No se encontro el simbolo.");
        }
    }

    /**
     * Adiciona un simbolo a una rueda especifica en la maquina
     * @param wheel posicion de la rueda.
     * @param symbol color del simbolo a adicionar.
     */
    public void placeSymbol(int wheel, String symbol) {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        Wheel w = eje.getWheel(wheel);
        if (w.isLocked()) {
            fail("La rueda esta fija.");
            return;
        }
        if (!w.place(symbol)) {
            fail("La rueda no contiene el simbolo " + symbol + ".");
            return;
        }
        succeed();
    }

    /**
     * Gira la rueda especificada al azar
     * @param wheel posición de la rueda a girar.
     */
    public void spin(int wheel) {
        if (wheels.size() == 0) {
            fail("No hay ruedas en la maquina.");
            return;
        }

        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);

        if (w.isLocked()) {
            fail("La rueda indicada esta bloqueada.");
            return;
        }
        
        if (w.getSymbols().size() == 0) {
            fail("La rueda indicada no tiene simbolos.");
            return;
        }

        w.spin();
        succeed();
    }
    
    /**
     * Gira la rueda especificada una cantidad de pasos
     * @param wheel posicion de la rueda a rotar.
     * @param steps cantidad de pasos.
     */
    public void spin(int wheel, int steps) {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        Wheel w = eje.getWheel(wheel);
        if (w.isLocked()) {
            fail("La rueda indicada esta fija.");
            return;
        }
        if (w.getSymbols().isEmpty()) {
            fail("La rueda indicada no tiene simbolos.");
            return;
        }
        w.rotate(steps);
        succeed();
    }
    
        /**
     * Gira las ruedas y deja la maquina en una configuracion especifica
     * @param setSymbols arreglo con los colores.
     */
    public void spin(String[] setSymbols) {
        int count = eje.getWheelCount();
        if (setSymbols == null || count == 0 || count != setSymbols.length) {
            fail("Configuracion invalida.");
            return;
        }
        // Fase 1: validar TODO antes de mover nada (la operacion es atomica)
        for (int i = 0; i < count; i++) {
            Wheel w = eje.getWheel(i + 1);
            String target = setSymbols[i];
            if (!w.contains(target)) {
                fail("La rueda " + (i + 1) + " no tiene el simbolo " + target + ".");
                return;
            }
            Symbol current = w.getCurrentSymbol();
            boolean alreadyThere = (current != null) && current.getColor().equals(target);
            if (w.isLocked() && !alreadyThere) {
                fail("La rueda " + (i + 1) + " esta fija y tendria que moverse.");
                return;
            }
        }
        // Fase 2: aplicar
        for (int i = 0; i < count; i++) {
            Wheel w = eje.getWheel(i + 1);
            if (!w.isLocked()) {
                w.place(setSymbols[i]);
            }
        }
        succeed();
    }

    /**
     * Gira todas las ruedas de la maquina que no esten fijas ni vacias
     */
    public void spin() {
        if (wheels.size() == 0) {
            fail("No hay ruedas en la maquina.");
            return;
        }

        if (!notEmpty()) {
            fail("Todas las ruedas estan vacias.");
            return;
        }

        boolean algunaGiro = false;
        
        for (Wheel w : wheels) {
            if (!w.isLocked() && w.getSymbols().size() > 0) {
                w.spin();
                algunaGiro = true;
            }
        }

        if (algunaGiro) {
            succeed();
        } else {
            fail("No se pudo girar ninguna rueda.");
        }
    }

    /**
     * Consulta los colores de todos los simbolos presentes en la maquina
     * @return arreglo con los colores.
     */
    public String[] symbols() {
        ArrayList<String> list = new ArrayList<>();
        for (Wheel w : wheels) {
            for (Symbol s : w.getSymbols()) {
                list.add(s.getColor()); 
            }
        }
        
        succeed();
        return list.toArray(new String[0]);
    }

    /**
     * Consulta la cantidad de simbolos diferentes en la maquina
     * @return cantidad de simbolos unicos.
     */
    public int distinctSymbols() {
        ArrayList<String> seen = new ArrayList<>();
        for (Wheel w : eje.getWheels()) {
            Symbol current = w.getCurrentSymbol();
            String color = (current == null) ? "empty" : current.getColor();
            if (!seen.contains(color)) {
                seen.add(color);
            }
        }
        succeed();
        return seen.size();
    }

    /**
     * Retorna los colores de los simbolos visibles actualmente en todas las ruedas
     * @return arreglo con la configuracion.
     */
    public String[] configuration() {
        ArrayList<String> list = new ArrayList<>();
        for (Wheel w : wheels) {
            Symbol current = w.getCurrentSymbol();
            if (current != null) {
                list.add(current.getColor());
            } else {
                list.add("empty");
            }
        }
        
        succeed();
        return list.toArray(new String[0]);
    }
    
       /**
     * Verifica si la configuracion actual de las ruedas es ganadora
     * @return true si es ganadora, false de lo contrario.
     */
    public boolean isJackpot() {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return false;
        }
        
        boolean value = false;
        ArrayList<String> list = new ArrayList<>();
        
        for (Wheel w : wheels) {
            Symbol current = w.getCurrentSymbol();
            if (current != null) {
                list.add(current.getColor());
            } else {
                fail("Rueda vacia.");
                return false; 
            }
        }
        
        if (list.size() > 0) {
            boolean allEqual = true;
            String firstColor = list.get(0);
            for (String color : list) {
                if (!firstColor.equals(color)) {
                    allEqual = false;
                    break;
                }
            }
            if (allEqual) {
                value = true;
            }
        }
        
        succeed();
        return value;
    }

    /**
     * Termina la ejecucion del simulador de la maquina tragamonedas
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Intercambia la posicion de dos ruedas dentro de la maquina
     * @param wheel1 posicion de la primera rueda.
     * @param wheel2 posicion de la segunda rueda.
     */
    public void swap(int wheel1, int wheel2) {
        if (eje.getWheelCount() < 2) {
            fail("Se necesitan al menos dos ruedas.");
            return;
        }
        int p1 = clamp(wheel1, eje.getWheelCount());
        int p2 = clamp(wheel2, eje.getWheelCount());
        if (p1 == p2) {
            fail("Las posiciones son iguales.");
            return;
        }
        if (eje.getWheel(p1).isLocked() || eje.getWheel(p2).isLocked()) {
            fail("Una de las ruedas esta fija.");
            return;
        }
        eje.swap(p1, p2);
        redraw();
        succeed();
    }

    /**
     * Fija una rueda especifica para que no pueda girar
     * @param wheel posicion de la rueda a fijar.
     */
    public void lock(int wheel) {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return;
        }

        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        boolean locked = w.isLocked();

        if (!locked) {
            w.lock(); 
            succeed();
        } else {
            fail("Ya esta bloqueada.");
        }
    }
    
    /**
     * Desbloquea una rueda especifica que estaba fija
     * @param wheel posicion de la rueda a desbloquear.
     */
    public void unlock(int wheel) {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return;
        }
    
        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        boolean locked = w.isLocked();
  
        if (locked) {
            w.unlock();
            succeed();
        } else {
            fail("No esta bloqueada.");
        }
    }
    
    /**
     * MÉTODOS AUXILIARES
     */
    
    /**
     * Metodo auxiliar para verificar que existen simbolos en las ruedas
     * @return true si hay al menos un simbolo en alguna rueda, false si no.
     */
    private boolean notEmpty() {
        for (Wheel w : wheels) {
            if (w.getSymbols().size() > 0) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Ajusta la posicion ingresada para que este en un rango valido
     * @param max limite maximo permitido.
     * @return posicion ajustada.
     */
    
    private int clamp(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    /**
     * Registra que la operacion fue exitosa
     */
    private void succeed() {
        this.lastOperationOk = true;
    }

    /**
     * Registra el fallo de la operacion y muestra alerta si esta visible
     * @param message mensaje de error.
     */
    private void fail(String message) {
        this.lastOperationOk = false;
        if (this.isVisible) {
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Indica si la ultima operacion fue exitosa
     * @return true si funciono, false si fallo.
     */
    public boolean ok() {
        return this.lastOperationOk;
    }

}