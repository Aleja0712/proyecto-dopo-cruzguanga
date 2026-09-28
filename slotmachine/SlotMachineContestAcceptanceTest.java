import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Pruebas de aceptacion para la presentacion: se ejecutan y se observan
 * (la maquina se hace visible y las ruedas giran hasta el jackpot).
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 3.0
 */
public class SlotMachineContestAcceptanceTest {

    @Test
    public void shouldSimulateSmallMachine() {
        SlotMachineContest.simulate(3);
    }

    @Test
    public void shouldSimulateFiveWheelMachine() {
        SlotMachineContest.simulate(5);
    }
}
