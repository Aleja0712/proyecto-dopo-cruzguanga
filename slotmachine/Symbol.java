
/**
 * La clase Symbol representa cada uno de los símbolos que pueden aparecer dentro de una rueda. 
 * Es decir, es la clase que permite modelar aquello que el jugador realmente observa en la máquina, 
 * como una figura de determinado color, tamaño o forma. 
 * 
 * @Mary Alejandra Guanga García
 * @Samuel Esteban Cruz Rodriguez
 */
public class Symbol {
    private String color;
    private Circle shape;
    private boolean isVisible;

    // Este constructor debe existir en Symbol.java
    public Symbol(String color) {
        this.color = color;
        this.isVisible = false;
        this.shape = new Circle();
        this.shape.changeColor(color);
    }

    public String getColor() {
        return color;
    }

    public void show() {
        shape.makeVisible();
        isVisible = true;
    }

    public void hide() {
        if (isVisible) {
            shape.makeInvisible();
            isVisible = false;
        }
    }

    public void showAt(int x, int y, int size) {
        shape.changeSize(size);
        shape.moveTo(x, y);
        shape.makeVisible();
        isVisible = true;
    }
}