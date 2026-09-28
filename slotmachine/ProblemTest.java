import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la resolución y simulación de la máquina tragamonedas.
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 3.0
 */
public class ProblemTest {

    @Test
    public void shouldSimulateAndSolveCorrectly() {
        // Si simulate y solve son métodos estáticos en SlotMachineContest:
        int[][] steps3 = SlotMachineContest.solve(3);
        assertNotNull(steps3);
        assertTrue(steps3.length > 0);
        
        // Simulación visual
        SlotMachineContest.simulate(3);
    }
}