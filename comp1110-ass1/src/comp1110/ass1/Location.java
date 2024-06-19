package comp1110.ass1;

import java.util.Objects;

/**
 * This class encodes a certain cartesian coordinate system (row, column). We call this row-major order.
 * For example, in the grid below the letter 'd' is at (1,0) Row 1, column 0. Row and Column values may be any
 * positive or negative integer.
 * <p>
 * 0 1 2
 * 0 a b c
 * 1 d e f
 * 2 g h i
 */
public class Location implements Comparable<Location> {

    /**
     * The Location's row-coordinate
     */
    private int row;

    /**
     * The Location's column-coordinate
     */
    private int column;

    /**
     * A default "out-of-bounds" coordinate
     */
    static final int OUT = -1;

    /**
     * Create a Location that is not on the board
     */
    public Location() {
        this.row = OUT;
        this.column = OUT;
    }


    /**
     * Create a new location, given an x- and y-coordinate.
     * <p>
     * Note that we can access the fields of the object under construction
     * using the `this` keyword.
     * This constructor needs to be able to handle any integer input.
     *
     * @param row    the x-coordinate
     * @param column the y-coordinate
     */
    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Given a two-character string representing a pair of (row, column) coordinates on the board,
     * construct the corresponding Location. Recall that the two character represent the row and column
     * coordinates of the Location respectively, so the string "01" corresponds to the location
     * (0,1).
     * This string representation will only contain coordinates on the game board, between values 0 to 2 inclusive.
     *
     * @param coordinates a String representing a pair of (row,column) coordinates.
     */
    public Location(String coordinates) {
        char[] charArray = coordinates.toCharArray();
        int row = Character.getNumericValue(charArray[0]);
        int column = Character.getNumericValue(charArray[1]);
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Returns whether this Location is on the Colour Catch board or not.
     * <p>
     * Remember that you can access the x and y coordinates of this instance of Location using
     * the `this` keyword. (Check out the getter and setter methods above)
     *
     * @return True if this Location is on the board, False otherwise.
     */
    public boolean isOnBoard() {
        return this.row >= 0 && this.row <= 2 && this.column >= 0 && this.column <= 2;
    }


    /**
     * Given an orientation, find the Location adjacent to this location in the direction of that orientation.
     * Note: this method does **not** check whether a Location is on the board.
     *
     * @param orientation the given orientation
     * @return the location pointed to by the given orientation.
     */
    public Location getAdjacentLocation(Orientation orientation) {

        Location adjacentLocation = new Location();
        switch (orientation) {
            case WEST:
                adjacentLocation.setColumn(this.column - 1);
                adjacentLocation.setRow(this.row);
                break;
            case EAST:
                adjacentLocation.setColumn(this.column + 1);
                adjacentLocation.setRow(this.row);
                break;
            case NORTH:
                adjacentLocation.setRow(this.row - 1);
                adjacentLocation.setColumn(this.column);
                break;
            case SOUTH:
                adjacentLocation.setRow(this.row + 1);
                adjacentLocation.setColumn(this.column);
                break;
        }
        return adjacentLocation;
    }

    /**
     * Checks whether this Location is the same as a given other Location. For this to be true,
     * the x and y values of this object must be the same as the x and y values of the other
     * object.
     *
     * @param other The other Location to compare to.
     * @return True if the Locations are identical, False otherwise.
     */
    public boolean equals(Location other) {
        return this.row == other.row && this.column == other.column;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Location pos)) return false;
        return this.equals(pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "" + row + column;
    }

    @Override
    public int compareTo(Location o) {
        return toString().compareTo(o.toString());
    }
}
