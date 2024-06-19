package comp1110.ass1;

import java.util.Objects;

import static comp1110.ass1.Colour.*;
import static comp1110.ass1.Colour.GREEN;

/**
 * This class represents the state of a single square. It encodes the Animal on the square including its orientation, the colour of the square, and the board position.
 */
public class State {

    /**
     * The animal on this square
     */
    private Animal animal;
    /**
     * The colour of this square
     */
    private final Colour colour;
    /**
     * a number between 0 and 8 representing the position of this square on the board as depicted below:
     * <p>
     * 0 1 2
     * 3 4 5
     * 6 7 8
     */
    private final int position;

    /**
     * An array denoting the colour of each square on the board, in order of position.
     */
    public static final Colour[] board = new Colour[]{BLUE, GREEN, YELLOW, RED, BLUE, BLUE, YELLOW, GREEN, GREEN};

    /**
     * Construct a state given a colour and position.
     *
     * @param colour The given colour
     * @param pos    The given position
     */
    public State(Colour colour, int pos) {
        animal = new Animal();
        this.colour = colour;
        position = pos;
    }

    /**
     * Construct a State from a string.
     * The first char represents the animal type
     * The second char represents the animal orientation (relative to North)
     * The third char represents the position of this state.
     *
     * @param target A string representing the state.
     */
    public State(String target) {
        if (target.isEmpty()) {
            this.animal = new Animal();
            this.position = -1;
            this.colour = Colour.NONE;
        } else {
            this.animal = new Animal(target.substring(0, 2));
            this.position = Integer.parseInt(String.valueOf(target.charAt(2)));
            this.colour = board[position];
        }
    }

    /**
     * @return The animal in this state
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * @return The colour of this state
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @return The position of this state
     */
    public int getPosition() {
        return position;
    }

    /**
     * Given an animal, update the animal for this state.
     * Note: We create a new animal to avoid state mutation.
     *
     * @param animal The given animal
     */
    public void setAnimal(Animal animal) {
        this.animal = new Animal(animal.getOrientation(), animal.getType());
    }

    /**
     * Clear the animal in this state. To avoid null-pointer exceptions, we create an empty animal object.
     */
    public void clearAnimal() {
        this.animal = new Animal();
    }

    /**
     * Determine if this state is equivalent to another state. Two states are equivalent if they have the same colour,
     * the same position and the animals on both are in the same orientation and have the same type.
     *
     * @param state The other state to check
     * @return True if the states are equivalent, otherwise false.
     */
    public boolean equals(State state) {
        if (this.colour == state.colour && this.position == state.position) {
            return this.animal.equals(state.animal);
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof State state)) return false;
        return this.equals(state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animal, colour, position);
    }

    /**
     * Construct a string representing the target board state.
     * See README for details on targetBoard strings.
     *
     * @return A string representing the target board state, or the empty string if this is not a target board state.
     */
    public String toTargetString() {
        if (colour == Colour.NONE) { // if a colour is set, this state is not a targetboard state.
            return "" + animal.getType().toChar() + animal.getOrientation().toChar() + position;
        } else {
            return "";
        }
    }

    /**
     * Construct a string representing this state as defined in the Constructor above.
     *
     * @return a string representing this state.
     */
    public String toStateString() {
        if (animal.getType() != Type.NONE) {
            return "" + animal.getType().toChar() + animal.getOrientation().toChar() + position;
        } else {
            return "";
        }
    }

}
