 import java.util.ArrayList;

/**
 * Resuelve y simula el Problem I (Slot Machine) de la maraton ICPC 2025.
 *
 * SlotMachine se usa unicamente como testing tool (SlotMachine(n), spin(wheel, steps)
 * y distinctSymbols()) y como simulador (makeVisible(), makeInvisible()).
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 3.0
 */
public class SlotMachineContest {

    /**
     * Retorna la secuencia de acciones {rueda, pasos} necesarias para ganar.
     * La maquina (n ruedas, n simbolos, inicializada aleatoriamente) permanece invisible.
     * @param n numero de ruedas y de simbolos.
     * @return acciones {i, j}: girar la rueda i, j posiciones.
     */
    public static int[][] solve(int n) {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        return new Solver(machine, n).run();
    }

    /**
     * Resuelve sobre una maquina dada (util para probar que la solucion gana).
     * @param machine maquina de n ruedas y n simbolos.
     * @param n numero de ruedas y de simbolos.
     * @return acciones {i, j} aplicadas.
     */
    public static int[][] solve(SlotMachine machine, int n) {
        return new Solver(machine, n).run();
    }

    /**
     * Simula la solucion sobre una maquina visible.
     * @param n numero de ruedas y de simbolos.
     */
    public static void simulate(int n) {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
        new Solver(machine, n).run();
    }

    /**
     * Cada llamada crea el suyo, no se comparte nada entre llamadas.
     */
    private static final class Solver {
        private final SlotMachine machine;
        private final int n;
        private final ArrayList<int[]> steps = new ArrayList<>();
        private int k;

        Solver(SlotMachine machine, int n) {
            this.machine = machine;
            this.n = n;
        }

        int[][] run() {
            k = machine.distinctSymbols();
            if (k > 1) {
                different();
            }
            if (k > 1) {
                int[] moves = identical();
                align(moves);
            }
            return steps.toArray(new int[0][]);
        }

        /**
         * Cada rueda (2..n) se deja en la posicion que aumenta los simbolos distintos.
         * Al terminar hay n simbolos distintos.
         */
        private void different() {
            int kOld = k;
            for (int wheel = 2; wheel <= n && k > 1; wheel++) {
                int bestTurn = 0;
                for (int turn = 1; turn <= n && k > 1; turn++) {
                    move(wheel, 1);
                    if (k > kOld) {
                        bestTurn = turn;
                        kOld = k;
                    }
                }
                if (k > 1 && bestTurn != 0) {
                    move(wheel, bestTurn);
                }
            }
        }

        /**
         * Avanza la rueda 1 de a un paso; la rueda que coincide con ella
         * se detecta porque al retrocederla los simbolos distintos aumentan.
         * @return moves[w] = cuantos pasos por delante de la rueda 1 estaba la rueda w.
         */
        private int[] identical() {
            int[] moves = new int[n + 1];
            for (int i = 1; i < n && k > 1; i++) {
                move(1, 1);
                int kOld = k;
                for (int wheel = 2; wheel <= n && k > 1; wheel++) {
                    move(wheel, -i);
                    if (k > kOld) {
                        moves[wheel] = i;
                        move(wheel, i);
                        break;
                    }
                    move(wheel, i);
                }
            }
            move(1, 1); // la rueda 1 completa n pasos y vuelve a su posicion original
            return moves;
        }

        /**
         * Cada rueda se lleva al simbolo de la rueda 1.
         */
        private void align(int[] moves) {
            for (int wheel = 2; wheel <= n && k > 1; wheel++) {
                if (moves[wheel] != 0) {
                    move(wheel, -moves[wheel]);
                }
            }
        }

        /**
         * Aplica la accion, la registra y consulta cuantos simbolos distintos hay.
         * Despues del jackpot (k == 1) no hace nada, como el juez.
         */
        private void move(int wheel, int stepsToSpin) {
            if (k == 1) {
                return;
            }
            machine.spin(wheel, stepsToSpin);
            steps.add(new int[]{wheel, stepsToSpin});
            k = machine.distinctSymbols();
        }
    }
}
