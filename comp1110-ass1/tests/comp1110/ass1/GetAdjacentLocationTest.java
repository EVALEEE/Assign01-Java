package comp1110.ass1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAdjacentLocationTest {

    private final Location[] north = {
            new Location(-1, 0),
            new Location(-1, 1),
            new Location(-1, 2),
            new Location(0, 0),
            new Location(0, 1),
            new Location(0, 2),
            new Location(1, 0),
            new Location(1, 1),
            new Location(1, 2),
    };
    private final Location[] east = {
            new Location(0, 1),
            new Location(0, 2),
            new Location(0, 3),
            new Location(1, 1),
            new Location(1, 2),
            new Location(1, 3),
            new Location(2, 1),
            new Location(2, 2),
            new Location(2, 3),
    };

    private final Location[] south = {
            new Location(1, 0),
            new Location(1, 1),
            new Location(1, 2),
            new Location(2, 0),
            new Location(2, 1),
            new Location(2, 2),
            new Location(3, 0),
            new Location(3, 1),
            new Location(3, 2),
    };
    private final Location[] west = {
            new Location(0, -1),
            new Location(0, 0),
            new Location(0, 1),
            new Location(1, -1),
            new Location(1, 0),
            new Location(1, 1),
            new Location(2, -1),
            new Location(2, 0),
            new Location(2, 1),
    };

    private void test(Location in, Orientation orientation, Location expected) {
        String errMsg = "Expected " + expected + " for Location " + in + " and orientation " + orientation + ", but " +
                "got ";
        Location out = in.getAdjacentLocation(orientation);
        assertEquals(expected, out, errMsg + out);
    }

    @Test
    public void testNorth() {
        for (int i = 0; i < 9; i++) {
            Location in = new Location(i / 3, i % 3);
            test(in, Orientation.NORTH, north[i]);
        }
    }


    @Test
    public void testEast() {
        for (int i = 0; i < 9; i++) {
            Location in = new Location(i / 3, i % 3);
            test(in, Orientation.EAST, east[i]);
        }
    }

    @Test
    public void testSouth() {
        for (int i = 0; i < 9; i++) {
            Location in = new Location(i / 3, i % 3);
            test(in, Orientation.SOUTH, south[i]);
        }
    }

    @Test
    public void testWest() {
        for (int i = 0; i < 9; i++) {
            Location in = new Location(i / 3, i % 3);
            test(in, Orientation.WEST, west[i]);
        }
    }

}
