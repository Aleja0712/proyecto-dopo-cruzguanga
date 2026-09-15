import java.util.ArrayList;

/**
 * Se incorpora esta clase, con el fin de que SlotMachine no suma toda la lógica 
 * del juego, su objetivo es representar el soporte fisico y mecanico 
 * que contiene, organize y posiciona las Wheels de la SlotMachine
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez 
 */

public class Eje {
    // Constantes de posicionamiento fisico en el Canvas
    private static final int START_X = 50;
    private static final int Y = 100;
    private static final int SPACING = 80;

    private int x;
    private ArrayList<Wheel> wheels;

    /**
     * Constructor para objetos de la clase Eje.
     */
    public Eje() {
        this.x = START_X;
        this.wheels = new ArrayList<Wheel>();
    }

    /**
     * Adiciona una rueda en la posicion indicada y calcula sus coordenadas fisicas.
     * @param pos posicion deseada por el usuario (1-based).
     * @return la rueda creada y posicionada.
     */
    public Wheel addWheel(int pos) {
        int adjustedPos = (wheels.isEmpty()) ? 1 : clamp(pos, wheels.size() + 1);
        
        Wheel wheel = new Wheel(adjustedPos);
        
        // Asignacion de coordenadas fisicas segun el indice en el eje
        int newPosX = START_X + (adjustedPos - 1) * SPACING;
        int newPosY = Y;
        wheel.changePositionX(newPosX);
        wheel.changePositionY(newPosY);
        
        wheels.add(adjustedPos - 1, wheel);
        relocate();
        return wheel;
    }

    /**
     * Elimina la rueda en la posicion indicada si no esta fija.
     * @param pos posicion de la rueda (1-based).
     * @return la rueda eliminada, o null si no se pudo eliminar.
     */
    public Wheel delWheel(int pos) {
        if (wheels.isEmpty()) return null;
        
        int p = clamp(pos, wheels.size());
        Wheel w = wheels.get(p - 1);
        
        if (w.isLocked()) {
            return null;
        }
        
        w.hide();
        wheels.remove(p - 1);
        relocate();
        return w;
    }

    /**
     * Retorna la rueda en la posicion indicada.
     */
    public Wheel getWheel(int pos) {
        if (wheels.isEmpty()) return null;
        int p = clamp(pos, wheels.size());
        return wheels.get(p - 1);
    }
    /**
     * Permite recorrer la lista de las Wheels
     */
    public ArrayList<Wheel> getWheels() {
        return wheels;
    }
    /**
     * Consulta cuantas ruedas hay montadas
     */
    public int getWheelCount() {
        return wheels.size();
    }
    /**
     * Consulta si la lista de las Wheels esta vacia
     */
    public boolean isEmpty() {
        return wheels.isEmpty();
    }
    
        /**
     * Intercambia dos ruedas dentro del eje y reubica físicamente toda la fila.
     * @param pos1 posición de la primera rueda (1-based).
     * @param pos2 posición de la segunda rueda (1-based).
     */
    public void swap(int pos1, int pos2) {
        if (wheels.size() < 2) {
            return;
        }
        int p1 = clamp(pos1, wheels.size());
        int p2 = clamp(pos2, wheels.size());
        if (p1 == p2) {
            return;
        }
        Wheel temp = wheels.get(p1 - 1);
        wheels.set(p1 - 1, wheels.get(p2 - 1));
        wheels.set(p2 - 1, temp);
        relocate();
    }

    /**
     * Recalcula las coordenadas de todas las ruedas según su índice en el eje.
     * Invariante: la rueda i-ésima siempre queda en START_X + i * SPACING.
     */
    private void relocate() {
        for (int i = 0; i < wheels.size(); i++) {
            Wheel w = wheels.get(i);
            w.changePositionX(START_X + (i * SPACING));
            w.changePositionY(Y);
        }
    }

    /**
     * @return coordenada X de la primera rueda del eje.
     */
    public int getStartX() {
        return START_X;
    }

    /**
     * @return coordenada Y común a todas las ruedas del eje.
     */
    public int getStartY() {
        return Y;
    }

    /**
     * Ajusta la posicion dentro de los limites validos [1, max].
     */
    private int clamp(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }
}