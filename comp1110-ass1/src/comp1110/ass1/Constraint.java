package comp1110.ass1;

import java.util.Objects;

/**
 * This class encodes a challenge Constraint.
 */
public class Constraint implements Comparable<Constraint> {

    /**
     * The Animal in this constraint
     */
    private final Animal animal;
    /**
     * The colour of the animal in this constraint
     */
    private final Colour colour;

    /**
     * The colour of the bug that the animal eats, or NONE if this constraint is that the animal should not eat any bug.
     */
    private final Colour bug;

    /**
     * Construct a constraint form a string.
     * The first character represents the animal type,
     * The second character represents the colour of the animal,
     * The third character represents the colour of the bug eaten, or none.
     * for example: "Frx" in english means "a red frog does not eat a bug".
     * and "Lbg" means "a blue lizard eats a green bug"
     *
     * @param constraint a string representation of the constraint
     */
    public Constraint(String constraint) {
        animal = new Animal(constraint.charAt(0));
        colour = Colour.fromChar(constraint.charAt(1));
        bug = Colour.fromChar(constraint.charAt(2));
    }

    /**
     * Construct a constraint from an animal, a colour and the colour of a bug to eat.
     *
     * @param animal The given animal
     * @param colour The given animal colour
     * @param eat    The given bug colour
     */
    public Constraint(Animal animal, Colour colour, Colour eat) {
        this.animal = new Animal(animal.getOrientation(), animal.getType());
        this.colour = colour;
        this.bug = eat;
    }

    /**
     * @return The animal in this constraint
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * @return The colour of the animal in this constraint
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @return The colour of the bug in this constraint
     */
    public Colour getBug() {
        return bug;
    }

    /**
     * @return The string representing this constraint, as defined in the Constructor.
     */
    public String toString() {
        return "" + animal.getType().toChar() + colour.toChar() + bug.toChar();
    }

    /**
     * Determine if this constraint is equivalent to another constraint.
     * Two constraints are equivalent if they contain the same animal type, the same animal colour and the same bug colour.
     *
     * @param other The other constraint to check
     * @return true if this constraint is equivalent to the given constraint, otherwise false.
     */
    public boolean equals(Constraint other) {
        return this.animal.getType() == other.animal.getType() && this.colour == other.colour && this.bug == other.bug;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Constraint a)) return false;
        return this.equals(a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animal, colour, bug);
    }

    @Override
    public int compareTo(Constraint c) {
        return this.toString().compareTo(c.toString());
    }
}

