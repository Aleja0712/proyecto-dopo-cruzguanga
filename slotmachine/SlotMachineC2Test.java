import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * pruebas de unidad para slotmachine ciclo 2
 * 
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 2.0
 */
public class SlotMachineC2Test {

    private SlotMachine machine;

    @Before
    public void setUp() {
        machine = new SlotMachine();
    }

    // ciclo 1: pruebas de funciones basicas

    @Test
    public void shouldAddWheelAndSymbols() {
        machine.addWheel(1);
        assertTrue(machine.ok());
        
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        assertTrue(machine.ok());
        
        assertEquals(2, machine.symbols().length);
    }

    @Test
    public void shouldNotAddSymbolToEmptyMachine() {
        machine.addSymbol(1, "red");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldDeleteSymbolFromWheels() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.delSymbol("red");
        assertTrue(machine.ok());
        assertEquals(1, machine.symbols().length);
    }

    @Test
    public void shouldNotDeleteNonexistentSymbol() {
        machine.addWheel(1);
        machine.addSymbol(1, "yellow");

        machine.delSymbol("black");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldIdentifyJackpot() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        
        machine.addWheel(2);
        machine.addSymbol(2, "red");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");

        assertTrue(machine.isJackpot());
    }

    @Test
    public void shouldNotIdentifyJackpotWhenSymbolsDiffer() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        
        machine.addWheel(2);
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        assertFalse(machine.isJackpot());
    }

    // ciclo 2: gestion de ruedas (swap, lock, unlock)

    @Test
    public void shouldSwapTwoWheels() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.addWheel(2);
        machine.addSymbol(2, "blue");

        machine.swap(1, 2);
        assertTrue(machine.ok());

        String[] config = machine.configuration();
        assertEquals("blue", config[0]);
        assertEquals("red", config[1]);
    }

    @Test
    public void shouldNotSwapSameWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.swap(1, 1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWithLessThanTwoWheels() {
        machine.addWheel(1);
        machine.addSymbol(1, "green");

        machine.swap(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldLockAndUnlockWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "yellow");

        machine.lock(1);
        assertTrue(machine.ok());

        machine.unlock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotLockAlreadyLockedWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "yellow");

        machine.lock(1);
        assertTrue(machine.ok());

        machine.lock(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotUnlockUnlockedWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "yellow");

        machine.unlock(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenAWheelIsLocked() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.addWheel(2);
        machine.addSymbol(2, "blue");

        machine.lock(1);
        machine.swap(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotDeleteLockedWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.lock(1);
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    // ciclo 2: giros de ruedas

    @Test
    public void shouldSpinWheelBySteps() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "green");
        machine.addSymbol(1, "blue");

        machine.placeSymbol(1, "red");
        machine.spin(1, 1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotSpinLockedWheelBySteps() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.lock(1);
        machine.spin(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldSetExactConfiguration() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.addWheel(2);
        machine.addSymbol(2, "red");
        machine.addSymbol(2, "yellow");

        String[] target = {"red", "red"};
        machine.spin(target);
        assertTrue(machine.ok());
        assertArrayEquals(target, machine.configuration());
        assertTrue(machine.isJackpot());
    }

    @Test
    public void shouldNotSetConfigurationWithInvalidColor() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.addWheel(2);
        machine.addSymbol(2, "blue");

        String[] target = {"purple", "blue"};
        machine.spin(target);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSetConfigurationWhenLockedWheelMustChange() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.placeSymbol(1, "red");
        machine.lock(1);

        String[] target = {"blue"};
        machine.spin(target);
        assertFalse(machine.ok());
    }
    
        @Test
    public void shouldPlaceAnExistingSymbolWithoutAddingIt() {
        // Arrange
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        // Act
        machine.placeSymbol(1, "blue");
        // Assert
        assertTrue(machine.ok());
        assertEquals(2, machine.symbols().length);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldNotPlaceASymbolThatIsNotInTheWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.placeSymbol(1, "purple");

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void shouldRotateWheelForwardBySteps() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "green");
        machine.addSymbol(1, "blue");
        machine.placeSymbol(1, "red");

        machine.spin(1, 1);

        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
    }

    @Test
    public void shouldRotateWheelBackwardsBySteps() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "green");
        machine.addSymbol(1, "blue");
        machine.placeSymbol(1, "red");

        machine.spin(1, -1);

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldReturnToTheSameSymbolAfterAFullTurn() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "green");
        machine.addSymbol(1, "blue");
        machine.placeSymbol(1, "red");

        machine.spin(1, 3);

        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void shouldCountDistinctSymbolsInTheCurrentConfiguration() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addWheel(2);
        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");

        machine.spin(new String[]{"red", "blue"});
        assertEquals(2, machine.distinctSymbols());

        machine.spin(new String[]{"red", "red"});
        assertEquals(1, machine.distinctSymbols());
        assertTrue(machine.isJackpot());
    }

    @Test
    public void shouldNotAddTwoEqualSymbolsToTheSameWheel() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.addSymbol(1, "red");

        assertFalse(machine.ok());
        assertEquals(1, machine.symbols().length);
    }

    @Test
    public void shouldNotSpinAnEmptyWheel() {
        machine.addWheel(1);

        machine.spin(1);

        assertFalse(machine.ok());
    }
    
}