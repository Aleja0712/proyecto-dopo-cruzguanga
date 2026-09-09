import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * la clase wheel representa cada una de las ruedas que forman parte de la maquina tragamonedas[cite: 1, 3]
 * 
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 2.0
 */
public class Wheel {
    private List<Symbol> symbols;
    private Symbol currentSymbol;
    private int position;
    private boolean locked;
    private int x;
    private int y;

    public Wheel() {
        this.position = 1;
        this.symbols = new ArrayList<>();
        this.currentSymbol = null;
        this.locked = false;
    }

    public Wheel(int position) {
        this.position = position;
        this.symbols = new ArrayList<>();
        this.currentSymbol = null;
        this.locked = false;
    }

    public void addSymbol(int pos, String color) {
        Symbol s = new Symbol(color);
        addSymbol(s);
    }

    public void addSymbol(int pos, String color) {
        Symbol s = new Symbol(color);
        int idx = Math.max(0, Math.min(pos - 1, symbols.size()));
        this.symbols.add(idx, s);
        if (this.currentSymbol == null) {
            this.currentSymbol = s;
        }
    }

    public void removeSymbol(Symbol symbol) {
        if (symbol != null) {
            symbol.hide();
            symbols.remove(symbol);
            if (currentSymbol == symbol) {
                currentSymbol = symbols.isEmpty() ? null : symbols.get(0);
            }
        }
    }

    public boolean delSymbol(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            Symbol s = symbols.get(i);
            if (s.getColor().equalsIgnoreCase(color)) {
                s.hide();
                symbols.remove(i);
                if (currentSymbol == s) {
                    currentSymbol = symbols.isEmpty() ? null : symbols.get(0);
                }
                return true;
            }
        }
        return false;
    }

    public void spin() {
        if (!symbols.isEmpty() && !locked) {
            Random rand = new Random();
            int index = rand.nextInt(symbols.size());
            currentSymbol = symbols.get(index);
        }
    }

    public void rotate(int direction) {
        if (symbols.isEmpty() || locked) return;
        int idx = symbols.indexOf(currentSymbol);
        if (idx == -1) idx = 0;
        idx = (idx + direction) % symbols.size();
        if (idx < 0) {
            idx += symbols.size();
        }
        currentSymbol = symbols.get(idx);
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public Symbol getCurrentSymbol() {
        return currentSymbol;
    }

    public String currentColor() {
        if (currentSymbol != null) {
            return currentSymbol.getColor();
        }
        return null;
    }

    public boolean setCurrentByColor(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(color)) {
                this.currentSymbol = s;
                return true;
            }
        }
        return false;
    }

    public boolean contains(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(color)) {
                return true;
            }
        }
        return false;
    }

    public String[] allColors() {
        String[] colors = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            colors[i] = symbols.get(i).getColor();
        }
        return colors;
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public void swap(Wheel other) {
        if (other != null && !this.locked && !other.locked) {
            List<Symbol> tempSymbols = this.symbols;
            Symbol tempCurrent = this.currentSymbol;

            this.symbols = other.symbols;
            this.currentSymbol = other.currentSymbol;

            other.symbols = tempSymbols;
            other.currentSymbol = tempCurrent;
        }
    }

    public int size() {
        return symbols.size();
    }

    public boolean isEmpty() {
        return symbols.isEmpty();
    }

    public void changePositionX(int newPosX) {
        this.x = newPosX;
    }

    public void changePositionY(int newPosY) {
        this.y = newPosY;
    }

    public void show() {
        if (currentSymbol != null) {
            currentSymbol.showAt(this.x, this.y, 30);
        }
    }

    public void showAt(int x, int y, int size) {
        this.x = x;
        this.y = y;
        hide();
        if (currentSymbol != null) {
            currentSymbol.showAt(x, y, size);
        }
    }

    public void hide() {
        for (Symbol s : symbols) {
            s.hide();
        }
    }
}