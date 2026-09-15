
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
    private boolean isVisible;
    private Shapes shape;
    

    /**
     * Constructor para inicializar los datos base del simbolo.
     * @param name identificador del simbolo.
     * @param color color en formato CSS
     * @param figure tipo de figura asignada.
     */
    public Symbol(String name, String color, String figure) {
        this.name = name;
        this.color = color;
        this.figure = figure;
        this.shape = createShape(figure);
        if (this.shape != null) {
            this.shape.changeColor(color);
        }
        this.isVisible = false;
    }
    
    /**
     * Genera una figura a partir de su nombre (círculo, rectángulo o triángulo)
     * @param figure nombre de la figura solicitada.
     * @return figura creada; Circle por defecto.
     */
    
    private static Shapes createShape(String shape) {
        if (shape == null) {
            return new Circle();
        }
        String kind = shape.toLowerCase();
        if (kind.equals("rectangle")) {
            return new Rectangle();
        }
        if (kind.equals("triangle")) {
            return new Triangle();
        }
        return new Circle();
    }

    /**
     * Retorna el color actual del simbolo
     * @return color en formato de texto.
     */
    public String getColor() {
        return this.color;
    }
    
    /**
     * Retorna la figura geometrica asociada simbolo
     */
    
    public Shapes getShape() {
        return this.shape;
    }

    /**
     * Muestra el simbolo delegando a la visibilidad grafica
     */
    public void show() {
        shape.makeVisible();
        this.isVisible=true;
    }

    /**
     * Oculta el simbolo delegando a la invisibilidad grafica
     */
    public void hide() {
        shape.makeInvisible();
        this.isVisible=false;
    }

    /**
     * Hace visible la figura en la pantalla
     */
    public void makeVisible() {
        show();
    }

    /**
     * Oculta la figura de la pantalla
     */
    public void makeInvisible() {
        hide();
    }

    /**
     * Cambia el color del simbolo
     * @param color nuevo color en estandar CSS
     */
    public void changeColor(String color) {
        this.color = color;
    }
}