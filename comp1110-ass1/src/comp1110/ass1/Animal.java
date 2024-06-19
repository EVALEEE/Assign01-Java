package comp1110.ass1;

import java.util.Objects;

/**
 * This class represents an Animal in the game. The class encodes what `type` of animal it is, and what `orientation` it is facing.
 */
public class Animal {

    /**
     * The animal's orientation. If this animal is on a tile, its orientation represents the orientation
     * it would be facing if the Tile is in the North orientation.
     */
    private final Orientation orientation;

    /**
     * The type of the animal
     **/
    private final Type type;


    /**
     * Construct a new Animal given an animal object.
     *
     * @param animal an animal
     */
    public Animal(String animal) {
        type = Type.fromChar(animal.charAt(0));
        orientation = Orientation.fromChar(animal.charAt(1));
    }

    /**
     * Construct an animal given a char representing the type of the animal.
     * The orientation will be set to NONE as this information is not specified.
     *
     * @param type a char representing the type of the animal.
     */
    public Animal(char type) {
        this.type = Type.fromChar(type);
        orientation = Orientation.NONE;
    }

    /**
     * Given an orientation and a type, construct an animal.
     *
     * @param orientation The given orientation
     * @param type        the given Type of animal
     */
    public Animal(Orientation orientation, Type type) {
        this.orientation = orientation;
        this.type = type;
    }

    /**
     * Create a default, blank animal object.
     */
    public Animal() {
        type = Type.NONE;
        orientation = Orientation.NONE;
    }

    /**
     * @return The orientation of this animal
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * @return The type of this animal
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the orientation of this animal relative to the given orientation.
     * <p>
     * If the given orientation is North, the relative animal orientation is the same as the current orientation.
     * If the given orientation is East, the relative animal orientation is rotated 90 degrees clockwise.
     * If the given orientation is South, the relative animal orientation is rotated 180 degrees clockwise.
     * If the given orientation is West, the relative animal orientation is rotated 270 degrees clockwise.
     * <p>
     * For example, if the animal is facing East, and the given orientation is East, the
     * relative orientation of the animal is South.
     *
     * @param orientation The given orientation
     * @return the orientation of the animal relative to the given orientation.
     */
    public Orientation getRelativeOrientation(Orientation orientation) {
        switch (orientation) {
            case NORTH:
                orientation = this.orientation;
                break;
            case EAST:
                orientation = this.orientation.rotate();
                break;
            case SOUTH:
                orientation = this.orientation.rotate().rotate();
                break;
            case WEST:
                orientation = this.orientation.rotate().rotate().rotate();
                break;
        }
        return orientation;
    }

    /**
     * Determine whether this animal is equivalent to another animal.
     * Two animals are equivalent if they have the same orientation and the same type.
     *
     * @param other The other animal to check
     * @return True if these animals are equivalent, otherwise false.
     */
    public boolean equals(Animal other) {
        return this.orientation == other.orientation && this.type == other.type;
    }

    /**
     * Determine if this animal is equivalent to another object.
     *
     * @param other The other object
     * @return true if these objects are equivalent, otherwise false
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Animal a)) return false;
        return this.equals(a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientation, type);
    }

    /**
     * Return a string representation of this tile.
     *
     * @return A string consisting of the animal's type char followed by the orientation char.
     */
    @Override
    public String toString() {
        return type + " " + orientation;
    }

}
