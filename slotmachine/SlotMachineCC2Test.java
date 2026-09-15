import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Casos de prueba compartidos del Ciclo 2.
 *
 * @author Cruz Rodriguez - Guanga Garcia
 * @version 2.0
 */
public class SlotMachineCC2Test {

    private SlotMachine machine;

    @Before
    public void setUp() {
        machine = new SlotMachine();
    }

    @Test
    public void accordingCrGgShouldKeepEachWheelSymbolsAfterSwapping() {
        // Arrange
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addWheel(2);
        machine.addSymbol(2, "blue");
        // Act
        machine.swap(1, 2);
        // Assert
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
        assertEquals(2, machine.symbols().length);
    }

    @Test
    public void accordingCrGgShouldNotRotateALockedWheelEvenWithManySteps() {
        // Arrange
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "green");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        // Act
        machine.spin(1, 1000);
        // Assert
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }
}