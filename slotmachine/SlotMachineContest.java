import java.util.*;
/**
 * Retorna y simula la secuencia de acciones {i,j} necesarias para ganar
 * 
 * @Mary Alejandra Guanga García- Samuel Esteban Cruz Rodiriguez 
 */
public class SlotMachineContest {

    public SlotMachineContest() {
    }

    /**
     * Calcula la secuencia de acciones para ganar el jackpot manteniendo la maquina invisible.
     * @param n numero de ruedas y simbolos.
     * @return matriz de acciones {rueda, pasos}.
     */
    public int[][] solve(int n) {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        ArrayList<int[]> actions = new ArrayList<>();

        // Fase 1: separar ruedas hasta tener n simbolos distintos
        for (int wheel = 2; wheel <= n; wheel++) {
            separateWheel(machine, wheel, n, actions);
        }

        // Fase 2: identificar que rueda colisiona en cada avance de la rueda 1
        ArrayList<Integer> pending = new ArrayList<>();
        for (int wheel = 2; wheel <= n; wheel++) {
            pending.add(wheel);
        }
        int[] matchRound = new int[n + 1];

        for (int round = 1; round <= n - 1; round++) {
            girarYRegistrar(machine, 1, 1, actions);

            Iterator<Integer> it = pending.iterator();
            while (it.hasNext()) {
                int candidate = it.next();
                girarYRegistrar(machine, candidate, -1, actions);
                if (machine.distinctSymbols() == n) {
                    matchRound[candidate] = round;
                    it.remove();
                    break;
                } else {
                    girarYRegistrar(machine, candidate, 1, actions);
                }
            }
        }

        // Fase 3: alinear las ruedas con la rueda 1
        for (int wheel = 2; wheel <= n; wheel++) {
            int steps = n - matchRound[wheel];
            girarYRegistrar(machine, wheel, steps, actions);
        }

        return actions.toArray(new int[0][]);
    }

    /**
     * Simula visualmente la solucion en una maquina visible.
     * @param n numero de ruedas y simbolos.
     */
    public void simulate(int n) {
        int[][] steps = solve(n);

        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();

        for (int[] action : steps) {
            machine.spin(action[0], action[1]);
        }
    }

    /**
     * Gira la rueda a lo largo de sus n posiciones y la deja en la posicion
     * que produce la mayor cantidad de simbolos distintos.
     */
    private void separateWheel(SlotMachine machine, int wheel, int n, ArrayList<int[]> actions) {
        int girosOptimos = 0;
        int maxSimbolosDistintos = -1;

        for (int giro = 0; giro < n; giro++) {
            int simbolosActuales = machine.distinctSymbols();
            if (simbolosActuales > maxSimbolosDistintos) {
                maxSimbolosDistintos = simbolosActuales;
                girosOptimos = giro;
            }
            girarYRegistrar(machine, wheel, 1, actions);
        }

        // Regresar por el camino mas corto a la mejor posicion encontrada
        int pasos = (girosOptimos <= n - girosOptimos) ? girosOptimos : girosOptimos - n;
        if (pasos != 0) {
            girarYRegistrar(machine, wheel, pasos, actions);
        }
    }

    /**
     * Aplica el giro en la maquina y almacena la accion en la lista.
     */
    private void girarYRegistrar(SlotMachine machine, int wheel, int steps, ArrayList<int[]> actions) {
        machine.spin(wheel, steps);
        actions.add(new int[]{wheel, steps});
    }
}