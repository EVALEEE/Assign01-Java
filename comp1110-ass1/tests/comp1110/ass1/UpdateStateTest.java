package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateStateTest {

    private void test(String state, Tile tile, ColourCatch game) {
        String errMsg = "Expected state " + state + ", for tile placement " + tile.toStateString() + " but got ";
        game.updateState(tile);
        State[][] out = game.getState();
        assertEquals(state, game.stateToString(), errMsg + game.stateToString());
        // Test colours on board are unchanged
        errMsg = "Expected colours of board unchanged, but got ";
        for (int i = 0; i < State.board.length; i++) {
            int x = i / 3;
            int y = i % 3;
            assertEquals(out[x][y].getColour(), State.board[i],
                    errMsg + out[x][y].getColour() + " in location (" + x + ", " + y + ").");
        }
    }

    private final static String[][] emptyBoard = {
            {"", "An02", "Lw2Bs5"},
            {"", "Bn11", "Bn4"},
            {"", "Ds10", "Fn3Le6"},
            {"", "Cw21", "Fs7Bs8"},
            {"", "Aw00", "Ls0Be1"},
            {"", "Be10", "Be3"},
            {"", "Es02", "Bs2Ls5"},
    };
    private final static String[][] filledBoard = {
            {"Ae00Dn11", "En02", "Bw0Ln1Ln2Lw4Bn5Fs7"},
            {"Ae00Dn11En02", "Be22", "Bw0Ln1Ln2Lw4Bn5Fs7Be8"},
            {"Ae00Be22Dn11En02", "Cs10", "Bw0Ln1Ln2Be3Lw4Bn5Fe6Fs7Be8"},
            {"An00", "Ee11", "Lw0Bs3Be4Le5"},
            {"An00Ee11", "De20", "Lw0Bs3Be4Le5Fw6Ln7"},
            {"An00De20Ee11", "Be22", "Lw0Bs3Be4Le5Fw6Ln7Be8"},
            {"An00Be22De20Ee11", "Cw01", "Lw0Fs1Bs2Bs3Be4Le5Fw6Ln7Be8"}
    };

    @Test
    public void testEmptyBoard() {
        for (String[] input : emptyBoard) {
            ColourCatch game = new ColourCatch();
            testUtilities.initialiseTiles(input[0], game);
            Tile tile = testUtilities.getTile(new Tile(input[1]), game);
            assert tile != null;
            test(input[2], tile, game);
        }
    }


    @Test
    public void testFilledBoard() {
        for (String[] input : filledBoard) {
            ColourCatch game = new ColourCatch();
            testUtilities.initialiseTiles(input[0], game);
            Tile tile = testUtilities.getTile((new Tile(input[1])), game);
            assert tile != null;
            test(input[2], tile, game);
        }


    }

}
