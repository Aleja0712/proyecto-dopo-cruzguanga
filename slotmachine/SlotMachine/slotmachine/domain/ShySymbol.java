package domain;

/**
 * Simbolo timido: alterna entre visible e invisible cada vez que
 * es seleccionado en la rueda. Aunque este oculto sigue contando
 * en la configuracion de la maquina.
 *
 * @author Mary Alejandra Guanga García - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class ShySymbol extends Symbol {
    private boolean concealed;

    /**
     * Crea un simbolo timido, inicialmente visible.
     * @param name identificador del simbolo.
     * @param color color del simbolo.
     */
    public ShySymbol(String name, String color) {
        super(name, color);
        this.concealed = false;
    }

    @Override
    public String getType() {
        return "shy";
    }

    @Override
    public boolean isConcealed() {
        return concealed;
    }

    /**
     * Alterna su visibilidad.
     */
    @Override
    public void selected() {
        concealed = !concealed;
    }
}
