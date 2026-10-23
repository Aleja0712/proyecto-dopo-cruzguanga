package test;
import presentation.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de aceptacion para la presentacion: se ejecutan y se observan
 * (la maquina se hace visible y las ruedas giran hasta el jackpot).
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 4.0
 */
public class SlotMachineContestAcceptanceTest {

    @Test
    public void shouldSimulateSmallMachine() {
        SlotMachineContest.simulate(3);
    }

    @Test
    public void shouldSimulateTwoRowMachine() {
        SlotMachineContest.simulate(10);
    }
}
