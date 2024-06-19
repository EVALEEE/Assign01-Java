package comp1110.ass1;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Timeout;

import static comp1110.ass1.testUtilities.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ViolatesChallengeTest {

    private void test(Challenge challenge, String initialTiles, String tile, boolean expected) {
        ColourCatch game = new ColourCatch(challenge);
        initialiseTiles(initialTiles, game);
        Tile t = getTile(new Tile(tile), game);
        assert t != null;
        String errMsg =
                "Expected " + expected + " for tile placement " + tile + " and state \"" + game.stateToString() +
                        "\"," +
                        " but got ";
        boolean out = game.violatesChallenge(t);
        assertEquals(expected, out, errMsg + out);
    }


    @Test
    public void testColour() {
        String bad1 = "Dn10";
        String prefix2 = "Bx02En12";
        String prefix2a = "En12";
        String bad2 = "Ae10";
        String good1 = "An10";
        test(Challenge.CHALLENGES[6], prefix2, bad1, true);
        test(Challenge.CHALLENGES[6], prefix2, bad2, true);
        test(Challenge.CHALLENGES[6], prefix2, good1, false);
        test(Challenge.CHALLENGES[6], prefix2a, bad2, true); //not caught by unfinished-constraint solution
        test(Challenge.CHALLENGES[6], prefix2, good1, false);
    }

    @Test
    public void testEat() {
        String prefix1 = "Cs11";
        String bad1 = "As00";
        String good1 = "En12";
        test(Challenge.CHALLENGES[6], prefix1, bad1, true);
        test(Challenge.CHALLENGES[6], prefix1, good1, false);
        test(Challenge.CHALLENGES[9], "An01Bn20Cn00Ee21", "Dn02", true);
        test(Challenge.CHALLENGES[9], "Bn02Cn12De00Es10", "As11", true);
    }

    @Test
    public void testTargetBoard() {
        String solution = "An11Bx22Cn10Ds02Ew00";
        String[] tiles = solution.split("(?<=\\G.{4})");
        for (String tile : tiles) {
            test(Challenge.CHALLENGES[0], "", tile, false);
        }
        test(Challenge.CHALLENGES[0], "", "Cn12", true);
        for (int i = 0; i < SOLUTIONS.length; i++) {
            solution = SOLUTIONS[i];
            tiles = solution.split("(?<=\\G.{4})");
            for (String tile : tiles) {
                test(Challenge.CHALLENGES[i], "", tile, false);
            }
        }
    }


}
