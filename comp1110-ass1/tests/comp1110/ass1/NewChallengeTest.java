package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import java.util.Collections;
import java.util.HashSet;

import static comp1110.ass1.Challenge.newChallenge;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class NewChallengeTest {
    private int getDifficulty(Challenge challenge) {
        int challengeNum = challenge.getChallengeNumber();
        if (challengeNum <= 12) {
            return 0;
        } else {
            for (int diff = 1; diff < 4; diff++) {
                if (challengeNum <= 12 + diff * 16) {
                    return diff;
                }
            }
        }
        return -1;
    }

    private int countChallenges(Challenge[] Challenges) {
        HashSet<Challenge> set = new HashSet<>();
        Collections.addAll(set, Challenges);
        return set.size();
    }

    private void test(int difficulty) {
        Challenge[] out = new Challenge[20];
        for (int i = 0; i < out.length; i++) {
            Challenge challenge = newChallenge(difficulty);
            assertNotNull(challenge, "Expected newChallenge to not return null");
            int actualDifficulty = getDifficulty(challenge);
            assertEquals(difficulty, actualDifficulty, "Expected challenge difficulty " + difficulty +
                    ", on calling randomChallenge(" + difficulty + ") but got one of difficulty " + actualDifficulty +
                    ": challenge number " + challenge.getChallengeNumber() + ".");
            out[i] = challenge;
        }
        int unique = countChallenges(out);
        assertTrue(unique >= 3, "Expected at least 3 different Challenges after calling newChallenge() 12 times, but only got " + unique + ".");
    }

    @Test
    public void testStarter() {
        test(0);
    }

    @Test
    public void testJunior() {
        test(1);
    }

    @Test
    public void testExpert() {
        test(2);
    }

    @Test
    public void testMaster() {
        test(3);
    }

}
