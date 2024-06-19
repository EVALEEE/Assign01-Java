package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FindCandidatePlacementsTest {

    private void test(Challenge challenge, String initialTiles, String tile, ArrayList<Location> expected) {
        ColourCatch game = new ColourCatch(challenge);
        testUtilities.initialiseTiles(initialTiles, game);
        Tile t = testUtilities.getTile(new Tile(tile), game);
        assert t != null;
        t.setLocation(new Location());
        String expect = expected.stream().map(Object::toString)
                .collect(Collectors.joining(""));
        String errMsg =
                "Expected " + expect + " for challenge: " + challenge.getChallengeNumber() + " and state \"" + game.stateToString() + "\", " +
                        "but got ";
        ArrayList<Location> out = game.findCandidatePlacements(t);
        String output = out.stream().map(Object::toString)
                .collect(Collectors.joining(""));
        Collections.sort(expected);
        Collections.sort(out);
        assertEquals(expected, out, errMsg + output);
    }

    private static final String[][] valid = {
            // challenge number, initial tiles, tile, valid locations
            {"5", "Bx00Cw10", "Dn00", "12"},
            {"3", "As00Cn12De20Ew01", "Bn00", "11"},
            {"50", "", "Cw00", "01"},
            {"50", "", "Ce00", "2021"},
            {"47", "Bx20", "Cn00", "001112"},
            {"33", "Bn00Ee01", "As00", "11"},
            {"24", "Cs01Dw20", "Bw00", "0002101222"},
            {"24", "Aw01Cn12En10", "Bs00", "21"}
    };

    @Test
    public void testSingle() {
        for (int i = 0; i < testUtilities.SOLUTIONS.length; i++) {
            String solution = testUtilities.SOLUTIONS[i];
            Challenge c = Challenge.CHALLENGES[i];
            String initialTiles = solution.substring(0, solution.length() - 4);
            String tile = solution.substring(solution.length() - 4);
            String loc = solution.substring(solution.length() - 2);
            ArrayList<Location> expected = new ArrayList<>();
            expected.add(new Location(loc));
            test(c, initialTiles, tile, expected);
        }
    }

    @Test
    public void testValid() {
        for (String[] inputs : valid) {
            ArrayList<Location> expected = new ArrayList<>();
            String[] split = inputs[3].split("(?<=\\G.{2})");
            for (String loc : split) {
                expected.add(new Location(loc));
            }
            test(Challenge.CHALLENGES[Integer.parseInt(inputs[0]) - 1], inputs[1], inputs[2], expected);
        }
    }
}
