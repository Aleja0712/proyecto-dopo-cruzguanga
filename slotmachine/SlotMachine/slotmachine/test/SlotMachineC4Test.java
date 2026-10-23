package test;
import presentation.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de unidad del ciclo 4: tipos de ruedas, tipos de simbolos y layout.
 *
 * @author Mary Alejandra Guanga Garcia - Samuel Esteban Cruz Rodriguez
 * @version 4.0
 */
public class SlotMachineC4Test {
    
     private SlotMachine slotMachine;

    @BeforeEach
    public void setUp() { slotMachine = new SlotMachine(); }

    private void addWheels(int n) {
        for (int i = 1; i <= n; i++) slotMachine.addWheel(i);
    }

    /** Agrega una rueda al final del tipo dado con esos símbolos (el último queda a la vista). */
    private void wheelWith(String type, String... colors) {
        int pos = slotMachine.configuration().length + 1;
        slotMachine.addWheel(type, pos);
        for (String c : colors) slotMachine.addSymbol(pos, c);
    }
    
    @Test
    public void shouldAddNormalWheelByDefault() {
        slotMachine.addWheel(1);
        assertTrue(slotMachine.ok());
        assertEquals(1, slotMachine.configuration().length);
    }
    
    @Test
    public void shouldNotAddWheelOfUnknownType() {
        slotMachine.addWheel("magic", 1);
        assertFalse(slotMachine.ok());
        assertEquals(0, slotMachine.configuration().length);
    }
    
    @Test
    public void shouldRebelWheelRefuseLockButStillSpin() {
        wheelWith("rebel", "red", "blue");
        slotMachine.placeSymbol(1, "red");
        slotMachine.lock(1);
        assertFalse(slotMachine.ok());
        slotMachine.spin(1, 1);
        assertTrue(slotMachine.ok());
        assertEquals("blue", slotMachine.configuration()[0]);
    }
    
    @Test
    public void shouldRebelWheelRefuseSwapInEitherPosition() {
        wheelWith("rebel", "red");
        wheelWith("normal", "blue");
        slotMachine.swap(1, 2);
        assertFalse(slotMachine.ok());
        slotMachine.swap(2, 1);
        assertFalse(slotMachine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, slotMachine.configuration());
    }
    
    @Test
    public void shouldRebelWheelRefuseDelete() {
        wheelWith("rebel", "red");
        wheelWith("normal", "blue");
        slotMachine.delWheel(1);
        assertFalse(slotMachine.ok());
        assertEquals(2, slotMachine.configuration().length);
    }
    
    @Test
    public void shouldNormalWheelNextToRebelStillLockSwapAndDelete() {
        wheelWith("rebel", "red");
        wheelWith("normal", "blue");
        wheelWith("normal", "green");
        slotMachine.lock(2);   assertTrue(slotMachine.ok());
        slotMachine.unlock(2);
        slotMachine.swap(2, 3);  assertTrue(slotMachine.ok());
        slotMachine.delWheel(2); assertTrue(slotMachine.ok());
        assertEquals(2, slotMachine.configuration().length);
    }
    
    @Test
    public void shouldRebelWheelHaveDifferentColorFromNormalWheel() {
        slotMachine.addWheel("rebel", 1);
        slotMachine.addWheel("normal", 2);
        assertNotEquals(slotMachine.wheelColor(1), slotMachine.wheelColor(2));
    }
    
    @Test
    public void shouldNotAddSymbolOfUnknownType() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("ghost", 1, "red");
        assertFalse(slotMachine.ok());
        assertEquals(0, slotMachine.symbols().length);
    }
    
    @Test
    public void shouldNotAddSameColorTwiceEvenIfTypesDiffer() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("normal", 1, "red");
        slotMachine.addSymbol("ephemeral", 1, "red");
        assertFalse(slotMachine.ok());
        assertEquals(1, slotMachine.symbols().length);
    }
    
    @Test
    public void shouldEphemeralSymbolStartWithNormalSize() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("ephemeral", 1, "red");
        assertEquals(30, slotMachine.symbolSize(1));
    }
    
    @Test
    public void shouldEphemeralSymbolShrinkEachTimeItIsSelected() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("ephemeral", 1, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.placeSymbol(1, "blue");
        slotMachine.spin(1, 1);                         // cae en red
        assertEquals(25, slotMachine.symbolSize(1));
        slotMachine.spin(1, 1);                         // blue
        slotMachine.spin(1, 1);                         // red otra vez
        assertEquals(20, slotMachine.symbolSize(1));
    }
    
    @Test
    public void shouldEphemeralSymbolNeverGetSmallerThanAPoint() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("ephemeral", 1, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.placeSymbol(1, "blue");
        for (int i = 0; i < 100; i++) {
            slotMachine.spin(1, 1);
            slotMachine.spin(1, 1);
        }
        slotMachine.spin(1, 1);                         // termina en red
        assertEquals("red", slotMachine.configuration()[0]);
        assertEquals(2, slotMachine.symbolSize(1));
    }
    
    @Test
    public void shouldNotShrinkEphemeralSymbolOnFullTurnOrWhenPlacedWhereItAlreadyIs() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("ephemeral", 1, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.placeSymbol(1, "red");
        slotMachine.placeSymbol(1, "red");              // ya estaba ahi
        slotMachine.spin(1, 2);                         // vuelta completa
        assertEquals(30, slotMachine.symbolSize(1));
    }
    
    @Test
    public void shouldNormalSymbolKeepItsSize() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("ephemeral", 1, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.placeSymbol(1, "red");
        slotMachine.spin(1, 1);                         // cae en blue (normal)
        assertEquals(30, slotMachine.symbolSize(1));
    }
    
    @Test
    public void shouldEphemeralShrinkOnlyInTheWheelThatSpun() {
        addWheels(2);
        slotMachine.addSymbol("ephemeral", 1, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.addSymbol("ephemeral", 2, "red");
        slotMachine.addSymbol(2, "blue");
        slotMachine.placeSymbol(1, "blue");
        slotMachine.placeSymbol(2, "blue");
        slotMachine.spin(1, 1);
        assertEquals(25, slotMachine.symbolSize(1));
        slotMachine.placeSymbol(2, "red");
        assertEquals(25, slotMachine.symbolSize(2));
        // la rueda 2 solo cuenta su propia seleccion
    }
    
    @Test
    public void shouldShySymbolStartVisible() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("shy", 1, "red");
        assertFalse(slotMachine.isSymbolHidden(1));
    }
    
    @Test
    public void shouldShySymbolAlternateVisibilityEachTimeItIsSelected() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("shy", 1, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.placeSymbol(1, "blue");
        slotMachine.spin(1, 1);                         // cae en red
        assertTrue(slotMachine.isSymbolHidden(1));
        slotMachine.spin(1, 1);
        slotMachine.spin(1, 1);                         // otra vez en red
        assertFalse(slotMachine.isSymbolHidden(1));
    }
    
    @Test
    public void shouldHiddenShySymbolStillCountInConfigurationAndJackpot() {
        addWheels(2);
        slotMachine.addSymbol("shy", 1, "red");
        slotMachine.addSymbol("shy", 2, "red");
        slotMachine.addSymbol(1, "blue");
        slotMachine.addSymbol(2, "blue");
        slotMachine.placeSymbol(1, "blue");
        slotMachine.placeSymbol(2, "blue");
        slotMachine.spin(1, 1);
        slotMachine.spin(2, 1);
        assertTrue(slotMachine.isSymbolHidden(1));
        assertArrayEquals(new String[]{"red", "red"}, slotMachine.configuration());
        assertTrue(slotMachine.isJackpot());
    }
    
    @Test
    public void shouldNormalSymbolNeverBeHidden() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol(1, "red");
        slotMachine.addSymbol(1, "blue");
        for (int i = 0; i < 5; i++) {
            slotMachine.spin(1, 1);
            assertFalse(slotMachine.isSymbolHidden(1));
        }
    }
    
    @Test
    public void shouldNotToggleShySymbolWhenPlacedWhereItAlreadyIs() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("shy", 1, "red");
        slotMachine.placeSymbol(1, "red");
        assertFalse(slotMachine.isSymbolHidden(1));
    }
    
    @Test
    public void shouldNotToggleShySymbolWhenWheelOnlyPassesOverIt() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol(1, "blue");
        slotMachine.addSymbol("shy", 1, "red");
        slotMachine.addSymbol(1, "green");
        slotMachine.placeSymbol(1, "blue");
        slotMachine.spin(1, 2);                         // pasa por red, cae en green
        slotMachine.placeSymbol(1, "red");              // primera seleccion real
        assertTrue(slotMachine.isSymbolHidden(1));
    }
    
    @Test
    public void shouldAllowShyEphemeralAndNormalSymbolsInTheSameWheel() {
        slotMachine.addWheel(1);
        slotMachine.addSymbol("shy", 1, "red");
        slotMachine.addSymbol("ephemeral", 1, "blue");
        slotMachine.addSymbol(1, "green");
        assertTrue(slotMachine.ok());
        assertEquals(3, slotMachine.symbols().length);
    }
    
    // ===================== Tipo propuesto: rueda reverse =====================
    
    @Test
    public void shouldReverseWheelSpinBackwards() {
        wheelWith("reverse", "red", "green", "blue");
        slotMachine.placeSymbol(1, "red");
        slotMachine.spin(1, 1);
        assertTrue(slotMachine.ok());
        assertEquals("blue", slotMachine.configuration()[0]);
    }
    
    @Test
    public void shouldReverseWheelHaveItsOwnColor() {
        slotMachine.addWheel("reverse", 1);
        slotMachine.addWheel("normal", 2);
        slotMachine.addWheel("rebel", 3);
        slotMachine.addWheel("lefty", 4);
        assertNotEquals(slotMachine.wheelColor(1), slotMachine.wheelColor(2));
        assertNotEquals(slotMachine.wheelColor(1), slotMachine.wheelColor(3));
        assertNotEquals(slotMachine.wheelColor(1), slotMachine.wheelColor(4));
    }
    
    // ===================== Layout: matriz de 8 ruedas por fila =====================
    
    @Test
    public void shouldPlaceFirstEightWheelsInTheSameRow() {
        for (int i = 0; i < 8; i++) {
            int[] xy = slotMachine.calculateWheelPosition(i);
            assertEquals(50 + i * 70, xy[0]);
            assertEquals(100, xy[1]);
        }
    }
    
    @Test
    public void shouldStartANewRowWithTheNinthWheel() {
        assertArrayEquals(new int[]{50, 190}, slotMachine.calculateWheelPosition(8));
        assertArrayEquals(new int[]{120, 190}, slotMachine.calculateWheelPosition(9));
        assertArrayEquals(new int[]{50, 280}, slotMachine.calculateWheelPosition(16));
    }
    
    @Test
    public void shouldRelocateWheelsWhenOneIsDeleted() {
        addWheels(9);
        assertEquals(50, slotMachine.getWheelX(9));
        assertEquals(190, slotMachine.getWheelY(9));
        slotMachine.delWheel(1);
        assertEquals(8, slotMachine.configuration().length);
        assertEquals(540, slotMachine.getWheelX(8));
        assertEquals(100, slotMachine.getWheelY(8));
    }
    
    @Test
    public void shouldCreateContestMachineWithNDistinctSymbolsPerWheel() {
        slotMachine = new SlotMachine(20);
        assertEquals(20, slotMachine.configuration().length);
        assertEquals(400, slotMachine.symbols().length);
        assertTrue(slotMachine.distinctSymbols() > 1);
    }
}
