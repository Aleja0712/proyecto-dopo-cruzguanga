
/**
 * La clase Symbol representa cada uno de los símbolos que pueden aparecer dentro de una rueda. 
 * Es decir, es la clase que permite modelar aquello que el jugador realmente observa en la máquina, 
 * como una figura de determinado color, tamaño o forma. 
 * 
 * @Mary Alejandra Guanga García
 * @Samuel Esteban Cruz Rodriguez
 */
public class Symbol {
    protected String name;
    protected String color;
    protected String figure;
    protected boolean isVisible;

    /**
     * Constructor para inicializar los datos base del simbolo.
     * @param name identificador del simbolo.
     * @param color color en formato CSS[span_6](start_span)[span_6](end_span).
     * @param figure tipo de figura asignada.
     */
    public Symbol(String name, String color, String figure) {
        this.name = name;
        this.color = color;
        this.figure = figure;
        this.isVisible = false;
    }

    /**
     * Retorna el color actual del simbolo[span_7](start_span)[span_7](end_span).
     * @return color en formato de texto.
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Muestra el simbolo delegando a la visibilidad grafica[span_8](start_span)[span_8](end_span).
     */
    public void show() {
        makeVisible();
    }

    /**
     * Oculta el simbolo delegando a la invisibilidad grafica[span_9](start_span)[span_9](end_span).
     */
    public void hide() {
        makeInvisible();
    }

    /**
     * Hace visible la figura en la pantalla[span_10](start_span)[span_10](end_span).
     */
    public void makeVisible() {
        this.isVisible = true;
    }

    /**
     * Oculta la figura de la pantalla[span_11](start_span)[span_11](end_span).
     */
    public void makeInvisible() {
        this.isVisible = false;
    }

    /**
     * Cambia el color del simbolo[span_12](start_span)[span_12](end_span).
     * @param color nuevo color en estandar CSS[span_13](start_span)[span_13](end_span).
     */
    public void changeColor(String color) {
        this.color = color;
    }
}