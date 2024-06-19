package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class IsLocationEmptyTest {


    private void test(String state, Location loc, boolean expected) {
        ColourCatch game = new ColourCatch();
        testUtilities.initialiseTiles(state, game);
        String errMsg = "Expected " + expected + " for state " + state + " and Location " + loc + ", but got ";
        boolean out = game.isLocationEmpty(loc);
        assertEquals(expected, out, errMsg + out);
    }

    @Test
    public void testSimple() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                test("", new Location(i, j), true);
            }
        }
        String fullBoard = "An11Bx22Cn10Ds02Ew00";
        String[] tiles = fullBoard.split("(?<=\\G.{4})");
        for (String t : tiles) {
            Location l = new Location(Character.getNumericValue(t.charAt(2)), Character.getNumericValue(t.charAt(3)));
            test(fullBoard, l, false);
        }
    }

    @Test
    public void testAll() {
        test("", new Location(0, 0), true);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (String solution : testUtilities.SOLUTIONS)
                    test(solution, new Location(i, j), false);
            }
        }
    }

}
