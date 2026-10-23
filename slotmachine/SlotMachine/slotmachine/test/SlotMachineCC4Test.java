package test;
import presentation.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Casos de prueba compartidos del Ciclo 4.
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 4.0
 */
public class SlotMachineCC4Test {
    private SlotMachine machine;

    @BeforeEach
    public void setUp() {
        machine = new SlotMachine();
    }

    @Test
    public void accordingCrGgShouldLeftyWheelCopyItsLeftNeighbor() {
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.addSymbol(1, "red");   machine.addSymbol(1, "blue");   machine.addSymbol(1, "green");
        machine.addSymbol(2, "red");   machine.addSymbol(2, "blue");   machine.addSymbol(2, "green");
        machine.placeSymbol(1, "green");
        machine.placeSymbol(2, "red");
        machine.spin(2, 1);
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[1]);
    }

    @Test
    public void accordingCrGgShouldShySymbolHideAndShowAlternately() {
        machine.addWheel(1);
        machine.addSymbol("shy", 1, "red");
        machine.addSymbol(1, "blue");
        machine.placeSymbol(1, "blue");
        machine.spin(1, 1);
        assertTrue(machine.isSymbolHidden(1));
        machine.spin(1, 1);                 // blue
        machine.spin(1, 1);                 // red otra vez
        assertFalse(machine.isSymbolHidden(1));
    }

    @Test
    public void accordingCrGgShouldWrapToANewRowEveryEightWheels() {
        for (int i = 1; i <= 17; i++) {
            machine.addWheel(i);
        }
        assertEquals(machine.getWheelX(1), machine.getWheelX(9));
        assertEquals(machine.getWheelX(1), machine.getWheelX(17));
        assertTrue(machine.getWheelY(9) > machine.getWheelY(8));
        assertTrue(machine.getWheelY(17) > machine.getWheelY(16));
    }
}
