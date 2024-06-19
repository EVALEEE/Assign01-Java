package comp1110.ass1;

import org.junit.jupiter.api.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class LocationConstructorTest {

    private void testFromCoordinates(int x, int y, Location expected) {
        Location out = new Location(x, y);
        assertEquals(expected.getRow(), out.getRow(), "Expected x coordinate of " + expected.getRow() + ", but got " + out.getRow() + ".");
        assertEquals(expected.getColumn(), out.getColumn(), "Expected y coordinate of " + expected.getColumn() + ", but got " + out.getColumn() + ".");
    }

    private void testFromString(String in, Location expected) {
        Location out = new Location(in);
        assertEquals(expected.getRow(), out.getRow(), "Expected x coordinate of " + expected.getRow() + ", but got " + out.getRow() + ".");
        assertEquals(expected.getColumn(), out.getColumn(), "Expected y coordinate of " + expected.getColumn() + ", but got " + out.getColumn() + ".");
    }

    @Test
    public void testCoordinates() {
        for (int x = -1; x < 5; x++) {
            for (int y = -1; y < 5; y++) {
                Location expected = new Location();
                expected.setRow(x);
                expected.setColumn(y);
                testFromCoordinates(x, y, expected);
            }
        }
    }

    @Test
    public void testString() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Location expected = new Location();
                expected.setRow(x);
                expected.setColumn(y);
                testFromString(x + "" + y, expected);
            }
        }
    }
}

