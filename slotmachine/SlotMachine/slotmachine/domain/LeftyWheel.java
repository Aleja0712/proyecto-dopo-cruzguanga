package domain;

/**
 * Rueda zurda: si hay una rueda a su izquierda, al girar copia su estado
 * (deja a la vista el mismo simbolo, si lo tiene).
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class LeftyWheel extends Wheel {

    @Override
    public String getType() {
        return "lefty";
    }

    /**
     * Copia el simbolo a la vista de la rueda izquierda. Si no lo tiene,
     * se queda donde quedo al girar.
     */
    @Override
    public void copyLeft(Wheel left) {
        if (left == null || left.getCurrentSymbol() == null) {
            return;
        }
        place(left.getCurrentSymbol().getColor());
    }
}
