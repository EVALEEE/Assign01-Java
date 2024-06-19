package comp1110.ass1;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Timeout;

import static comp1110.ass1.testUtilities.initialiseTiles;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class IsConstraintSatisfiedTest {


    public void test(ColourCatch game, Constraint constraint, boolean expected) {
        String errMsg =
                "Expected " + expected + " for constraint " + constraint + " and state " + game.tilePlacementsToString() +
                        ", " + "but got ";
        boolean out = game.isConstraintSatisfied(constraint);
        assertEquals(expected, out,
                errMsg + out);
    }

    @Test
    public void testSatisfied() {
        for (int i = 0; i < testUtilities.SOLUTIONS.length; i++) {
            Challenge c = Challenge.CHALLENGES[i];
            ColourCatch game = new ColourCatch(c);
            initialiseTiles("", game);
            test(game, c.getConstraints()[0], false);
            String tiles = testUtilities.SOLUTIONS[i];
            for (Constraint constraint : c.getConstraints()) {
                game = new ColourCatch(c);
                initialiseTiles(tiles, game);
                test(game, constraint, true);
            }
        }
    }

    @Test
    public void testUnsatisfied() {
        Challenge c = Challenge.CHALLENGES[0];
        ColourCatch game = new ColourCatch(c);
        initialiseTiles("Ew00", game);
        test(game, c.getConstraints()[2], true);
        c = Challenge.CHALLENGES[3];
        String[] tiles = {"Cn10", "Cw20Ds00", "Bx02En12", "An01Ds00Es12", "Bx01En11"};
        Constraint[] constraints = c.getConstraints();
        for (int i = 0; i < constraints.length; i++) {
            game = new ColourCatch(c);
            initialiseTiles(tiles[i], game);
            test(game, constraints[i], false);
        }
        c = Challenge.CHALLENGES[45];
        game = new ColourCatch(c);
        initialiseTiles("Cn02", game);
        test(game, c.getConstraints()[0], false);
    }

}
