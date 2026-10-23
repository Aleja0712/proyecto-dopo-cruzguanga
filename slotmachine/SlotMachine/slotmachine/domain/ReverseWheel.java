package domain;

/**
 * Rueda invertida (tipo propuesto, requisito 19): gira en sentido contrario.
 * spin(k) sobre esta rueda equivale a spin(-k) sobre una rueda normal.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class ReverseWheel extends Wheel {

    @Override
    public String getType() {
        return "reverse";
    }

    /**
     * Invierte el sentido del giro.
     */
    @Override
    public int effectiveSteps(int steps) {
        return -steps;
    }
}
