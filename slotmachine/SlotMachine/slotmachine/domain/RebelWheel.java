package domain;

/**
 * Rueda rebelde: no se deja bloquear, ni intercambiar, ni eliminar.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class RebelWheel extends Wheel {

    @Override
    public String getType() {
        return "rebel";
    }

    @Override public boolean canLock()   { return false; }
    @Override public boolean canSwap()   { return false; }
    @Override public boolean canDelete() { return false; }
}
