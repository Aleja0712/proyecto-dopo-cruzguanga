import java.util.ArrayList;
import java.util.LinkedHashSet;
import javax.swing.JOptionPane;

/**
 * Simula una máquina tragamonedas compuesta por una o más ruedas
 * 
 * Invariante de clase:
 * - wheels != null
 * - machine != null
 * 
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 2.0
 */

// prueba


public class SlotMachine {
    private ArrayList<Wheel> wheels;
    private boolean isvisible;
    private boolean lastOperationOk;

    /**
     * Constructor del simulador
     */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isvisible = false;
        this.lastOperationOk = true;
    }

    /**
     * Metodo auxiliar para verificar que existen simbolos en las ruedas
     * @return true si hay al menos un simbolo en alguna rueda, false si no.
     */
    private boolean notEmpty() {
        for (Wheel w : wheels) {
            if (w.getSymbols().size() > 0) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Ajusta la posicion ingresada para que este en un rango valido
     * @param max limite maximo permitido.
     * @return posicion ajustada.
     */
    private int clamp(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    /**
     * Registra que la operacion fue exitosa
     */
    private void succeed() {
        this.lastOperationOk = true;
    }

    /**
     * Registra el fallo de la operacion y muestra alerta si esta visible
     * @param message mensaje de error.
     */
    private void fail(String message) {
        this.lastOperationOk = false;
        if (this.isvisible) {
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Indica si la ultima operacion fue exitosa
     * @return true si funciono, false si fallo.
     */
    public boolean ok() {
        return this.lastOperationOk;
    }

    /**
     * Adiciona una rueda en la posicion indicada
     * @param pos posicion de la rueda.
     */
    public void addWheel(int pos) {
        int adjustedPos = (wheels.size() == 0) ? 1 : clamp(pos, wheels.size() + 1);
        wheels.add(adjustedPos - 1, new Wheel(adjustedPos));
        succeed();
    }

    /**
     * Elimina una rueda de la maquina
     * @param position posicion de la rueda.
     */
    public void delWheel(int position) {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return;
        }
        int adjustedPos = clamp(position, wheels.size());
        wheels.remove(adjustedPos - 1);
        succeed();
    }

    /**
     * Adiciona un simbolo a una rueda especifica
     * @param pos posicion de la rueda.
     * @param color color del simbolo.
     */
    public void addSymbol(int pos, String color) {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return;
        }
        int adjustedPos = clamp(pos, wheels.size());
        Wheel w = wheels.get(adjustedPos - 1);
        w.addSymbol(new Symbol(color, color, "circle"));
        succeed();
    }
    
      /**
     * Elimina un simbolo especifico de todas las ruedas en la maquina
     * @param symbol color del simbolo a eliminar.
     */
    public void delSymbol(String symbol) {
        boolean removed = false;
        for (Wheel w : wheels) {
            int sizeBefore = w.getSymbols().size();
            w.delSymbol(symbol);
            if (w.getSymbols().size() < sizeBefore) {
                removed = true;
            }
        }
        
        if (removed) {
            succeed();
        } else {
            fail("No se encontro el simbolo.");
        }
    }

    /**
     * Adiciona un simbolo a una rueda especifica en la maquina
     * @param wheel posicion de la rueda.
     * @param symbol color del simbolo a adicionar.
     */
    public void placeSymbol(int wheel, String symbol) {
        if (wheels.size() == 0) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        
        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        w.addSymbol(new Symbol(symbol, symbol, "circle")); 
        succeed();
    }

    /**
     * Gira la rueda especificada al azar
     * @param wheel posición de la rueda a girar.
     */
    public void spin(int wheel) {
        if (wheels.size() == 0) {
            fail("No hay ruedas en la maquina.");
            return;
        }

        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);

        if (w.isLocked()) {
            fail("La rueda indicada esta bloqueada.");
            return;
        }
        
        if (w.getSymbols().size() == 0) {
            fail("La rueda indicada no tiene simbolos.");
            return;
        }

        w.spin();
        succeed();
    }
    
    /**
     * Gira la rueda especificada una cantidad de pasos
     * @param wheel posicion de la rueda a rotar.
     * @param steps cantidad de pasos.
     */
    public void spin(int wheel, int steps) {
        if (wheels.size() == 0 || steps < 0) {
            fail("Parametros invalidos.");
            return;
        }
        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        
        if (w.isLocked() || w.getSymbols().size() == 0) {
            fail("Rueda invalida.");
            return;
        }
        
        for(int i = 0; i < steps; i++) {
             w.spin();
        }
        succeed();
    }
    
        /**
     * Gira las ruedas y deja la maquina en una configuracion especifica
     * @param setSymbols arreglo con los colores.
     */
    public void spin(String[] setSymbols) {
        if (wheels.size() == 0 || wheels.size() != setSymbols.length) {
            fail("Ruedas invalidas.");
            return;
        }
        
        for (int i = 0; i < wheels.size(); i++) {
            Wheel w = wheels.get(i);
            if (!w.isLocked() && w.getSymbols().size() > 0) {
                String targetColor = setSymbols[i];
                boolean hasColor = false;
                
                for (Symbol s : w.getSymbols()) {
                    if (s.getColor().equals(targetColor)) {
                        hasColor = true;
                        break;
                    }
                }
                
                if (hasColor) {
                    while(w.getCurrentSymbol() != null && !w.getCurrentSymbol().getColor().equals(targetColor)){
                        w.spin();
                    }
                }
            }
        }
        succeed();
    }

    /**
     * Gira todas las ruedas de la maquina que no esten fijas ni vacias
     */
    public void spin() {
        if (wheels.size() == 0) {
            fail("No hay ruedas en la maquina.");
            return;
        }

        if (!notEmpty()) {
            fail("Todas las ruedas estan vacias.");
            return;
        }

        boolean algunaGiro = false;
        
        for (Wheel w : wheels) {
            if (!w.isLocked() && w.getSymbols().size() > 0) {
                w.spin();
                algunaGiro = true;
            }
        }

        if (algunaGiro) {
            succeed();
        } else {
            fail("No se pudo girar ninguna rueda.");
        }
    }

    /**
     * Consulta los colores de todos los simbolos presentes en la maquina
     * @return arreglo con los colores.
     */
    public String[] symbols() {
        ArrayList<String> list = new ArrayList<>();
        for (Wheel w : wheels) {
            for (Symbol s : w.getSymbols()) {
                list.add(s.getColor()); 
            }
        }
        
        succeed();
        return list.toArray(new String[0]);
    }

    /**
     * Consulta la cantidad de simbolos diferentes en la maquina
     * @return cantidad de simbolos unicos.
     */
    public int distinctSymbols() {
        ArrayList<String> list = new ArrayList<>();
        int quantity = 0;
        
        for (Wheel w : wheels) {
            for (Symbol s : w.getSymbols()) {
                String color = s.getColor();
                if (!list.contains(color)) {
                    quantity++;
                    list.add(color);
                }
            }
        }
        
        succeed();
        return quantity;
    }

    /**
     * Retorna los colores de los simbolos visibles actualmente en todas las ruedas
     * @return arreglo con la configuracion.
     */
    public String[] configuration() {
        ArrayList<String> list = new ArrayList<>();
        for (Wheel w : wheels) {
            Symbol current = w.getCurrentSymbol();
            if (current != null) {
                list.add(current.getColor());
            } else {
                list.add("empty");
            }
        }
        
        succeed();
        return list.toArray(new String[0]);
    }
    
       /**
     * Verifica si la configuracion actual de las ruedas es ganadora
     * @return true si es ganadora, false de lo contrario.
     */
    public boolean isJackpot() {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return false;
        }
        
        boolean value = false;
        ArrayList<String> list = new ArrayList<>();
        
        for (Wheel w : wheels) {
            Symbol current = w.getCurrentSymbol();
            if (current != null) {
                list.add(current.getColor());
            } else {
                fail("Rueda vacia.");
                return false; 
            }
        }
        
        if (list.size() > 0) {
            boolean allEqual = true;
            String firstColor = list.get(0);
            for (String color : list) {
                if (!firstColor.equals(color)) {
                    allEqual = false;
                    break;
                }
            }
            if (allEqual) {
                value = true;
            }
        }
        
        succeed();
        return value;
    }

    /**
     * Hace visible el simulador y todas sus ruedas en pantalla
     * 
     */
    public void makeVisible() {
        for (Wheel w : wheels) {
            w.makeVisible();
        }
        isvisible = true;
        succeed();
    }

    /**
     * Hace invisible el simulador y todas sus ruedas en pantalla
     */
    public void makeInvisible() {
        for (Wheel w : wheels) {
            w.makeInvisible();
        }
        isvisible = false;
        succeed();
    }

    /**
     * Termina la ejecucion del simulador de la maquina tragamonedas
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Intercambia la posicion de dos ruedas dentro de la maquina
     * @param wheel1 posicion de la primera rueda.
     * @param wheel2 posicion de la segunda rueda.
     */
    public void swap(int wheel1, int wheel2) {
        if (wheels.size() >= 2 && wheel1 != wheel2) {
            int p1 = clamp(wheel1, wheels.size());
            int p2 = clamp(wheel2, wheels.size());
            
            if (p1 != p2) {
                Wheel temp = wheels.get(p1 - 1);
                wheels.set(p1 - 1, wheels.get(p2 - 1));
                wheels.set(p2 - 1, temp);
                succeed();
            } else {
                fail("Posiciones iguales.");
            }
        } else {
            fail("Ruedas insuficientes.");
        }
    }

    /**
     * Fija una rueda especifica para que no pueda girar
     * @param wheel posicion de la rueda a fijar.
     */
    public void lock(int wheel) {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return;
        }

        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        boolean locked = w.isLocked();

        if (!locked) {
            w.lock(); 
            succeed();
        } else {
            fail("Ya esta bloqueada.");
        }
    }

    /**
     * Desbloquea una rueda especifica que estaba fija
     * @param wheel posicion de la rueda a desbloquear.
     */
    public void unlock(int wheel) {
        if (wheels.size() == 0) {
            fail("No hay ruedas.");
            return;
        }
    
        int p = clamp(wheel, wheels.size());
        Wheel w = wheels.get(p - 1);
        boolean locked = w.isLocked();
  
        if (locked) {
            w.unlock();
            succeed();
        } else {
            fail("No esta bloqueada.");
        }
    }
}