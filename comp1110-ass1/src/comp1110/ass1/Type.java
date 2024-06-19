package comp1110.ass1;

/**
 * This enumeration type represents the 3 animal types.
 * <p>
 * Notice that this is an enumeration type, so none of the fields change once the type is created (they are all declared final).
 * </p>
 */
public enum Type {
    BUG, FROG, LIZARD, NONE;

    /**
     * Given an upper-case character, return the Type associated with this character.
     * If the character doesn't have an associated orientation, return `NONE`
     *
     * @param type the given char
     * @return the Type associated with this char.
     */
    public static Type fromChar(char type) {
        return switch (type) {
            case 'B' -> BUG;
            case 'F' -> FROG;
            case 'L' -> LIZARD;
            default -> NONE;
        };
    }

    /**
     * @return the representative char for this Type.
     */
    public char toChar() {
        return switch (this) {
            case BUG -> 'B';
            case LIZARD -> 'L';
            case FROG -> 'F';
            case NONE -> '_';
        };
    }
}
