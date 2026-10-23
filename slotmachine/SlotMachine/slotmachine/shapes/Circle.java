package shapes;
import java.awt.geom.Ellipse2D;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 4.0
 */
public class Circle extends Shapes{

    public static final double PI = 3.1416;
    private int diameter;

    /**
     * Create a new circle at default position with default color.
     */
    public Circle(){
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }

    /**
     * @return el diametro del circulo.
     */
    @Override
    public int getSize() {
        return diameter;
    }

    /**
     * Cambia el diametro del circulo.
     */
    @Override
    public void setSize(int size) {
        changeSize(size);
    }

    /*
     * Draw the circle with current specifications on screen.
     */
    @Override
    protected void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
            canvas.wait(10);
        }
    }

    /*
     * Erase the circle on screen.
     */
    @Override
    protected void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
