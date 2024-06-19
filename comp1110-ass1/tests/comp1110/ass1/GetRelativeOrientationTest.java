package comp1110.ass1;

import org.junit.jupiter.api.*;

import static comp1110.ass1.Orientation.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class GetRelativeOrientationTest {

    @Test
    public void testNorth() {
        for (int i = 0; i < 4; i++) {
            Orientation expected = Orientation.values()[i];
            Animal a = new Animal(expected, Type.FROG);
            Orientation out = a.getRelativeOrientation(Orientation.NORTH);
            assertEquals(expected, out, "Expected orientation " + expected + ", but got " + out);
        }
    }

    @Test
    public void testEast() {
        Orientation[] in = {NORTH, EAST, SOUTH, WEST};
        Orientation[] expected = {EAST, SOUTH, WEST, NORTH};
        for (int i = 0; i < in.length; i++) {
            Animal a = new Animal(in[i], Type.FROG);
            Orientation out = a.getRelativeOrientation(EAST);
            assertEquals(expected[i], out,
                    "Expected orientation " + expected[i] + ", but got " + out);
        }
    }

    @Test
    public void testWest() {
        Orientation[] in = {NORTH, EAST, SOUTH, WEST};
        Orientation[] expected = {WEST, NORTH, EAST, SOUTH};
        for (int i = 0; i < in.length; i++) {
            Animal a = new Animal(in[i], Type.LIZARD);
            Orientation out = a.getRelativeOrientation(WEST);
            assertEquals(expected[i], out,
                    "Expected orientation " + expected[i] + ", but got " + out);
        }
    }


    @Test
    public void testSouth() {
        Orientation[] in = {NORTH, EAST, SOUTH, WEST};
        Orientation[] expected = {SOUTH, WEST, NORTH, EAST};
        for (int i = 0; i < in.length; i++) {
            Animal a = new Animal(in[i], Type.BUG);
            Orientation out = a.getRelativeOrientation(SOUTH);
            assertEquals(expected[i], out,
                    "Expected orientation " + expected[i] + ", but got " + out);
        }
    }

}
