package comp1110.ass1;

/**
 * This enumeration type represents the four cardinal orientations.
 * <p>
 * Notice that this is an enumeration type, so none of the fields change once the type is created (they are all declared final).
 * </p>
 */
public enum Orientation {
    NORTH, EAST, SOUTH, WEST, NONE;

    /**
     * Given a lower-case character, return the Orientation associated with this character.
     * If the character doesn't have an associated orientation, return `NONE`
     *
     * @param orientation the given char
     * @return the Orientation associated with this char.
     */
    public static Orientation fromChar(char orientation) {
        return switch (orientation) {
            case 'n' -> NORTH;
            case 's' -> SOUTH;
            case 'e' -> EAST;
            case 'w' -> WEST;
            default -> NONE;
        };
    }

    /**
     * @return the representative char for this orientation
     */
    public char toChar() {
        return switch (this) {
            case NORTH -> 'n';
            case EAST -> 'e';
            case SOUTH -> 's';
            case WEST -> 'w';
            case NONE -> 'x';
        };
    }

    /**
     * Rotate this orientation 90 degrees clockwise.
     *
     * @return the rotated orientation
     */
    public Orientation rotate() {
        return values()[(this.ordinal() + 1) % 4];
    }
}
