package shapes;

/**
 * Crea las figuras y contiene los atributos y métodos necesarios
 * para crear a Circle, Rectangle y Triangle.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public abstract class Shapes{
    // ATRIBUTOS

    public int xPosition;
    public int yPosition;
    public String color;
    public boolean isVisible;

    /**
     * Constructor for objects of class Shapes
     */
    public Shapes(){
        this.xPosition = 0;
        this.yPosition = 0;
        this.color = "blue";
        this.isVisible = false;
    }

    //METODOS

    /**
     * Make this shape visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }

    /**
     * Make this shape invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    /**
     * Move the shape a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the shape a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the shape a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the shape a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the shape horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the shape vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Ubica la figura directamente en las coordenadas indicadas
     * (un solo borrado y un solo dibujo).
     * @param x nueva coordenada X
     * @param y nueva coordenada Y
     */
    public void moveTo(int x, int y){
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /**
     * Slowly move the shape horizontally.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance){
        int delta;
        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }
        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the shape vertically
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;
        if(distance < 0) {
            delta = -1;
            distance = -distance;
        }else {
            delta = 1;
        }
        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the color.
     * @param newColor the new color (nombre conocido por Canvas o "#rrggbb").
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    /**
     * @return tamaño representativo de la figura en pixeles.
     */
    public abstract int getSize();

    /**
     * Cambia el tamaño representativo de la figura.
     * @param size nuevo tamaño en pixeles (>= 0).
     */
    public abstract void setSize(int size);

    // Metodos que cada figura dibuja de forma distinta
    protected abstract void draw();
    protected abstract void erase();
}
