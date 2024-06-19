package comp1110.ass1.gui;

import comp1110.ass1.*;
import comp1110.ass1.ColourCatch;
import comp1110.ass1.Location;
import comp1110.ass1.Orientation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static comp1110.ass1.Orientation.*;

public class Game extends Application {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 650;

    private static final int SQUARE_SIZE = 150;
    private static final int BOARD_SIZE = SQUARE_SIZE * 3;
    private static final int MARGIN_X = 20;
    private static final int MARGIN_Y = 10;

    private static final long ROTATION_THRESHOLD = 100; // Allow rotation every 100 ms

    private static final String URI_BASE = "assets/"; // Where to find assets

    private static final boolean RULES = true; // boolean to turn on and off rules usage.

    private static boolean COLOUR_ASSISTANCE = false;

    private static final String INSTRUCTIONS = """
            Select a challenge or choose a difficulty level and receive a random challenge.
            Place all tiles on the board such that the constraints are satisfied. See README for more information on constraint satisfiability.
            Click on a tile and drag it to move the tile. Release the mouse to place it.
            Whilst dragging a tile, press `R` or scroll the mouse wheel to rotate the tile 90 degrees clockwise.
            You can only move one piece at a time.
            If you get stuck, press `restart` to reset the board.
            """;

    /* A list of the draggable tiles in the game */
    private final List<DraggableTile> dTiles = new ArrayList<>();

    /* Node Groups */
    private final Group root = new Group();
    private final Group tiles = new Group();
    private final Group board = new Group();
    private final Group target = new Group();
    private final Group controls = new Group();
    private final ArrayList<Text> assist = new ArrayList<>();
    private final Group colourAssist = new Group();

    private final Group instructions = new Group();
    private final Text completionText = new Text("Well done!");

    private final Text constraint = new Text("");
    private final Button colourToggle = new Button("Toggle colour \nassistance: OFF");


    /* The underlying game */
    ColourCatch game;

    /* Define a drop shadow effect that will apply to tiles */
    private static final DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */
    static {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    private final BoardGrid boardGrid = new BoardGrid(); // The boardGrid that will represent the board.

    /*
     * Add the board to the root node and bring it to the front.
     */ {
        board.getChildren().addAll(boardGrid.squares); // add all squares in the boardGrid to root.
        board.toFront();
    }

    /**
     * This class defines a BoardGrid, which is used to represent GUI-interactive board.
     */
    class BoardGrid {
        /**
         * Using the statically defined board colours, initialise the board.
         */
        public void setBoard() {
            for (int i = 0; i < State.board.length; i++) {
                Colour col = State.board[i];
                Text text;
                Color c;
                switch (col) {
                    case BLUE -> {
                        c = Color.BLUE;
                        text = new Text("B");
                        text.setFill(Color.WHITE);
                    }
                    case GREEN -> {
                        c = Color.GREEN;
                        text = new Text("G");
                        text.setFill(Color.WHITE);
                    }
                    case YELLOW -> {
                        c = Color.YELLOW;
                        text = new Text("Y");
                        text.setFill(Color.BLACK);
                    }
                    case RED -> {
                        c = Color.RED;
                        text = new Text("R");
                        text.setFill(Color.WHITE);
                    }
                    default -> {
                        c = Color.GRAY;
                        text = new Text();
                        text.setFill(Color.BLACK);
                    }
                }
                BoardSquare s = this.squares.get(i);
                s.setFill(c);
                s.setOpacity(1);
                s.text = text;
                s.text.toFront();
                s.text.setFont(Font.font("verdana", FontWeight.BOLD, 20));
                s.text.setLayoutX(s.getLayoutX() + ((double) SQUARE_SIZE / 2) - 5);
                s.text.setLayoutY(s.getLayoutY() + ((double) SQUARE_SIZE / 2) + 5);
                assist.add(text);
            }
        }

        static class BoardSquare extends Rectangle {
            int x, y;
            Text text;

            BoardSquare(int x, int y) {
                super(SQUARE_SIZE, SQUARE_SIZE);
                this.x = x;
                this.y = y;
                this.setFill(Color.GRAY);
                this.setScaleX(1);
                this.setScaleY(1);
                this.setStroke(Color.BLACK);
                this.setStrokeWidth(1);
                gLayout layout = gLayout.getLoc(x, y);
                this.relocate(layout.x, layout.y);
            }

            /**
             * Find the distance between this square and the given x and y coordinate
             *
             * @param x the x coordinate
             * @param y the y coordinate
             * @return the distance between this square and the given coordinates.
             */
            private double distance(double x, double y) {
                return Math.sqrt((this.getLayoutX() - x) * (this.getLayoutX() - x)
                        + (this.getLayoutY() - y) * (this.getLayoutY() - y));
            }
        }

        List<BoardSquare> colouredSquares;
        double baseOpacity = 1;
        List<BoardSquare> squares;

        /**
         * Create the Board
         */
        BoardGrid() {
            squares = new ArrayList<>();
            for (int c = 0; c < 3; c++) {
                for (int r = 0; r < 3; r++) {
                    var square = new BoardSquare(r, c);
                    square.setFill(Color.GRAY);
                    squares.add(square);
                }
            }
        }

        /**
         * highlight the square closest to the given (x,y) coordinate
         *
         * @param x x layout location
         * @param y y layout location
         */
        void highlightClosestSquare(double x, double y, Orientation orientation, int tileID) {
            if (this.colouredSquares == null) {
                this.colouredSquares = getAllClosestSquares(x, y, orientation, tileID);
            }
            if (this.colouredSquares != null) {
                for (BoardSquare s : this.colouredSquares) {
                    if (s != null) {
                        s.setStroke(Color.BLACK);
                        s.setStrokeWidth(1); // reset stroke
                        s.setOpacity(baseOpacity); // reset opacity
                    }
                }
                this.colouredSquares = this.getAllClosestSquares(x, y, orientation, tileID);
                if (this.colouredSquares != null) {
                    for (BoardSquare s : this.colouredSquares) {
                        if (s != null) {
                            s.setOpacity(0.75); // highlight
                            s.setStrokeWidth(3.5);
                            s.setStroke(Color.CORAL);
                        }
                    }
                }
            }
        }

        public void clearClosestSquares() {
            if (colouredSquares != null) {
                for (BoardSquare s : colouredSquares) {
                    if (s != null) {
                        s.setOpacity(baseOpacity);
                        s.setStroke(Color.BLACK);
                        s.setStrokeWidth(1);
                    }
                }
            }
        }

        BoardSquare getClosestSquare(double x, double y, Orientation orientation, int tileID) {
            if (tileID != 1) {
                if (orientation == EAST || orientation == WEST) {
                    x = x - (0.5 * SQUARE_SIZE);
                    y = y + (0.5 * SQUARE_SIZE);
                }
            }
            double finalY = y;
            double finalX = x;
            BoardSquare closest = squares.stream().reduce(squares.get(0), (l, r) ->
                    (l.distance(finalX, finalY) > r.distance(finalX, finalY)) ? r : l
            );
            if (closest.distance(x, y) > SQUARE_SIZE) {
                return null;
            } else {
                return closest;
            }
        }

        List<BoardSquare> getAllClosestSquares(double x, double y, Orientation orientation, int tileID) {
            BoardSquare closest = getClosestSquare(x, y, orientation, tileID);
            double xOffset = 0;
            double yOffset = 0;
            switch (orientation) {
                case NORTH, SOUTH -> {
                    xOffset = 0;
                    yOffset = SQUARE_SIZE;
                }
                case EAST, WEST -> {
                    xOffset = SQUARE_SIZE;
                    yOffset = 0;
                }
                case NONE -> {
                    xOffset = 0;
                    yOffset = 0;
                }
            }
            if (closest == null) {
                return null;
            }
            List<BoardSquare> squares = new ArrayList<>();
            squares.add(closest);
            if (tileID != 1) {
                BoardSquare other = getClosestSquare(x + xOffset, y + yOffset, orientation, tileID);
                squares.add(other);
            }
            return squares;
        }

    }


    /**
     * Graphical representation of tiles, constraints and targetBoard
     */
    class GTile extends ImageView {
        int tileID;

        /**
         * Construct a particular playing tile
         *
         * @param tile the int representing the tile to be created.
         */
        GTile(int tile) {
            if (tile > 4 || tile < 0) {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            this.tileID = tile;
            if (tileID == 1) {
                setFitHeight(SQUARE_SIZE);
                setFitWidth(SQUARE_SIZE);
            } else {
                setFitHeight(2 * SQUARE_SIZE);
                setFitWidth(SQUARE_SIZE);
            }
            setEffect(dropShadow);
        }

        /**
         * Create a preview tile for the target board.
         *
         * @param state A single state of the target board
         */
        GTile(State state) {
            char c = Character.toLowerCase(state.getAnimal().getType().toChar());
            switch (c) {
                case 'b' -> tileID = 1;
                case 'f' -> tileID = 5;
                case 'l' -> tileID = 6;
                default -> tileID = -1;
            }
            setImage(new Image(Objects.requireNonNull(Game.class.getResource(URI_BASE + c + ".png")).toString()));
            setRotate(getRotation(state.getAnimal().getOrientation()));
            setFitWidth(SQUARE_SIZE);
            setFitHeight(SQUARE_SIZE);
            gLayout layout = gLayout.getLoc(state.getPosition());
            this.relocate(layout.x, layout.y);
            setOpacity(0.4);
        }


        /**
         * A constructor used to build a constraint tile.
         *
         * @param tile A string representing the constraint to construct
         */
        GTile(String tile) {
            setImage(new Image(Objects.requireNonNull(Game.class.getResource(URI_BASE + tile + ".png")).toString()));
            this.tileID = -1;
            if (tile.charAt(2) == 'x') {
                setFitWidth(75);
            } else {
                setFitWidth(125);
            }
            setFitHeight(75);
            setEffect(dropShadow);
        }

    }

    /**
     * This class extends GTile with the capacity for it to be dragged, dropped and snap-to-grid.
     */
    class DraggableTile extends GTile {
        int homeX, homeY; // The position in the window where the tile should be when not on the board.
        double mouseX, mouseY; // The last known mouse positions (used when dragging)
        Location location; // The Location of the tile on the board.
        Image image; // The image for this tile
        Orientation orientation; // The current orientation of this tile
        long lastRotationTime = System.currentTimeMillis(); // only allow rotation every ROTATION_THRESHOLD (ms)
        // This caters for mice which send multiple scroll events per tick.

        /**
         * Construct a draggable tile
         *
         * @param tile the tile ID (0 to 4)
         */
        DraggableTile(int tile) {
            super(tile);
            char filename = (char) (tile + 'a');
            image = new Image((Objects.requireNonNull(Game.class.getResource(URI_BASE + filename + ".png"))).toString());
            setImage(image);
            orientation = NORTH;
            int x = BOARD_SIZE + (2 * MARGIN_X) + ((tile % 3) * (SQUARE_SIZE + 2)) + 5;
            int y = MARGIN_Y + ((tile % 2) * (2 * (SQUARE_SIZE + 1))) + 5;
            homeX = x;
            homeY = y;

            /* Event handlers */

            setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.R) {
                    hideCompletion();
                    rotate();
                    keyEvent.consume();
                    checkCompletion();
                }
            });
            setOnScroll(event -> {            // scroll to change orientation
                if (System.currentTimeMillis() - lastRotationTime > ROTATION_THRESHOLD) {
                    lastRotationTime = System.currentTimeMillis();
                    hideCompletion();
                    rotate();
                    event.consume();
                    checkCompletion();
                }
            });
            setOnMousePressed(event -> { // mouse press indicates begin of drag
                this.requestFocus();
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                boardGrid.highlightClosestSquare(this.getLayoutX(), this.getLayoutY(), this.orientation, this.tileID);

            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
                boardGrid.highlightClosestSquare(this.getLayoutX(), this.getLayoutY(), this.orientation, this.tileID);

            });
            setOnMouseReleased(event -> {     // drag is complete
                if (RULES) {
                    snapToGrid();
                    gLayout l = new gLayout(getLayoutX(), getLayoutY());
                    if (tileID == 1) {
                        location = l.gLayoutToLoc(NORTH);
                    } else {
                        location = l.gLayoutToLoc(orientation);
                    }
                    Tile t = updateTile(tileID, location);
                    game.removeTile(t);
                    if (game.isPlacementValid(t)) {
                        game.placeTile(t);
                        checkCompletion();
                        boardGrid.clearClosestSquares();
                    } else {
                        game.removeTile(t);
                        snapToHome();
                        boardGrid.clearClosestSquares();
                    }
                } else {
                    snapToGrid();
                    boardGrid.clearClosestSquares();
                }
            });
        }

        /**
         * Update the Location of the given tile
         *
         * @param ID  The ID of the tile to update
         * @param loc The new tile location
         * @return The updated Tile
         */
        Tile updateTile(int ID, Location loc) {
            Tile t = getTile(ID);
            t.setOrientation(orientation);
            t.setLocation(loc);
            return t;
        }

        /**
         * Get the tile with the corresponding ID
         *
         * @param ID the ID of the tile
         * @return the Tile object with the identifier ID.
         */
        Tile getTile(int ID) {
            for (Tile t : game.getTiles()) {
                if (ID == t.getID()) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Bad tile " + ID);
        }

        /**
         * Snap the tile to its home position and orientation North.
         */
        void snapToHome() {
            this.orientation = NORTH;
            setRotate(getRotation(orientation));
            this.setLayoutX(homeX);
            this.setLayoutY(homeY);
        }

        /**
         * Snap the tile to its nearest grid position (if it is over the grid)
         */
        void snapToGrid() {
            if (boardGrid.colouredSquares != null) {
                BoardGrid.BoardSquare snapSquare = boardGrid.colouredSquares.get(0);
                if (orientation == NORTH || orientation == SOUTH || tileID == 1) {
                    this.setLayoutX(snapSquare.getLayoutX());
                    this.setLayoutY(snapSquare.getLayoutY());
                } else {
                    this.setLayoutX(snapSquare.getLayoutX() + (0.5 * SQUARE_SIZE));
                    this.setLayoutY(snapSquare.getLayoutY() - (0.5 * SQUARE_SIZE));
                }
                this.toFront();
            } else {
                snapToHome();
            }
        }

        /**
         * Rotate the tile 90 degrees clockwise.
         */
        void rotate() {
            orientation = orientation.rotate();
            setRotate(getRotation(orientation));
        }
    }

    /**
     * Return the degree value for a given orientation
     *
     * @param o The given orientation
     * @return the number of degrees that orientation rotates by.
     */
    double getRotation(Orientation o) {
        return switch (o) {
            case NORTH, NONE -> 0.0;
            case EAST -> 90.0;
            case SOUTH -> 180.0;
            case WEST -> 270.0;
        };
    }

    /**
     * A class representing a Gui Layout.
     * This is similar to the Location class, but we require double precision.
     */
    public static class gLayout {
        double x;
        double y;

        public gLayout(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Given a Location, return the GUI layout for that location.
         * Note that in row-major order, x is the column and y is the row.
         *
         * @param x the column coordinate
         * @param y the row coordinate
         * @return the Gui Layout for this location
         */
        public static gLayout getLoc(double x, double y) {
            double layoutX = (x * SQUARE_SIZE) + MARGIN_X;
            double layoutY = (y * SQUARE_SIZE) + MARGIN_Y;

            return new gLayout(layoutX, layoutY);
        }

        /**
         * Given a position, return the GUI layout for that position
         *
         * @param pos the given position
         * @return the GUI layout for this position
         */
        public static gLayout getLoc(int pos) {
            int x = pos % 3;
            int y = pos / 3;
            double layoutX = (x * SQUARE_SIZE) + MARGIN_X;
            double layoutY = (y * SQUARE_SIZE) + MARGIN_Y;
            return new gLayout(layoutX, layoutY);
        }

        /**
         * Convert a GUI layout to the corresponding integer position on the board.
         *
         * @return (Row, Column) Location of this position.
         */
        private Location gLayoutToLoc(Orientation orientation) {
            double xOffset = 0;
            double yOffset = 0;
            if (orientation == EAST || orientation == WEST) { // account for shifting when rotated
                xOffset = -(0.5 * SQUARE_SIZE);
                yOffset = (0.5 * SQUARE_SIZE);
            }
            int col = (int) (x + xOffset - MARGIN_X) / SQUARE_SIZE;
            int row = (int) (y + yOffset - MARGIN_Y) / SQUARE_SIZE;
            if (row < 0 || row > 2 || col < 0 || col > 2) {
                return new Location(-1, -1);
            }
            return new Location(row, col);
        }
    }



    /* Initialisers */

    /**
     * Initialise the draggable tiles in the game
     */
    private void initTiles() {
        for (int i = 0; i < 5; i++) {
            dTiles.add((initialiseTile(i)));
        }
    }

    /**
     * Initialise a draggable tile of the given ID
     *
     * @param ID the ID of the tile to initialise
     * @return The constructed tile
     */
    private DraggableTile initialiseTile(int ID) {
        DraggableTile dTile = new DraggableTile(ID);
        tiles.getChildren().add(dTile);
        dTile.setOpacity(1);
        dTile.toFront();
        dTile.relocate(dTile.homeX, dTile.homeY);
        dTile.location = new Location();
        tiles.toFront();
        return dTile;
    }

    /**
     * Initialise a ColourCatch game with a randomly chosen challenge of the given difficulty
     *
     * @param difficulty The difficulty of the challenge to choose.
     */
    private void initState(int difficulty) {
        game = new ColourCatch(difficulty);
    }

    /**
     * Initialise a game of the given difficulty
     *
     * @param difficulty the given difficulty
     */
    public void initialiseGame(int difficulty) {
        hideCompletion();
        initState(difficulty);
        initChallenge();
        boardGrid.setBoard();

    }

    /**
     * Given an instance of a ColourCatch game, initialise the game.
     *
     * @param game the instance of a ColourCatch game
     */
    public void initialiseGame(ColourCatch game) {
        hideCompletion();
        this.game = game;
        initChallenge();
        boardGrid.setBoard();
    }

    /**
     * Initialise the challenge constraint and target board tiles for this game.
     */
    private void initChallenge() {
        Challenge c = game.getChallenge();
        State[] tb = c.getTargetBoard();
        if (tb != null) {
            for (State state : tb) {
                if (state != null) {
                    GTile t = new GTile(state);
                    target.getChildren().add(t);
                }
            }
        }
        initConstraints();
        target.toFront();
        tiles.toFront();
    }

    private void makeColourAssist() {
        constraint.setText(game.getChallenge().constraintsToString().substring(0, 6) + "\n" + game.getChallenge().constraintsToString().substring(6));
        constraint.setFont(Font.font("verdana", FontWeight.BOLD, 16));
        constraint.setLayoutX(MARGIN_X + 2 * SQUARE_SIZE);
        constraint.setLayoutY(2 * MARGIN_Y + BOARD_SIZE + 50);
        constraint.toFront();
    }

    private void showColourAssist() {
        colourAssist.setOpacity(1.0);
        colourAssist.toFront();
        colourToggle.setText("Toggle colour \nassistance: ON");
        COLOUR_ASSISTANCE = true;
    }

    private void hideColourAssist() {
        colourAssist.setOpacity(0.0);
        colourAssist.toBack();
        colourToggle.setText("Toggle colour \nassistance: OFF");
        COLOUR_ASSISTANCE = false;
    }


    /**
     * Initialise the constraints for this game
     */
    private void initConstraints() {
        Constraint[] constraints = game.getChallenge().getConstraints();
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        for (Constraint constraint : constraints) {
            GTile c = new GTile(constraint.toString());
            if (constraint.getAnimal().getType() == Type.FROG) {
                row1.getChildren().add(c);
            } else {
                row2.getChildren().add(c);
            }
        }
        Rectangle r = new Rectangle(3 * (SQUARE_SIZE), 175);
        r.setLayoutX(MARGIN_X);
        r.setLayoutY(2 * MARGIN_Y + BOARD_SIZE);
        r.setFill(Color.LIGHTGRAY);
        target.getChildren().add(r);
        Text constraintTitle = new Text("Challenge " + game.getChallenge().getChallengeNumber());
        constraintTitle.setFont(Font.font("verdana", FontWeight.BOLD, 20));
        constraintTitle.setLayoutX(MARGIN_X + 2 * SQUARE_SIZE);
        constraintTitle.setLayoutY(2 * MARGIN_Y + BOARD_SIZE + 20);
        target.getChildren().add(constraintTitle);
        row1.setSpacing(10);
        row1.setLayoutX(MARGIN_X + 5);
        row1.setLayoutY(3 * MARGIN_Y + BOARD_SIZE);
        row2.setSpacing(10);
        row2.setLayoutX(MARGIN_X + 5);
        row2.setLayoutY(3 * MARGIN_Y + BOARD_SIZE + 10 + 75);
        target.getChildren().add(row1);
        target.getChildren().add(row2);
    }


    /**
     * Reset this game to its starting state - with all tiles off board.
     */
    private void reset() {
        resetTiles();
        if (game != null) {
            game = new ColourCatch(game.getChallenge());
            target.getChildren().clear();
            initialiseGame(game);
            makeColourAssist();
            if (COLOUR_ASSISTANCE) {
                showColourAssist();
            }
        }
    }

    /**
     * Reset all tiles to their home position and North orientation.
     */
    private void resetTiles() {
        for (DraggableTile tile : dTiles) {
            tile.snapToHome();
        }
    }

    /**
     * Create the completion message
     */
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(MARGIN_X + 25);
        completionText.setLayoutY(MARGIN_Y + 0.5 * BOARD_SIZE);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }

    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }

    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }

    /**
     * Check game completion status
     */
    private void checkCompletion() {
        if (game.isSolution()) {
            showCompletion();
        }
    }

    /**
     * Create the controls that allow the game to be restarted and the difficulty level set.
     */
    private void makeControls() {
        Label challengeSelectLabel = new Label("Input challenge number 1-60");
        TextField challengeField = new TextField();
        challengeField.setPrefWidth(30);

        Button selectChallenge = new Button("Select Challenge");
        selectChallenge.setOnAction(e -> {
            String text = challengeField.getText();
            if (!text.isEmpty()) {
                int challenge = Integer.parseInt(text);
                if (challenge >= 1 && challenge <= 60) {
                    game = new ColourCatch(Challenge.CHALLENGES[challenge - 1]);
                    reset();
                }
            }
        });
        Button printState = new Button("Print State");
        printState.setOnAction(e -> printState());

        Slider diff = new Slider(0, 3, 0);
        diff.setSnapToTicks(true);
        diff.setShowTickMarks(true);
        diff.setMajorTickUnit(1);
        diff.setMinorTickCount(0);
        diff.setShowTickLabels(true);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);

        colourToggle.setOnAction(e -> {
            if (COLOUR_ASSISTANCE) {
                hideColourAssist();
            } else {
                showColourAssist();
            }
        });
        colourToggle.setLayoutX(WINDOW_WIDTH - (1.5 * MARGIN_X) - 2 * SQUARE_SIZE);
        colourToggle.setLayoutY(WINDOW_HEIGHT - MARGIN_Y - SQUARE_SIZE);
        controls.getChildren().add(colourToggle);

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            game = new ColourCatch((int) diff.getValue());
            reset();
        });

        Button help = new Button("help");
        help.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, INSTRUCTIONS);
            alert.setTitle("Instructions");
            alert.setHeaderText("Instructions");
            alert.show();
        });
        help.setText("Instructions");


        Button restart = new Button("Restart");
        restart.setOnAction(e -> reset());

        VBox vb = new VBox();
        vb.getChildren().addAll(challengeSelectLabel, challengeField, selectChallenge, difficultyCaption, diff,
                newGame, restart, help);
        vb.setSpacing(15);
        vb.setLayoutX(WINDOW_WIDTH - (1.5 * MARGIN_X) - SQUARE_SIZE);
        vb.setLayoutY(WINDOW_HEIGHT - MARGIN_Y - (2 * SQUARE_SIZE));
        controls.getChildren().add(vb);

    }


    /**
     * Print the string representation of this game, including the Challenge, placedTiles and state
     */
    private void printState() {
        String targetBoard = game.getChallenge().targetBoardToString();
        String constraints = game.getChallenge().constraintsToString();
        int challengeNumber = game.getChallenge().getChallengeNumber();
        String tiles = game.tilePlacementsToString();
        String state = game.stateToString();
        System.out.println("TargetBoard: \"" + targetBoard + "\", Constraints: \"" + constraints + "\", ChallengeNumber: " + challengeNumber + ", Placed Tiles: \"" + tiles + "\", State: \"" + state + "\"");

    }


    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(target);
        root.getChildren().add(tiles);
        root.getChildren().add(board);
        root.getChildren().add(instructions);


        initialiseGame(0);
        initTiles();
        makeControls();
        makeCompletion();
        makeColourAssist();
        hideColourAssist();

        assist.add(constraint);

        colourAssist.getChildren().addAll(assist);
        root.getChildren().add(colourAssist);

        tiles.toFront();

        stage.setScene(scene);
        stage.show();
    }


}
