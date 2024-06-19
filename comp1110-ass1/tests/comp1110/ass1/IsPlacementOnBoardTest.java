package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class IsPlacementOnBoardTest {
    Orientation[] values = Orientation.values();


    private void test(Tile in, boolean expected) {
        String errMsg = "Expected " + expected + " for tile " + in.toString() + ", but got ";
        boolean out = ColourCatch.isPlacementOnBoard(in);
        assertEquals(expected, out, errMsg + out);
    }

    @Test
    public void testX() {
        Tile in = new Tile(3);
        in.setLocation(new Location(2, 0));
        Tile tile = new Tile(3); // Redundancy for object mutation
        tile.setLocation(new Location(2, 0));
        test(in, false);
        tile.setOrientation(Orientation.WEST);
        in.setOrientation(Orientation.WEST);
        test(in, true);
        tile.setOrientation(Orientation.EAST);
        in.setOrientation(Orientation.EAST);
        test(in, true);
        tile.setOrientation(Orientation.SOUTH);
        in.setOrientation(Orientation.SOUTH);
        test(in, false);
    }

    @Test
    public void testY() {
        Tile in = new Tile(3);
        in.setLocation(new Location(0, 2));
        Tile tile = new Tile(3);
        tile.setLocation(new Location(0, 2));
        test(in, true);
        tile.setOrientation(Orientation.SOUTH);
        in.setOrientation(Orientation.SOUTH);
        test(in, true);
        tile.setOrientation(Orientation.EAST);
        in.setOrientation(Orientation.EAST);
        test(in, false);
        tile.setOrientation(Orientation.WEST);
        in.setOrientation(Orientation.WEST);
        test(in, false);
    }

    @Test
    public void testXY() {
        Tile in = new Tile(2);
        Tile tile = new Tile(2);
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                Location l = new Location(r, c);
                in.setLocation(l);
                tile.setLocation(l);
                for (int i = 0; i < 4; i++) {
                    Orientation o = values[i];
                    in.setOrientation(o);
                    tile.setOrientation(o);
                    test(in, true);
                }
            }
        }
        in = new Tile(4);
        for (int i = 0; i < values.length; i++) {
            Location l = new Location(0, 0);
            in.setLocation(l);
            test(in, true);
            l = new Location(2, 2);
            in.setLocation(l);
            test(in, false);
        }

        in = new Tile(1);
        for (int i = 0; i < values.length; i++) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    Location l = new Location(r, c);
                    in.setLocation(l);
                    test(in, true);
                }
            }
        }
    }

    @Test
    public void testInvalid() {
        Tile in = new Tile(2);
        Tile tile = new Tile(2);
        test(in, false);
        Location l = new Location(0, 0);
        in.setLocation(l);
        tile.setLocation(l);
        test(in, true);
    }
}
