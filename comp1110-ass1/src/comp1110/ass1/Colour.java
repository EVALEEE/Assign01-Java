package comp1110.ass1;


/**
 * This enumeration type represents the four colours in the game.
 * <p>
 * Notice that this is an enumeration type, so none of the fields change once the type is created (they are all declared final).
 * </p>
 */
public enum Colour {
    BLUE, GREEN, RED, YELLOW, NONE;

    /**
     * Given a lower-case character, return the Orientation associated with this character.
     * If the character doesn't have an associated orientation, return `NONE`
     *
     * @param colour the char to convert to a colour
     * @return the associated colour for the char, or NONE if there isn't one.
     */
    public static Colour fromChar(char colour) {
        return switch (colour) {
            case 'b' -> BLUE;
            case 'g' -> GREEN;
            case 'r' -> RED;
            case 'y' -> YELLOW;
            default -> NONE;
        };
    }

    /**
     * @return the representative char for this colour
     */
    public char toChar() {
        return switch (this) {
            case BLUE -> 'b';
            case GREEN -> 'g';
            case RED -> 'r';
            case YELLOW -> 'y';
            default -> 'x';
        };
    }

}
