package presentation;
import domain.*;
import shapes.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Simula una máquina tragamonedas compuesta por una o más ruedas.
 * Es la fachada del simulador: delega la lógica al paquete domain y
 * dibuja el estado usando las figuras del paquete shapes.
 *
 * Las ruedas se dibujan en una matriz de máximo 8 ruedas por fila
 * (ver calculateWheelPosition).
 *
 * Invariante de clase: eje != null; cada rueda y cada simbolo visibles
 * tienen exactamente una figura asociada.
 *
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class SlotMachine {
    // Layout de ruedas: matriz de 8 por fila
    private static final int MAX_WHEELS_PER_ROW = 8;
    private static final int START_X = 50;
    private static final int START_Y = 100;
    private static final int SPACING_X = 70;
    private static final int SPACING_Y = 90;

    // Dimensiones de cada rueda y de la carcasa
    private static final int WHEEL_WIDTH = 60;
    private static final int WHEEL_HEIGHT = 80;
    private static final int FRAME_PADDING = 10;
    private static final int BASE_HEIGHT = 20;
    private static final int ANIMATION_DELAY = 80;
    private static final String FRAME_COLOR = "purple";
    private static final String JACKPOT_COLOR = "yellow";

    // Colores que distinguen visualmente los tipos de rueda
    private static final Map<String, String> WHEEL_COLORS = new HashMap<String, String>();
    static {
        WHEEL_COLORS.put("normal", "lightGray");
        WHEEL_COLORS.put("lefty", "lightBlue");
        WHEEL_COLORS.put("rebel", "darkGray");
        WHEEL_COLORS.put("reverse", "lightGreen");
    }

    // Paleta para los n simbolos de SlotMachine(n)
    private static final String[] PALETTE = {"red", "yellow", "blue", "green", "magenta",
        "orange", "pink", "cyan", "purple", "brown", "black", "gray"};

    private final Eje eje;
    private final Rectangle machineFrame;   // carcasa exterior de la maquina
    private final Rectangle machineBase;
    private final Map<Wheel, Rectangle> bodies;
    private final Map<Symbol, Shapes> figures;
    private boolean isVisible;
    private boolean lastOperationOk;
    private boolean jackpotShown;

    /**
     * Constructor del simulador: maquina vacia e invisible.
     */
    public SlotMachine() {
        this.eje = new Eje();
        this.bodies = new HashMap<Wheel, Rectangle>();
        this.figures = new HashMap<Symbol, Shapes>();
        this.isVisible = false;
        this.lastOperationOk = true;

        this.machineFrame = new Rectangle();
        this.machineFrame.changeColor(FRAME_COLOR);
        this.machineBase = new Rectangle();
        this.machineBase.changeColor("gray");
        updateMachineFrame();
    }

    /**
     * Constructor para el concurso: crea una maquina de n ruedas y n simbolos
     * (los mismos en el mismo orden) con una configuracion inicial aleatoria.
     * @param n numero de ruedas y de simbolos (3 <= n <= 50).
     */
    public SlotMachine(int n) {
        this();
        n = Math.max(3, Math.min(50, n));
        for (int i = 1; i <= n; i++) {
            eje.addWheel(new Wheel(), i);
        }
        for (int s = 0; s < n; s++) {
            String color = colorFor(s, n);
            for (Wheel w : eje.getWheels()) {
                w.addSymbol(new Symbol(color, color));
            }
        }
        Random random = new Random();
        for (Wheel w : eje.getWheels()) {
            w.spin(random.nextInt(n));
        }
        if (countDistinct() == 1) {
            eje.getWheel(1).spin(1);
        }
        updateMachineFrame();
    }

    // ===================== LAYOUT =====================

    /**
     * Calcula las coordenadas de dibujo de la rueda con el indice dado.
     * Maximo 8 ruedas por fila: la rueda 9 (indice 8) vuelve a la primera
     * columna en una nueva fila.
     * @param index indice de la rueda (0-based).
     * @return {x, y} de la esquina superior izquierda de la rueda.
     */
    public int[] calculateWheelPosition(int index) {
        int row = index / MAX_WHEELS_PER_ROW;
        int col = index % MAX_WHEELS_PER_ROW;

        int x = START_X + (col * SPACING_X);
        int y = START_Y + (row * SPACING_Y);

        return new int[]{x, y};
    }

    /**
     * @param pos posicion de la rueda (1-based).
     * @return coordenada X donde se dibuja la rueda.
     */
    public int getWheelX(int pos) {
        return calculateWheelPosition(Eje.clamp(pos, eje.getWheelCount()) - 1)[0];
    }

    /**
     * @param pos posicion de la rueda (1-based).
     * @return coordenada Y donde se dibuja la rueda.
     */
    public int getWheelY(int pos) {
        return calculateWheelPosition(Eje.clamp(pos, eje.getWheelCount()) - 1)[1];
    }

    // ===================== RUEDAS =====================

    /**
     * Añade una rueda normal.
     * @param pos posicion de la rueda.
     */
    public void addWheel(int pos) {
        addWheel("normal", pos);
    }

    /**
     * Añade una rueda del tipo indicado.
     * @param type "normal", "lefty", "rebel" o "reverse".
     * @param pos posicion de la rueda.
     */
    public void addWheel(String type, int pos) {
        Wheel w = Wheel.create(type);
        if (w == null) {
            fail("Tipo de rueda desconocido: " + type + ".");
            return;
        }
        eje.addWheel(w, pos);
        updateMachineFrame();
        drawAllWheels();
        succeed();
    }

    /**
     * Elimina una rueda (si no esta fija y su tipo lo permite).
     * @param pos posicion de la rueda.
     */
    public void delWheel(int pos) {
        Wheel w = eje.delWheel(pos);
        if (w == null) {
            fail("No se pudo eliminar la rueda.");
            return;
        }
        forgetWheel(w);
        updateMachineFrame();
        drawAllWheels();
        succeed();
    }

    /**
     * Intercambia la posicion de dos ruedas dentro de la maquina.
     * @param wheel1 posicion de la primera rueda.
     * @param wheel2 posicion de la segunda rueda.
     */
    public void swap(int wheel1, int wheel2) {
        if (eje.getWheelCount() < 2) {
            fail("Se necesitan al menos dos ruedas.");
            return;
        }
        int p1 = Eje.clamp(wheel1, eje.getWheelCount());
        int p2 = Eje.clamp(wheel2, eje.getWheelCount());
        if (p1 == p2) {
            fail("Las posiciones son iguales.");
            return;
        }
        Wheel w1 = eje.getWheel(p1);
        Wheel w2 = eje.getWheel(p2);
        if (w1.isLocked() || w2.isLocked()) {
            fail("Una de las ruedas esta fija.");
            return;
        }
        if (!w1.canSwap() || !w2.canSwap()) {
            fail("Una de las ruedas es rebelde y no se deja intercambiar.");
            return;
        }
        eje.swap(p1, p2);
        drawWheel(p1 - 1);
        drawWheel(p2 - 1);
        succeed();
    }

    /**
     * Fija una rueda especifica para que no pueda girar.
     * @param wheel posicion de la rueda a fijar.
     */
    public void lock(int wheel) {
        if (eje.isEmpty()) {
            fail("No hay ruedas.");
            return;
        }
        Wheel w = eje.getWheel(wheel);
        if (!w.canLock()) {
            fail("La rueda es rebelde y no se deja bloquear.");
            return;
        }
        if (w.isLocked()) {
            fail("Ya esta bloqueada.");
            return;
        }
        w.lock();
        succeed();
    }

    /**
     * Desbloquea una rueda especifica que estaba fija.
     * @param wheel posicion de la rueda a desbloquear.
     */
    public void unlock(int wheel) {
        if (eje.isEmpty()) {
            fail("No hay ruedas.");
            return;
        }
        Wheel w = eje.getWheel(wheel);
        if (!w.isLocked()) {
            fail("No esta bloqueada.");
            return;
        }
        w.unlock();
        succeed();
    }

    /**
     * @param wheel posicion de la rueda.
     * @return color con el que se dibuja la rueda (depende de su tipo).
     */
    public String wheelColor(int wheel) {
        if (eje.isEmpty()) {
            return null;
        }
        return bodyColor(eje.getWheel(wheel));
    }

    // ===================== SIMBOLOS =====================

    /**
     * Adiciona un simbolo normal a una rueda especifica.
     * @param pos posicion de la rueda.
     * @param color color del simbolo.
     */
    public void addSymbol(int pos, String color) {
        addSymbol("normal", pos, color);
    }

    /**
     * Adiciona un simbolo del tipo indicado a una rueda especifica.
     * @param type "normal", "ephemeral" o "shy".
     * @param pos posicion de la rueda.
     * @param color color del simbolo.
     */
    public void addSymbol(String type, int pos, String color) {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        Wheel w = eje.getWheel(pos);
        if (w.contains(color)) {
            fail("La rueda ya contiene el simbolo " + color + ".");
            return;
        }
        Symbol s = Symbol.create(type, color);
        if (s == null) {
            fail("Tipo de simbolo desconocido: " + type + ".");
            return;
        }
        w.addSymbol(s);
        drawWheel(Eje.clamp(pos, eje.getWheelCount()) - 1);
        succeed();
    }

    /**
     * Elimina un simbolo especifico de todas las ruedas en la maquina.
     * @param symbol color del simbolo a eliminar.
     */
    public void delSymbol(String symbol) {
        boolean removed = false;
        for (Wheel w : eje.getWheels()) {
            for (Symbol s : w.getSymbols()) {
                if (s.getColor().equals(symbol)) {
                    forgetSymbol(s);
                }
            }
            removed |= w.delSymbol(symbol);
        }
        if (removed) {
            drawAllWheels();
            succeed();
        } else {
            fail("No se encontro el simbolo.");
        }
    }

    /**
     * Deja a la vista un simbolo que ya existe en la rueda.
     * @param wheel posicion de la rueda.
     * @param symbol color del simbolo.
     */
    public void placeSymbol(int wheel, String symbol) {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        Wheel w = eje.getWheel(wheel);
        if (w.isLocked()) {
            fail("La rueda esta fija.");
            return;
        }
        if (!w.place(symbol)) {
            fail("La rueda no contiene el simbolo " + symbol + ".");
            return;
        }
        drawWheel(Eje.clamp(wheel, eje.getWheelCount()) - 1);
        succeed();
    }

    /**
     * @param wheel posicion de la rueda.
     * @return tamaño del simbolo a la vista (0 si la rueda esta vacia).
     */
    public int symbolSize(int wheel) {
        Symbol s = currentOf(wheel);
        return (s == null) ? 0 : s.getSize();
    }

    /**
     * @param wheel posicion de la rueda.
     * @return true si el simbolo a la vista esta oculto.
     */
    public boolean isSymbolHidden(int wheel) {
        Symbol s = currentOf(wheel);
        return s != null && s.isConcealed();
    }

    // ===================== GIROS =====================

    /**
     * Gira la rueda especificada al azar.
     * @param wheel posición de la rueda a girar.
     */
    public void spin(int wheel) {
        if (!canSpin(wheel)) {
            return;
        }
        int p = Eje.clamp(wheel, eje.getWheelCount());
        eje.getWheel(p).spin();
        afterSpin(p);
        succeed();
    }

    /**
     * Gira la rueda especificada una cantidad de pasos.
     * @param wheel posicion de la rueda a girar.
     * @param steps cantidad de pasos (negativos giran hacia atras).
     */
    public void spin(int wheel, int steps) {
        if (!canSpin(wheel)) {
            return;
        }
        int p = Eje.clamp(wheel, eje.getWheelCount());
        animateSteps(p, steps);
        eje.getWheel(p).spin(steps);
        afterSpin(p);
        succeed();
    }

    /**
     * Gira las ruedas y deja la maquina en una configuracion especifica.
     * La operacion es atomica: si algo no se puede, no se mueve nada.
     * @param setSymbols arreglo con los colores, uno por rueda.
     */
    public void spin(String[] setSymbols) {
        int count = eje.getWheelCount();
        if (setSymbols == null || count == 0 || count != setSymbols.length) {
            fail("Configuracion invalida.");
            return;
        }
        for (int i = 0; i < count; i++) {
            Wheel w = eje.getWheel(i + 1);
            String target = setSymbols[i];
            if (!w.contains(target)) {
                fail("La rueda " + (i + 1) + " no tiene el simbolo " + target + ".");
                return;
            }
            Symbol current = w.getCurrentSymbol();
            boolean alreadyThere = current != null && current.getColor().equals(target);
            if (w.isLocked() && !alreadyThere) {
                fail("La rueda " + (i + 1) + " esta fija y tendria que moverse.");
                return;
            }
        }
        for (int i = 0; i < count; i++) {
            Wheel w = eje.getWheel(i + 1);
            if (!w.isLocked()) {
                w.place(setSymbols[i]);
            }
        }
        drawAllWheels();
        succeed();
    }

    /**
     * Gira todas las ruedas de la maquina que no esten fijas ni vacias.
     * Luego, de izquierda a derecha, cada rueda que giro aplica copyLeft.
     */
    public void spin() {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        List<Wheel> wheels = eje.getWheels();
        boolean[] spun = new boolean[wheels.size()];
        boolean any = false;
        for (int i = 0; i < wheels.size(); i++) {
            Wheel w = wheels.get(i);
            if (!w.isLocked() && !w.getSymbols().isEmpty()) {
                w.spin();
                spun[i] = true;
                any = true;
            }
        }
        if (!any) {
            fail("No se pudo girar ninguna rueda.");
            return;
        }
        for (int i = 0; i < wheels.size(); i++) {
            if (spun[i]) {
                wheels.get(i).copyLeft(eje.leftOf(i + 1));
            }
        }
        drawAllWheels();
        succeed();
    }

    // ===================== CONSULTAS =====================

    /**
     * Consulta los colores de todos los simbolos presentes en la maquina.
     * @return arreglo con los colores.
     */
    public String[] symbols() {
        ArrayList<String> list = new ArrayList<String>();
        for (Wheel w : eje.getWheels()) {
            for (Symbol s : w.getSymbols()) {
                list.add(s.getColor());
            }
        }
        succeed();
        return list.toArray(new String[0]);
    }

    /**
     * Consulta la cantidad de simbolos diferentes a la vista.
     * @return cantidad de simbolos distintos ("empty" cuenta como uno).
     */
    public int distinctSymbols() {
        succeed();
        return countDistinct();
    }

    /**
     * Retorna los colores de los simbolos a la vista en todas las ruedas.
     * Un simbolo timido oculto sigue contando.
     * @return arreglo con la configuracion ("empty" para ruedas vacias).
     */
    public String[] configuration() {
        String[] config = new String[eje.getWheelCount()];
        for (int i = 0; i < config.length; i++) {
            config[i] = colorAt(eje.getWheel(i + 1));
        }
        succeed();
        return config;
    }

    /**
     * Verifica si la configuracion actual es ganadora (todas iguales).
     * @return true si es ganadora, false de lo contrario.
     */
    public boolean isJackpot() {
        if (eje.isEmpty()) {
            fail("No hay ruedas.");
            return false;
        }
        for (Wheel w : eje.getWheels()) {
            if (w.getCurrentSymbol() == null) {
                fail("Rueda vacia.");
                return false;
            }
        }
        succeed();
        return countDistinct() == 1;
    }

    // ===================== VISIBILIDAD =====================

    /**
     * Hace visible la maquina.
     */
    public void makeVisible() {
        this.isVisible = true;
        machineFrame.makeVisible();
        machineBase.makeVisible();
        drawAllWheels();
    }

    /**
     * Hace invisible la maquina.
     */
    public void makeInvisible() {
        this.isVisible = false;
        for (Shapes f : figures.values()) {
            f.makeInvisible();
        }
        for (Rectangle b : bodies.values()) {
            b.makeInvisible();
        }
        machineBase.makeInvisible();
        machineFrame.makeInvisible();
    }

    /**
     * Termina la ejecucion del simulador.
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Indica si la ultima operacion fue exitosa.
     * @return true si funciono, false si fallo.
     */
    public boolean ok() {
        return this.lastOperationOk;
    }

    // ===================== AUXILIARES =====================

    /**
     * Valida que la rueda exista, no este fija y tenga simbolos.
     */
    private boolean canSpin(int wheel) {
        if (eje.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return false;
        }
        Wheel w = eje.getWheel(wheel);
        if (w.isLocked()) {
            fail("La rueda indicada esta fija.");
            return false;
        }
        if (w.getSymbols().isEmpty()) {
            fail("La rueda indicada no tiene simbolos.");
            return false;
        }
        return true;
    }

    /**
     * Despues de girar la rueda p: aplica copyLeft y la redibuja.
     */
    private void afterSpin(int p) {
        eje.getWheel(p).copyLeft(eje.leftOf(p));
        drawWheel(p - 1);
    }

    private Symbol currentOf(int wheel) {
        return eje.isEmpty() ? null : eje.getWheel(wheel).getCurrentSymbol();
    }

    private static String colorAt(Wheel w) {
        Symbol current = w.getCurrentSymbol();
        return (current == null) ? "empty" : current.getColor();
    }

    private int countDistinct() {
        LinkedHashSet<String> seen = new LinkedHashSet<String>();
        for (Wheel w : eje.getWheels()) {
            seen.add(colorAt(w));
        }
        return seen.size();
    }

    /**
     * Color del simbolo i de una maquina de n simbolos: nombres para n pequeños,
     * tonos distintos "#rrggbb" para n grandes.
     */
    private static String colorFor(int i, int n) {
        if (n <= PALETTE.length) {
            return PALETTE[i];
        }
        int rgb = java.awt.Color.HSBtoRGB((float) i / n, 0.85f, 0.9f) & 0xFFFFFF;
        return String.format("#%06x", rgb);
    }

    private static String bodyColor(Wheel w) {
        String color = WHEEL_COLORS.get(w.getType());
        return (color == null) ? "lightGray" : color;
    }

    /**
     * Ajusta tamaño y posicion de la carcasa segun las filas y columnas usadas.
     */
    private void updateMachineFrame() {
        int count = eje.getWheelCount();
        int cols = Math.max(1, Math.min(count, MAX_WHEELS_PER_ROW));
        int rows = Math.max(1, (count + MAX_WHEELS_PER_ROW - 1) / MAX_WHEELS_PER_ROW);
        int width = (cols - 1) * SPACING_X + WHEEL_WIDTH + 2 * FRAME_PADDING;
        int height = (rows - 1) * SPACING_Y + WHEEL_HEIGHT + 2 * FRAME_PADDING;
        int x = START_X - FRAME_PADDING;
        int y = START_Y - FRAME_PADDING;

        machineFrame.changeSize(height, width);
        machineFrame.moveTo(x, y);
        machineBase.changeSize(BASE_HEIGHT, width - 2 * FRAME_PADDING);
        machineBase.moveTo(x + FRAME_PADDING, y + height);
    }

    private void drawAllWheels() {
        for (int i = 0; i < eje.getWheelCount(); i++) {
            drawWheel(i);
        }
    }

    /**
     * Dibuja la rueda del indice dado (0-based) en su celda de la matriz,
     * con su simbolo a la vista centrado (los demas ocultos).
     */
    private void drawWheel(int index) {
        if (!isVisible) {
            return;
        }
        Wheel w = eje.getWheel(index + 1);
        int[] xy = calculateWheelPosition(index);
        Rectangle body = bodyOf(w);
        body.moveTo(xy[0], xy[1]);
        body.makeVisible();

        Symbol current = w.getCurrentSymbol();
        for (Symbol s : w.getSymbols()) {
            if (s != current) {
                figureOf(s).makeInvisible();
            }
        }
        if (current == null) {
            return;
        }
        Shapes figure = figureOf(current);
        if (current.isConcealed()) {
            figure.makeInvisible();
            return;
        }
        placeFigure(figure, current, xy);
        figure.makeVisible();
    }
    
    /**
     * Ajusta tamaño y posicion de la figura de un simbolo para que quede
     * centrada en la celda de la rueda (xy).
     */
    private void placeFigure(Shapes figure, Symbol symbol, int[] xy) {
        int size = symbol.getSize();
        int fx = xy[0] + (WHEEL_WIDTH - size) / 2;
        int fy = xy[1] + (WHEEL_HEIGHT - size) / 2;
        if (figure instanceof Triangle) {
            fx += size / 2;   // el triangulo se ubica por su apice
        }
        figure.setSize(size);
        figure.moveTo(fx, fy);
    }
    
    /**
     * Si la maquina esta visible, muestra uno a uno los simbolos por los que
     * pasa la rueda p al girar steps pasos (solo dibujo: no cambia el dominio).
     */
    private void animateSteps(int p, int steps) {
        if (!isVisible) {
            return;
        }
        Wheel w = eje.getWheel(p);
        List<Symbol> list = w.getSymbols();
        int total = list.size();
        int real = w.effectiveSteps(steps);
        int direction = (real < 0) ? -1 : 1;
        int moves = (int) (Math.abs((long) real) % total);
        int index = w.getPosition();
        int[] xy = calculateWheelPosition(p - 1);
        Shapes shown = figureOf(list.get(index));
        for (int i = 0; i < moves; i++) {
            index = ((index + direction) % total + total) % total;
            Symbol s = list.get(index);
            shown.makeInvisible();
            shown = figureOf(s);
            if (!s.isConcealed()) {
                placeFigure(shown, s, xy);
                shown.makeVisible();
            }
            Canvas.getCanvas().wait(ANIMATION_DELAY);
        }
    }
    
    /**
     * Cambia el color de la carcasa cuando la maquina entra o sale
     * de un estado ganador (todas las ruedas muestran el mismo simbolo).
     */
    private void updateJackpotLook() {
        boolean jackpot = !eje.isEmpty() && eje.getWheel(1).getCurrentSymbol() != null
                          && countDistinct() == 1;
        if (jackpot == jackpotShown) {
            return;
        }
        jackpotShown = jackpot;
        machineFrame.changeColor(jackpot ? JACKPOT_COLOR : FRAME_COLOR);
        drawAllWheels();   // la carcasa quedo encima: se redibujan las ruedas
    }

    private Rectangle bodyOf(Wheel w) {
        Rectangle body = bodies.get(w);
        if (body == null) {
            body = new Rectangle();
            body.changeSize(WHEEL_HEIGHT, WHEEL_WIDTH);
            body.changeColor(bodyColor(w));
            bodies.put(w, body);
        }
        return body;
    }

    /**
     * Figura de un simbolo segun su tipo:
     * normal = circulo, ephemeral = cuadrado, shy = triangulo.
     */
    private Shapes figureOf(Symbol s) {
        Shapes figure = figures.get(s);
        if (figure == null) {
            switch (s.getType()) {
                case "ephemeral": figure = new Rectangle(); break;
                case "shy":       figure = new Triangle();  break;
                default:          figure = new Circle();    break;
            }
            figure.setSize(s.getSize());
            figure.changeColor(s.getColor());
            figures.put(s, figure);
        }
        return figure;
    }

    private void forgetWheel(Wheel w) {
        for (Symbol s : w.getSymbols()) {
            forgetSymbol(s);
        }
        Rectangle body = bodies.remove(w);
        if (body != null) {
            body.makeInvisible();
        }
    }

    private void forgetSymbol(Symbol s) {
        Shapes figure = figures.remove(s);
        if (figure != null) {
            figure.makeInvisible();
        }
    }

    /**
     * Registra que la operacion fue exitosa.
     */
    private void succeed() {
        this.lastOperationOk = true;
        updateJackpotLook();
    }

    /**
     * Registra el fallo de la operacion y muestra alerta si esta visible.
     * @param message mensaje de error.
     */
    private void fail(String message) {
        this.lastOperationOk = false;
        if (this.isVisible) {
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
