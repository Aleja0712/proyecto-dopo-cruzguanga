package domain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el soporte mecanico que contiene y ordena las ruedas de la
 * SlotMachine. Solo maneja el orden logico; las coordenadas de dibujo
 * las calcula la presentacion.
 * Todas las posiciones son 1-based y se ajustan al rango valido.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class Eje {
    private final ArrayList<Wheel> wheels;

    /**
     * Constructor para objetos de la clase Eje.
     */
    public Eje() {
        this.wheels = new ArrayList<Wheel>();
    }

    /**
     * Monta una rueda en la posicion indicada.
     * @param wheel rueda a montar.
     * @param pos posicion deseada (1-based), ajustada a [1, n+1].
     * @return true si se monto.
     */
    public boolean addWheel(Wheel wheel, int pos) {
        if (wheel == null) {
            return false;
        }
        int p = clamp(pos, wheels.size() + 1);
        wheels.add(p - 1, wheel);
        return true;
    }

    /**
     * Desmonta la rueda de la posicion indicada si no esta fija y se deja eliminar.
     * @param pos posicion de la rueda (1-based).
     * @return la rueda eliminada, o null si no se pudo eliminar.
     */
    public Wheel delWheel(int pos) {
        if (wheels.isEmpty()) {
            return null;
        }
        int p = clamp(pos, wheels.size());
        Wheel w = wheels.get(p - 1);
        if (w.isLocked() || !w.canDelete()) {
            return null;
        }
        wheels.remove(p - 1);
        return w;
    }

    /**
     * @return la rueda en la posicion indicada (1-based), o null si no hay ruedas.
     */
    public Wheel getWheel(int pos) {
        if (wheels.isEmpty()) {
            return null;
        }
        return wheels.get(clamp(pos, wheels.size()) - 1);
    }

    /**
     * @return vista de solo lectura de las ruedas, de izquierda a derecha.
     */
    public List<Wheel> getWheels() {
        return Collections.unmodifiableList(wheels);
    }

    /**
     * @return cantidad de ruedas montadas.
     */
    public int getWheelCount() {
        return wheels.size();
    }

    /**
     * @return true si no hay ruedas.
     */
    public boolean isEmpty() {
        return wheels.isEmpty();
    }

    /**
     * Intercambia dos ruedas.
     * @param pos1 posición de la primera rueda (1-based).
     * @param pos2 posición de la segunda rueda (1-based).
     */
    public void swap(int pos1, int pos2) {
        if (wheels.size() < 2) {
            return;
        }
        int p1 = clamp(pos1, wheels.size());
        int p2 = clamp(pos2, wheels.size());
        Collections.swap(wheels, p1 - 1, p2 - 1);
    }

    /**
     * @return la rueda a la izquierda de pos, o null si pos es la primera.
     */
    public Wheel leftOf(int pos) {
        int p = clamp(pos, wheels.size());
        return (p > 1) ? wheels.get(p - 2) : null;
    }

    /**
     * Ajusta la posicion dentro de los limites validos [1, max].
     */
    public static int clamp(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }
}
