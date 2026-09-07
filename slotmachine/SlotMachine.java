import java.util.List;
import java.util.ArrayList;
/**
 * La clase SlotMachine representa la máquina tragamonedas completa y funciona como la clase principal del sistema. 
 * Su responsabilidad es coordinar y administrar las diferentes ruedas 
 * que hacen parte de la máquina, además de controlar las operaciones generales que puede realizar el usuario.
 * 
 * @Mary Alejandra Guanga García
 * @Samuel Esteban Cruz Rodriguez
 */

// prueba


public class SlotMachine {
    private List<Wheel> wheels;
    private boolean isvisible;

    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isvisible = false;
    }

    public void addWheel(int pos) {
        Wheel newWheel = new Wheel(pos);
        // El diagrama muestra que se añade un símbolo tras crear la rueda
        newWheel.addSymbol(new Symbol("Default", "red", "circle")); 
        wheels.add(newWheel);
        makeVisible();
    }

    public void delWheel(int position) {
        // El diagrama borra rueda
    }

    public void addSymbol(int pos, String color) {
        //El diagrama añade simbolos
    }

    public void delSymbol(String symbol) {
        // Lógica para eliminar un símbolo por su nombre
    }

    public void placeSymbol(String symbol) {
        // Lógica para ubicar un símbolo 
    }

    public void spin(int wheelIndex) {
    }

    public void spin() {
    }

    public String[] symbols() {
        // Retorna los nombres de los símbolos actuales
        return new String[0]; 
    }

    public int distintSymbols() {
        return 0; // Lógica para contar símbolos distintos
    }

    public String[] configuration() {
        return new String[0];
    }

    public boolean isJackpot() {
        // Lógica para saber si alguien obtiene el premio
        return false;
    }

    public void makeVisible() {
        //Convierte algo en visible
        }
    }

    public void makeInvisible() {
        //Convierte algo en invisible
        this.isvisible = false;
        for(Wheel w : wheels) {
            w.hide();
        }
    }

    public void exit() {
        System.exit(0);
    }

    public boolean ok() {
        return true; // Lógica para confirmar estado correcto
    }
}