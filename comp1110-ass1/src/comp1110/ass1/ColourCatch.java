package comp1110.ass1;

import java.util.*;

import static comp1110.ass1.Orientation.*;

/**
 * This class represents a ColourCatch game, including the state of the game and supporting methods that allow the game to be solved.
 */
public class ColourCatch {
    private List<State> currentState;


    /**
     * The challenge represents the problem to be solved in this instance of the game
     */
    private final Challenge challenge;
    /**
     * An array of the tiles in this game
     */
    public final Tile[] tiles = new Tile[5];
    /**
     * placedTiles is an instance field that is a 2D array holding the tiles that have been placed on the board.
     * Each element contains a reference to a single tile at a specific location.
     * placedTiles[row][column] holds the tile at Location (row, column) on the board.
     */
    private final Tile[][] placedTiles = new Tile[3][3];
    /**
     * state is an instance field that is a 2D array holding the state of each square on the board.
     * Each element contains a State. state[row][column] holds the state at Location (row, column) on the board.
     */
    private final State[][] state = new State[3][3];

    /**
     * Given a challenge, initialise a ColourCatch game.
     *
     * @param challenge The given challenge
     */
    public ColourCatch(Challenge challenge) {
        this.challenge = challenge;
        initialiseState();
        initialiseTiles();
    }

    /**
     * Construct a ColourCatch game with a random challenge of level `difficulty`
     *
     * @param difficulty the given difficulty of the challenge
     */
    public ColourCatch(int difficulty) {
        this.challenge = (Challenge.newChallenge(difficulty));
        initialiseState();
        initialiseTiles();
    }

    /**
     * Construct a default ColourCatch game.
     * This is used for testing only.
     */
    public ColourCatch() {
        this.challenge = new Challenge();
        initialiseState();
        initialiseTiles();
    }

    /**
     * Initialise the state with the board colours.
     */
    private void initialiseState() {
        for (int i = 0; i < State.board.length; i++) {
            int x = i / 3;
            int y = i % 3;
            state[x][y] = new State(State.board[i], i);
        }
    }

    /**
     * Initialise the tiles for this ColourCatch game
     */
    private void initialiseTiles() {
        for (int i = 0; i < 5; i++) {
            tiles[i] = new Tile(i);
        }
    }

    /**
     * @return The placed tiles in this game
     */
    public Tile[][] getPlacedTiles() {
        return placedTiles;
    }

    /**
     * @return The array of tiles in this game.
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * Given a Location and a tile, update the placedTiles array at the given Location with a reference to this tile.
     *
     * @param location The location to update
     * @param tile     The tile to store in the given location
     */
    private void updatePlaced(Location location, Tile tile) {
        placedTiles[location.getRow()][location.getColumn()] = tile;
    }

    /**
     * @return The state of this game.
     */
    public State[][] getState() {
        return state;
    }

    /**
     * Set the state at the given Location with the given animal.
     *
     * @param location The location to update
     * @param animal   the animal to set.
     */
    public void setState(Location location, Animal animal) {
        state[location.getRow()][location.getColumn()].setAnimal(animal);
    }

    /**
     * @return The challenge for this game.
     */
    public Challenge getChallenge() {
        return challenge;
    }

    /**
     * Checks whether a Location on the board is empty. ie: whether it contains an animal.
     * Hint: The placedTiles array might be useful.
     *
     * @param location the location to check
     * @return True if there is no animal at this location, otherwise false.
     */
    public boolean isLocationEmpty(Location location) {
        int a = 0;
        label1:
        for (Tile[] tile : placedTiles) {
            for (Tile tile1 : tile) {
                if ((tile1 != null) && tile1.getTileSize() == 2) {
                    //tile's size = 2
                    if (tile1.getLocation().equals(location) || tile1.getSecondLocation().equals(location)) {
                        //not empty!
                        a = 1;
                        break label1;
                    }
                } else {
                    //size = 1
                    if ((tile1 != null) && tile1.getLocation().equals(location)) {
                        //not empty!
                        a = 1;
                        break label1;
                    }
                }
            }
        }
        return a != 1;
    }


    /**
     * Add a new tile placement to the board, updating all relevant data structures.
     *
     * @param tile The tile to be placed.
     */
    public void placeTile(Tile tile) {
        updateTiles(tile);
        updateState(tile);
    }

    /**
     * Remove a tile from the board, updating all relevant data structures
     *
     * @param tile The tile to remove
     */
    public void removeTile(Tile tile) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!(placedTiles[row][col] == null)) {
                    if (placedTiles[row][col].equals(tile)) {
                        placedTiles[row][col] = null;
                        state[row][col].clearAnimal();
                    }
                }
            }
        }
    }

    /**
     * Update the tile data structure with a new tile placement.
     * <p>
     * Each entry in the data structure corresponds to a square, and tiles are composed of 1 or 2
     * squares. So each time a tile is added, one or two entries must be updated to point to the new
     * tile accordingly.
     * <p>
     * Squares that are covered by a tile will have their data structure entry or entries point to
     * the covering tile. Squares not covered by a tile will have their data structure entry point
     * to null.
     *
     * @param tile The tile being placed.
     */
    public void updateTiles(Tile tile) {
        Location location = tile.getLocation();
        int size = tile.getTileSize();
        if (tile.getLocation().isOnBoard()) {
            updatePlaced(location, tile);
            if (size == 2) {
                updatePlaced(tile.getSecondLocation(), tile);
            }
        }
    }


    /**
     * Determine whether this tile is on the board or not. That is, whether all squares of the tile
     * are on the board.
     *
     * @param tile The tile to be checked
     * @return True if all squares of this tile are on the board, false if not.
     */
    public static boolean isPlacementOnBoard(Tile tile) {
        if (tile.getTileSize() == 2) {
            //the tile has two square
            if (tile.getLocation().isOnBoard() && tile.getSecondLocation().isOnBoard()) {
                return true;
            } else {
                return false;
            }
        } else {
            //the tile has only one square
            if (tile.getLocation().isOnBoard()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Given a tile to place, check whether the proposed tile overlaps any previous placements.
     * <p>
     * You will need to use both the tile about to be placed and the existing board state
     * (specifically the placedTiles data structure), which is kept up to date when placeTile is called.
     *
     * @param tile The tile to check.
     * @return True if the tile overlaps another tile, otherwise false.
     */
    public boolean doesPlacementOverlap(Tile tile) {
        int row = 0;
        int column = 0;
        Location tileLocation = tile.getLocation();
        Location tileSecLocation = tile.getSecondLocation();
        // Check if the tile overlaps with existing placements
        for (row = 0; row < 3; row++) {
            for (column = 0; column < 3; column++) {
                Tile existingTile = placedTiles[row][column];
                if (existingTile != null) {
                    if (existingTile.getLocation().equals(tileLocation)) {
                        updatePlaced(tile.getLocation(), tile);
                        updateTiles(tile);
                        updateState(tile);
                        return true;
                    } else if (tile.getTileSize() == 2 && existingTile.getLocation().equals(tileSecLocation)) {
                        updatePlaced(tile.getLocation(), tile);
                        updateTiles(tile);
                        updateState(tile);
                        return true;
                    } else if (existingTile.getTileSize() == 2 && existingTile.getSecondLocation().equals(tileLocation)) {
                        updatePlaced(tile.getLocation(), tile);
                        updateTiles(tile);
                        updateState(tile);
                        return true;
                    } else if (tile.getTileSize() == 2 && existingTile.getTileSize() == 2
                            && existingTile.getSecondLocation().equals(tileSecLocation)) {
                        updatePlaced(tile.getLocation(), tile);
                        updateTiles(tile);
                        updateState(tile);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determine if a tile placement is valid. ie: it is on the board and does not overlap any previously placed tiles.
     *
     * @param tile the tile to place
     * @return True if the tile can be placed validly, otherwise false.
     */
    public boolean isPlacementValid(Tile tile) {
        if (isPlacementOnBoard(tile)) {
            return !doesPlacementOverlap(tile);
        }
        return false;
    }

    /**
     * Update the state data structure with a new tile placement.
     * <p>
     * Each entry in the data structure corresponds to a square, and tiles are composed of 1 or 2 squares. So each time
     * a tile is added, one or two entries must be updated to contain the state of the tile accordingly.
     * <p>
     * Squares that are covered by a tile will have their state updated to contain the animal from the tile,
     * accounting for the relative orientation of the animal.
     * Hint: Task 5 getRelativeOrientation will be helpful.
     *
     * @param tile The tile being placed.
     */
    public void updateState(Tile tile) {
        //FIXME Task 8
//        Location mainLocation = tile.getLocation();
//        Animal[] animals = tile.getAnimals();
//        Orientation tileOrientation = tile.getOrientation();
//
//        // Update animal 1
//        Orientation relativeOrientation1 = animals[0].getRelativeOrientation(tileOrientation);
//        Animal animal1 = new Animal(relativeOrientation1, animals[0].getType());
//        setState(mainLocation, animal1);
//
//        // Update animal 2 if the tile has two squares
//        if (tile.getTileSize() == 2) {
//            Orientation relativeOrientation2 = animals[1].getRelativeOrientation(tileOrientation);
//            Animal animal2 = new Animal(relativeOrientation2, animals[1].getType());
//            Location secLocation = tile.getSecondLocation();
//            switch (tileOrientation) {
//                case NORTH:
//                    if (tile.getLocation().getRow() - 1 >= 0) {
//                        secLocation = tile.getLocation().getAdjacentLocation(tileOrientation);
//                    }
//                    break;
//                case EAST:
//                    if (tile.getLocation().getColumn() + 1 <= 2) {
//                        secLocation = tile.getLocation().getAdjacentLocation(tileOrientation);
//                    }
//                    break;
//                case SOUTH:
//                    if (tile.getLocation().getRow() + 1 <= 2) {
//                        secLocation = tile.getLocation().getAdjacentLocation(tileOrientation);
//                    }
//                    break;
//                case WEST:
//                    if (tile.getLocation().getColumn() - 1 >= 0) {
//                        secLocation = tile.getLocation().getAdjacentLocation(tileOrientation);
//                    }
//                    break;
//                case NONE:
//                    secLocation = tile.getLocation().getAdjacentLocation(tileOrientation);
//                    break;
//            }
//            setState(secLocation, animal2);
//        }
    }


    /**
     * Determine whether the given constraint is satisfied by the current game state.
     * <p>
     * A constraint is satisfied if there exists an animal on the board that meets each of the requirements of the
     * Constraint.
     * <p>
     * That is, there is an animal of the correct type *and* colour, that eats the correct colour of bug.
     * <p>
     * If an animal is facing an empty location on the board, this animal does not satisfy a constraint. However, an
     * animal that is facing off the board does satisfy the constraint that an animal does *not* eat a bug.
     *
     * @param constraint The constraint to check
     * @return True if the constraint is satisfied, otherwise false.
     */
    public boolean isConstraintSatisfied(Constraint constraint) {
        // FIXME Task 9
        int a = 0;
        label1:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Animal animal = getState()[i][j].getAnimal();
                Animal animalRequest = constraint.getAnimal();
                Orientation orientation = animal.getOrientation();

                if (animal.getType().equals(animalRequest.getType())
                        && getState()[i][j].getColour().equals(constraint.getColour())) {//find the animal to test on the board
                    switch (animal.getOrientation()) {
                        case WEST:
                            if (constraint.getBug().equals(Colour.NONE)) {//animal does *not* eat a bug
                                a = 1;
                                break label1;
                            } else if ((i - 1 >= 0) && getState()[i][j - 1].getColour().equals(constraint.getBug())
                                    && !getState()[i][j - 1].getAnimal().getType().equals(Type.NONE)) {
                                a = 1;
                                break label1;
                            } else {
                                a = 0;
                            }
                            break;
                        case SOUTH:
                            if (constraint.getBug().equals(Colour.NONE)) {
                                a = 1;
                                break label1;
                            } else if ((j + 1 <= 2) && getState()[i + 1][j].getColour().equals(constraint.getBug())
                                    && !getState()[i + 1][j].getAnimal().getType().equals(Type.NONE)) {
                                a = 1;
                                break label1;
                            } else {
                                a = 0;
                            }
                            break;
                        case EAST:
                            if (constraint.getBug().equals(Colour.NONE)) {
                                a = 1;
                                break label1;
                            } else if ((j + 1 <= 2) && getState()[i][j + 1].getColour().equals(constraint.getBug())
                                    && !getState()[i][j + 1].getAnimal().getType().equals(Type.NONE)) {
                                a = 1;
                                break label1;
                            } else {
                                a = 0;
                            }
                            break;
                        case NORTH:
                            if (constraint.getBug().equals(Colour.NONE)) {
                                a = 1;
                                break label1;
                            } else if ((i - 1 >= 0) && getState()[i - 1][j].getColour().equals(constraint.getBug())
                                    && !getState()[i - 1][j].getAnimal().getType().equals(Type.NONE)) {
                                a = 1;
                                break label1;
                            } else {
                                a = 0;
                            }
                            break;
                        case NONE:
                            a = 0;
                            break;
                    }
                }
            }
        }
        return a != 0;
    }


    /**
     * Determine whether this game's current state is a solution. For rules on solving challenge, see the README.
     * Note: isConstraintSatisfied will not be enough to solve this task. You need to check that *all* constraints
     * and the target board are simultaneously satisfied, which is not captured by the isConstraintSatisfied method.
     *
     * @return True if the current state is a solution, otherwise false.
     */
    public boolean isSolution() {
        return true; // FIXME Task 10
    }

    /**
     * Find all constraints that are not satisfied by the current placed tiles.
     * <p>
     * See isConstraintSatisfied for the definition of constraint satisfaction.
     *
     * @return An arraylist of all constraints that are not satisfied.
     */
    public ArrayList<Constraint> findOutstandingConstraints() {
        return new ArrayList<>(); // FIXME Task 11
    }


    /**
     * Given a tile, check whether its placement violates the game Challenge, specifically:
     * <p>
     * 1 - The initial state of the board is satisfied by this placement.
     * 2 - None of the constraints of the Challenge are broken by this placement.
     * <p>
     * A constraint is broken by a placement if any of the following become true:
     * 1. The orientation of an animal is not the same as the target board *unless* the tile placed is Tile B (the
     * single bug).
     * 2. There are more Frogs or Lizards of a certain colour on the board than appear in the challenge constraints.
     * For example: If there is 1 blue Frog in the challenge constraints, placing a second blue frog will violate the
     * challenge.
     * 3. If placing this tile causes a Frog/Lizard to eat/not eat a bug, the challenge is violated if this
     * constraint is not found in the set of outstanding constraints.
     *
     * @param tile the Tile placement to check
     * @return True if the placement violates the game Challenge, otherwise false.
     */
    public boolean violatesChallenge(Tile tile) {
        return false; // FIXME Task 12

    }

    /**
     * Given a tile, find all possible Locations that tile can be placed validly in the current orientation of the tile.
     * ie: Every location where that tile does not overlap another tile, and does not violate a challenge constraint.
     *
     * @param tile The given tile
     * @return An arraylist of Locations where this tile can be placed validly.
     */
    public ArrayList<Location> findCandidatePlacements(Tile tile) {
        return new ArrayList<>(); // FIXME Task 13
    }

    /**
     * Convert the tilePlacements array into a string for testing purposes.
     *
     * @return A string representing the tile placements.
     */
    public String tilePlacementsToString() {
        List<Tile> seen = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Tile[] row : placedTiles) {
            for (Tile t : row) {
                if (!(t == null)) {
                    if (!seen.contains(t)) {
                        seen.add(t);
                    }
                }
            }
        }
        Collections.sort(seen);
        for (Tile t : seen) {
            sb.append(t.toStateString());
        }
        return sb.toString();
    }

    /**
     * Convert the state into a string for testing purposes
     *
     * @return A string representing the state
     */
    public String stateToString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                sb.append(state[row][col].toStateString());
            }
        }
        return sb.toString();
    }
}
