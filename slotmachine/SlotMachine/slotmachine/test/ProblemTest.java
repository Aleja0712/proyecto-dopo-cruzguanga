package test;
import presentation.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la solucion del concurso: la secuencia retornada debe ganar.
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 4.0
 */
public class ProblemTest {

    @Test
    public void shouldSolveAndReachJackpot() {
        for (int n = 3; n <= 50; n++) {
            SlotMachine machine = new SlotMachine(n);
            int[][] steps = SlotMachineContest.solve(machine, n);
            assertNotNull(steps);
            assertTrue(machine.isJackpot(), "n = " + n);
        }
    }

    @Test
    public void shouldReturnValidActions() {
        int n = 10;
        int[][] steps = SlotMachineContest.solve(n);
        assertTrue(steps.length > 0);
        for (int[] action : steps) {
            assertEquals(2, action.length);
            assertTrue(action[0] >= 1 && action[0] <= n);
            assertNotEquals(0, action[1]);
        }
    }
}
