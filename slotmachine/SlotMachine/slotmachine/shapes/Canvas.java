package shapes;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example.
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 *
 * @version: 4.0 (slotmachine)
 */
public class Canvas{
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    // Tamaño del lienzo: 8 ruedas por fila y hasta 7 filas (50 ruedas)
    public static final int WIDTH = 700;
    public static final int HEIGHT = 800;

    private static final Map<String, Color> COLORS = new HashMap<String, Color>();
    static {
        COLORS.put("red", Color.red);
        COLORS.put("black", Color.black);
        COLORS.put("blue", Color.blue);
        COLORS.put("yellow", Color.yellow);
        COLORS.put("green", Color.green);
        COLORS.put("magenta", Color.magenta);
        COLORS.put("white", Color.white);
        COLORS.put("orange", Color.orange);
        COLORS.put("pink", Color.pink);
        COLORS.put("cyan", Color.cyan);
        COLORS.put("purple", new Color(126, 87, 194));
        COLORS.put("brown", new Color(139, 90, 43));
        COLORS.put("gray", Color.gray);
        COLORS.put("lightgray", Color.lightGray);
        COLORS.put("darkgray", Color.darkGray);
        COLORS.put("lightblue", new Color(173, 216, 230));
        COLORS.put("lightgreen", new Color(144, 238, 144));
    }

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas(){
        if(canvasSingleton == null) {
            canvasSingleton = new Canvas("SlotMachine", WIDTH, HEIGHT, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    //  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List <Object> objects;
    private HashMap <Object,ShapeDescription> shapes;

    /**
     * Create a Canvas.
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * @param bgColour  the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColour){
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList <Object>();
        shapes = new HashMap <Object,ShapeDescription>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false)
     */
    public void setVisible(boolean visible){
        if(graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     * @param  referenceObject  an object to define identity for this shape
     * @param  color            the color of the shape
     * @param  shape            the shape object to be drawn on the canvas
     */
    public void draw(Object referenceObject, String color, Shape shape){
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Erase a given shape's from the screen.
     * @param  referenceObject  the shape object to be erased
     */
    public void erase(Object referenceObject){
        objects.remove(referenceObject);   // just in case it was already there
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * Acepta nombres conocidos (sin importar mayusculas) o codigos "#rrggbb".
     * Un color desconocido se pinta en negro.
     * @param  colorString   the new colour for the foreground of the Canvas
     */
    public void setForegroundColor(String colorString){
        graphic.setColor(toColor(colorString));
    }

    /**
     * Traduce el nombre de un color a un objeto Color.
     */
    private static Color toColor(String colorString){
        if (colorString == null) {
            return Color.black;
        }
        Color known = COLORS.get(colorString.toLowerCase());
        if (known != null) {
            return known;
        }
        if (colorString.startsWith("#")) {
            try {
                return Color.decode(colorString);
            } catch (NumberFormatException e) {
                return Color.black;
            }
        }
        return Color.black;
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number
     */
    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Redraw all shapes currently on the Canvas.
     */
    private void redraw(){
        erase();
        for (Object reference : objects) {
            shapes.get(reference).draw(graphic);
        }
        canvas.repaint();
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }


    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel{
        public void paint(Graphics g){
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /************************************************************************
     * Inner class ShapeDescription - guarda la figura y su color para
     * poder redibujarla.
     */
    private class ShapeDescription{
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color){
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic){
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}
