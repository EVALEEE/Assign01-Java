package comp1110.ass1;

import java.util.ArrayList;
import java.util.List;

import static comp1110.ass1.Orientation.NONE;
import static comp1110.ass1.Orientation.*;
import static comp1110.ass1.Type.*;

/**
 * This class represents a Tile in the game. The class encodes the tile ID, the animal(s) on the tile, the
 * orientation of the tile and the location of the tile.
 * The ID and animals are declared final, however care should be taken to not modify any of the fields of the animal
 * which are not declared final.
 * Editing such fields will yield unexpected results.
 */
public class Tile implements Comparable<Tile> {

    /**
     * An integer ID for this tile
     */
    private final int ID;
    /**
     * The animals located on this tile, relative to the North position.
     */
    private final Animal[] animals;
    /**
     * The current orientation of this tile
     */
    private Orientation orientation;

    /**
     * The current location of this tile is defined by the location of the top-left square in the respective orientation.
     * A tile that is not on the board or pending placement has location (-1,-1).
     */
    private Location location;

    /**
     * Construct a tile given a string representing the tile.
     * The first character is an int between 0 and 4 indicating the tileID.
     * The second char represents the orientation of the tile.
     * The third char represents the Row coordinate of the tile.
     * The fourth char represents the Column coordinate of the tile.
     *
     * @param tile A string representing the tile to create.
     */
    public Tile(String tile) {
        ID = tile.charAt(0) - 'A';
        animals = setAnimals(ID);
        this.orientation = Orientation.fromChar(tile.charAt(1));
        this.location = new Location(tile.substring(2));
    }

    /**
     * Construct a tile given an integer ID
     *
     * @param ID the ID of the tile to create.
     */
    public Tile(int ID) {
        assert ID >= 0 && ID <= 4;
        this.ID = ID;
        this.location = new Location();
        if (ID == 1) {
            this.orientation = NONE; // Orientations are irrelevant for single bug tile
        } else {
            this.orientation = NORTH;
        }
        animals = setAnimals(ID);
    }

    /**
     * Set the animals array for this tile based on the tile ID.
     *
     * @param ID The tile ID
     * @return the array of animals on this tile, relative to the North orientation. The animal in the square denoted
     * by the location (when in the North orientation) is listed first.
     * The other animal (if relevant) is the second entry in the array.
     */
    public static Animal[] setAnimals(int ID) {
        Animal[] animals = new Animal[0];
        switch (ID) {
            case 0: {
                animals = new Animal[2];
                animals[0] = new Animal(WEST, LIZARD);
                animals[1] = new Animal(SOUTH, BUG);
                break;
            }
            case 1:
                animals = new Animal[1];
                animals[0] = new Animal(NORTH, BUG);
                break;
            case 2:
                animals = new Animal[2];
                animals[0] = new Animal(WEST, FROG);
                animals[1] = new Animal(WEST, BUG);
                break;
            case 3:
                animals = new Animal[2];
                animals[0] = new Animal(WEST, LIZARD);
                animals[1] = new Animal(SOUTH, FROG);
                break;
            case 4:
                animals = new Animal[2];
                animals[0] = new Animal(NORTH, LIZARD);
                animals[1] = new Animal(NORTH, BUG);
                break;
        }
        return animals;
    }

    /**
     * @return The location of this tile
     */
    public Location getLocation() {
        return location;
    }


    /**
     * If this tile occupies more than one location, return the second location it occupies. If this tile does not
     * occupy a second location, or the location is off the board, return the default off-board Location.
     *
     * @return The other location this tile covers.
     */
    public Location getSecondLocation() {
        if (ID == 1) {
            return new Location();
        }
        return switch (this.orientation) {
            case NORTH, SOUTH -> new Location(location.getRow() + 1, location.getColumn());
            case EAST, WEST -> new Location(location.getRow(), location.getColumn() + 1);
            case NONE -> new Location();
        };

    }

    /**
     * @return The orientation of this tile
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * @return The array of animals on this tile
     */
    public Animal[] getAnimals() {
        return animals;
    }

    /**
     * @return The ID of this tile
     */
    public int getID() {
        return ID;
    }

    /**
     * Update the Location of this tile
     *
     * @param location The new Location of this tile.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Update the orientation of this tile
     *
     * @param orientation The new orientation of this tile.
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * @return The size of this tile. ie: how many squares this tile occupies.
     */
    public int getTileSize() {
        return animals.length;
    }


    @Override
    public String toString() {
        return ID + ", " + location.toString() + ", " + orientation.toString();
    }

    /**
     * @return The string representation of this tile
     */
    public String toStateString() {
        return "" + (char) (ID + 'A') + orientation.toChar() + location.getRow() + location.getColumn();
    }

    @Override
    public int compareTo(Tile tile) {
        return ID - tile.ID;
    }
}
