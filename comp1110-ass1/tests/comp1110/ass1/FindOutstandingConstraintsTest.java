package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static comp1110.ass1.testUtilities.SOLUTIONS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FindOutstandingConstraintsTest {


    private void test(Challenge challenge, String initialTiles, ArrayList<Constraint> expected) {
        ColourCatch game = new ColourCatch(challenge);
        testUtilities.initialiseTiles(initialTiles, game);
        String expect = expected.stream().map(Object::toString)
                .collect(Collectors.joining(""));
        String errMsg = "Expected \"" + expect + "\" for challenge: " + challenge.getChallengeNumber() + " and state " +
                "\"" + game.stateToString() + "\", but " + "got \"";
        ArrayList<Constraint> out = game.findOutstandingConstraints();
        String output = out.stream().map(Object::toString)
                .collect(Collectors.joining(""));
        Collections.sort(out);
        Collections.sort(expected);
        assertEquals(expected, out, errMsg + output + "\"");
    }

    private ArrayList<Constraint> getConstraints(String outstanding, Challenge challenge) {
        ArrayList<Constraint> expected = new ArrayList<>();
        if (!outstanding.isEmpty()) {
            String[] out = outstanding.split("(?<=\\G.{3})");
            Constraint[] constraints = challenge.getConstraints();
            for (String s : out) {
                for (Constraint c : constraints) {
                    if (s.equals(c.toString())) {
                        expected.add(c);
                        break;
                    }
                }
            }
        }
        return expected;
    }


    public static String[][] inputs = {
            // challenge, placements, outstanding
            {"1", "Ds02Ew00", "FrxLbx"},
            {"1", "Cs00En11", "FyxLbxLbxLbx"},
            {"27", "As10Cn11Dn02", "FrxFbxLbxLyg"},
            {"2", "Aw00Cw20Ds02", "LbxLbr"},
            {"3", "As00Cn12De20Ew01", "FbbLgbLrb"},
    };

    public static String[][] violatedInputs = {
            {"1", "Aw20Cw10Dw01", "LbxLbxLbx"},
            {"27", "As01Bn22Ds10En02", "FrxFbxLbxLygLyg"},
            {"41", "Ce10Dw00En02", "FgbFbbLyxLrb"},
            {"44", "Cn12Dn10Ee01", "FbxFbxLgyLrb"},
            {"20", "Aw21Cn00De11Ew01", "FgxFrbLbx"},
    };

    @Test
    public void testEmpty() {
        String solution = "An11Bx22Cn10Ds02Ew00";
        test(Challenge.CHALLENGES[0], solution, new ArrayList<>());
        for (int i = 0; i < Challenge.CHALLENGES.length; i++) {
            test(Challenge.CHALLENGES[i], "",
                    new ArrayList<>(Arrays.asList(Challenge.CHALLENGES[i].getConstraints())));
        }
    }


    @Test
    public void testPartial() {
        for (String[] input : inputs) {
            Challenge challenge = (Challenge.CHALLENGES[Integer.parseInt(input[0]) - 1]);
            ArrayList<Constraint> expected = getConstraints(input[2], challenge);
            test(challenge, input[1], expected);
        }
        for (int i = 0; i < SOLUTIONS.length; i++) {
            Challenge c = Challenge.CHALLENGES[i];
            ArrayList<Constraint> expected = new ArrayList<>(Arrays.asList(c.getConstraints()));
            test(c, "", expected);
        }
    }

    @Test
    public void testViolatedChallenge() {
        for (String[] input : violatedInputs) {
            Challenge challenge = (Challenge.CHALLENGES[Integer.parseInt(input[0]) - 1]);
            ArrayList<Constraint> expected = getConstraints(input[2], challenge);
            test(challenge, input[1], expected);
        }
    }

}
