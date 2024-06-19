package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import static comp1110.ass1.testUtilities.SOLUTIONS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class IsSolutionTest {

    private final String[] invalid = {
            "An11Bx22Cn10Ds02Ew11", // 1
            "Aw01Bx22Cw20Ds02Ee10", // 2
            "As00Bx11Cn10De20Ew01", // 3
            "As01Bx02Cw10Dn00En12", // 4
            "Ae00Bx01Cw10Dn12Ee20", // 5
    };

    private void test(String state, int challengeIndex, boolean expected) {
        ColourCatch game = new ColourCatch(Challenge.CHALLENGES[challengeIndex]);
        String errMsg =
                "Expected " + expected + " for Challenge " + (game.getChallenge().getChallengeNumber()) + " and tile placements " + state + ", but got ";
        testUtilities.initialiseTiles(state, game);
        boolean out = game.isSolution();
        assertEquals(expected, out, errMsg + out);
    }

    @Test
    public void testSolution() {
        test("", 0, false);
        for (int i = 0; i < SOLUTIONS.length; i++) {
            test(SOLUTIONS[i], i, true);
        }
    }

    @Test
    public void testIncomplete() {
        test(SOLUTIONS[0], 0, true);
        for (int i = 0; i < SOLUTIONS.length; i++) {
            test(SOLUTIONS[i].substring(0, SOLUTIONS[i].length() - 4), i, false);
        }
    }

    @Test
    public void testOverlapping() {
        test(SOLUTIONS[0], 0, true);
        for (int i = 0; i < invalid.length; i++) {
            test(invalid[i], i, false);
        }
    }

    @Test
    public void testUnsatisfiedConstraint() {
        test(SOLUTIONS[0], 0, true);
        for (int i = 1; i < SOLUTIONS.length; i++) {
            test(SOLUTIONS[i], i - 1, false);
        }
    }
}
