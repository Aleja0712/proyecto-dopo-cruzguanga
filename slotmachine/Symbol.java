
/**
 * La clase Symbol representa cada uno de los símbolos que pueden aparecer dentro de una rueda. 
 * Es decir, es la clase que permite modelar aquello que el jugador realmente observa en la máquina, 
 * como una figura de determinado color, tamaño o forma. 
 * 
 * @Mary Alejandra Guanga García
 * @Samuel Esteban Cruz Rodriguez
 */
public class Symbol {
    private String name;
    private String color;
    private String figure;

    public Symbol(String name, String color, String figure) {
        this.name = name;
        this.color = color;
        this.figure = figure;
    }

    public void show() {
        // Lógica base para mostrar
    }

    public void hide() {
        // Lógica base para ocultar
    }

    public void changeColor(String color) {
        this.color = color;
    }


    public String getName() { return name; }
    public String getColor() { return color; }
    public String getFigure() { return figure; }
}