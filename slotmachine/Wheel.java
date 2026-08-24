import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * La clase Wheel representa cada una de las ruedas que forman parte de la máquina tragamonedas.
 * 
 * @Mary Alejandra Guanga García
 * @Samuel Esteban Cruz Rodriguez
 */
public class Wheel {
    private List<Symbol> symbols;
    private Symbol currentSymbol;
    private int position;

    public Wheel(int position) {
        this.position = position;
        this.symbols = new ArrayList<>();
        this.currentSymbol = null;
    }

    public void addSymbol(Symbol symbol) {
        //Adiciona simbolos
        symbols.add(symbol);
    }

    public void removeSymbol(Symbol symbol) {
        //Remueve simbolos
        symbols.remove(symbol);
    }

    public void spin() {
        //Hace girar
        if (!symbols.isEmpty()) {
            Random rand = new Random();
            int index = rand.nextInt(symbols.size());
            currentSymbol = symbols.get(index);
        }
    }

    public List<Symbol> getSymbols() {
        //Obtiene los simbolos en forma de lista
        return symbols;
    }

    public Symbol getCurrentSymbol() {
        //Obtiene el simbolo actual
        return currentSymbol;
    }

    public void show() {
        //Muestra el simbolo
        if (currentSymbol != null) {
            currentSymbol.show();
        }
    }

    public void hide() {
        //Esconde el simbolo
        if (currentSymbol != null) {
            currentSymbol.hide();
        }
    }
}