package comp1110.ass1;

import java.util.Random;

public class Challenge {

    /**
     * The target board state for the Challenge
     **/
    private final State[] targetBoard;

    /**
     * The constraints for the Challenge
     **/
    private final Constraint[] constraints;

    /**
     * The identifying number associated with a Challenge
     **/
    private final int challengeNumber;

    /**
     * This array defines a set of 60 pre-defined puzzle Challenges.
     * <p>
     * There are 4 categories of Challenge, according to 4 difficulty levels. There are 12-16
     * Challenges per difficulty level, organised within the array as follows:
     * <p>
     * Starter: 0-11
     * Junior: 12-27
     * Expert: 28-43
     * Master: 44-60
     * <p>
     * Each challenge is encoded in terms of:
     * 1 - A string representing a target state for the board (see README for further details)
     * 2 - A string representing additional constraints for the Challenge (see README for further
     * details)
     * 3 - An Challenge number corresponding to the challenge number used in the original game.
     */
    public static final Challenge[] CHALLENGES = new Challenge[]{
            // STARTER
            new Challenge("Lw0Fn2Fw3Lw4Le5", "FrxFyxLbxLbxLbx", 1),
            new Challenge("Ls0Be1Fn2Be3Le4Le5Fs6Bs7Bs8", "FyxFyxLbxLbxLbr", 2),
            new Challenge("Bn0Lw1Le3Bs4Fw5Ln7", "FyxFbbLgbLgbLrb", 3),
            new Challenge("Lw0Bn1Bs2Le4Ln5Bs7Bn8", "FyxFrxLbxLbxLby", 4),
            new Challenge("Fs3Bs4Lw5Be6Fs8", "FgxFryLyxLgxLbb", 5),
            new Challenge("Fe1Fw2Le7Bs8", "FgxFyxLrxLbxLgg", 6),
            new Challenge("Fn1Bs2Ln5Fe7Bn8", "FgxFggLyxLrxLby", 7),
            new Challenge("Bs0Fw1Bw4Fw5Ln7", "FgbFbbLrxLyxLgb", 8),
            new Challenge("Le1Bs2Bs6Fw7", "FgyFbyLrxLgxLgy", 9),
            new Challenge("Fw0Bs4Fw5", "FbxFbbLyxLgxLgx", 10),
            new Challenge("Bs1Lw2Ls6", "FbxFbgLyxLbgLyg", 11),
            new Challenge("Bn3Fw4", "FbgFbrLbxLygLyg", 12),
            // JUNIOR
            new Challenge("", "FbrFgyLygLbyLbr", 13),
            new Challenge("", "FryFbyLgxLgxLgx", 14),
            new Challenge("", "FyxFyxLbxLbxLbx", 15),
            new Challenge("", "FgbFbbLgxLrxLyx", 16),
            new Challenge("", "FgbFgyLbrLbrLyg", 17),
            new Challenge("", "FgbFryLgxLgxLyx", 18),
            new Challenge("", "FyxFggLrxLbxLby", 19),
            new Challenge("", "FgxFrbLgxLgxLbx", 20),
            new Challenge("", "FgxFgxLyxLbrLbr", 21),
            new Challenge("", "FgxFrxLbxLbxLby", 22),
            new Challenge("", "FgyFygLbrLbrLgb", 23),
            new Challenge("", "FggFbbLyxLrxLgx", 24),
            new Challenge("", "FbxFbyLgxLgxLgy", 25),
            new Challenge("", "FyxFyxLbxLbxLgg", 26),
            new Challenge("", "FrxFbxLbxLygLyg", 27),
            new Challenge("", "FrbFbgLygLgbLgb", 28),
            // EXPERT
            new Challenge("", "FgxFgxLrxLyxLgy", 29),
            new Challenge("", "FggFygLyxLbxLbg", 30),
            new Challenge("", "FbgFbgLbxLyxLyg", 31),
            new Challenge("", "FgyFbrLgxLbxLbx", 32),
            new Challenge("", "FyxFbbLrxLyxLgg", 33),
            new Challenge("", "FyxFyxLrxLgxLgg", 34),
            new Challenge("", "FbgFrbLgxLyxLyg", 35),
            new Challenge("", "FrxFbxLyxLbxLby", 36),
            new Challenge("", "FyxFygLrxLbgLgb", 37),
            new Challenge("", "FgxFgxLyxLgxLry", 38),
            new Challenge("", "FbgFbgLygLggLbr", 39),
            new Challenge("", "FgxFbgLyxLyxLgg", 40),
            new Challenge("", "FgbFbbLyxLyxLrb", 41),
            new Challenge("", "FyxFbbLgxLggLry", 42),
            new Challenge("", "FbyFgyLrxLbxLgy", 43),
            new Challenge("", "FbxFbxLyxLgyLrb", 44),
            // MASTER
            new Challenge("", "FrbFgbLygLgbLbg", 45),
            new Challenge("", "FyxFbrLbxLbxLgy", 46),
            new Challenge("", "FbxFbxLrxLyxLgb", 47),
            new Challenge("", "FbgFbrLygLygLbg", 48),
            new Challenge("", "FgyFbbLygLgbLry", 49),
            new Challenge("", "FgbFgbLrxLyxLbb", 50),
            new Challenge("", "FgxFgxLbxLbxLby", 51),
            new Challenge("", "FrxFggLbxLyxLgx", 52),
            new Challenge("", "FgxFgbLbbLgbLrb", 53),
            new Challenge("", "FbyFbrLgxLgbLyg", 54),
            new Challenge("", "FyxFbbLyxLbxLrx", 55),
            new Challenge("", "FbrFgbLbxLyxLyg", 56),
            new Challenge("", "FgxFbxLrxLggLbg", 57),
            new Challenge("", "FgxFbxLyxLggLbr", 58),
            new Challenge("", "FgxFgxLbxLbxLgb", 59),
            new Challenge("", "FyxFgbLbxLgxLyg", 60)
    };


    /**
     * Given a targetState, a constraint string and a challenge number, constructs a Challenge
     * object.
     *
     * @param targetState     A string representing the list of target states
     * @param constraint      A string representing the list of constraints
     * @param challengeNumber The challenge number from the original game, from 1 to 60.
     */
    public Challenge(String targetState, String constraint, int challengeNumber) {
        assert challengeNumber >= 1 && challengeNumber <= 60;

        // Initialise the target board state
        if (targetState.isEmpty()) {
            targetBoard = null;
        } else {
            String[] targets = targetState.split("(?<=\\G.{3})");
            targetBoard = new State[targets.length];
            for (int i = 0; i < targets.length; i++) {
                targetBoard[i] = new State(targets[i]);
            }
        }

        // Initialise the constraints
        String[] c = constraint.split("(?<=\\G.{3})");
        this.constraints = new Constraint[c.length];
        for (int i = 0; i < c.length; i++) {
            this.constraints[i] = new Constraint(c[i]);
        }
        // set the challenge number
        this.challengeNumber = challengeNumber;
    }

    /**
     * Initialise an empty Challenge
     */
    public Challenge() {
        targetBoard = new State[0];
        constraints = new Constraint[0];
        challengeNumber = -1;
    }

    /**
     * Given a difficulty level, choose a random challenge of the given difficulty from the CHALLENGES array above.
     * <p>
     * Difficulty 0: "Starter" Challenge numbers 1-12
     * Difficulty 1: "Junior" Challenge numbers 13-28
     * Difficulty 2: "Expert" Challenge numbers 29-44
     * Difficulty 3: "Master" Challenge numbers 45-60
     * </p>
     *
     * @param difficulty the given difficulty level
     * @return A random challenge of the given difficulty from the CHALLENGES array in this class.
     */
    public static Challenge newChallenge(int difficulty) {
        assert difficulty >= 0 && difficulty <= 3;
        Random random = new Random();

        if (difficulty == 0) {
            return CHALLENGES[random.nextInt(12) + 1];
        } else if (difficulty == 1) {
            return CHALLENGES[random.nextInt(15) + 13];
        } else if (difficulty == 2) {
            return CHALLENGES[random.nextInt(15) + 29];
        } else {
            return CHALLENGES[random.nextInt(15) + 45];
        }
    }

    /**
     * @return The challenge number of this challenge.
     */
    public int getChallengeNumber() {
        return this.challengeNumber;
    }

    /**
     * @return The target board state for this challenge
     */
    public State[] getTargetBoard() {
        return targetBoard;
    }

    /**
     * @return The constraints for this challenge.
     */
    public Constraint[] getConstraints() {
        return constraints;
    }

    public String targetBoardToString() {
        if (targetBoard == null) {
            return "";
        }
        StringBuilder target = new StringBuilder();
        for (State s : targetBoard) {
            target.append(s.toTargetString());
        }
        return target.toString();
    }

    public String constraintsToString() {
        StringBuilder target = new StringBuilder();
        for (Constraint c : constraints) {
            target.append(c.toString());
        }
        return target.toString();
    }
}
