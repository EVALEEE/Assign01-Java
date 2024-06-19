package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import java.util.Optional;

import static comp1110.ass1.Orientation.NONE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DoesPlacementOverlapTest {

    private final String[][] states = {
            // tile state, valid placement(s), invalid placement(s)
            {"En00Ds11Bn20", "An12Cs12", "Ce20Aw10Ae11"},
            {"Dw10En02", "Bs00Ce21Cw20", "En00As01An12"},
            {"Bx22Cw00Ds11", "An10Es02", "As00Ee01Aw20"}

    };

    private void test(String state, String tile, boolean expected) {
        ColourCatch game = new ColourCatch();
        testUtilities.initialiseTiles(state, game);
        Tile t = new Tile(tile);
        boolean out = game.doesPlacementOverlap(t);
        assertEquals(expected, out, "Expected " + expected + " for state " + state + " and tile " + tile + ", but got " + out);
    }


    @Test
    public void testSimple() {
        Tile t = new Tile("An00");
        Orientation[] values = Orientation.values();
        for (Orientation o : values) {
            if (o != NONE) {
                t.setOrientation(o);
                test("", t.toStateString(), false);
                test(t.toStateString(), t.toStateString(), true);
            }
        }
    }

    @Test
    public void testNonTrivial() {
        for (String[] state : states) {
            String[] valid = state[1].split("(?<=\\G.{4})");
            String[] invalid = state[2].split("(?<=\\G.{4})");
            for (String t : valid) {
                test(state[0], t, false);
            }
            for (String t : invalid) {
                test(state[0], t, true);
            }
        }
    }
}
