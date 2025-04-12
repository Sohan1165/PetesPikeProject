package petespike.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeObserver;
import petespike.model.Position;


public class PetesPikeGUI extends Application implements PetesPikeObserver{
    private PetesPike game;
    private GridPane gridPane;
    private Label status;
    private Label moveCount;
    private Map<Position, Button> buttonMap;
    public Text statusText;
    public Text moves;
    private Image emptyImage;
    private Position selectedPosition;
    private Button newGame = new Button("New Puzzle");
    private Button reset = new Button("Reset");

    private TextField filenameField = new TextField();
    private static final String IMAGE_PATH = "file:images/";
    private static final Image redGoatImage = new Image(IMAGE_PATH +"redGoat.png");
    private static final Image greenGoatImage = new Image(IMAGE_PATH +"greenGoat.png");
    private static final Image blueGoatImage = new Image(IMAGE_PATH +"blueGoat.png");
    private static final Image goldGoatImage = new Image(IMAGE_PATH +"goldGoat.png");
    private static final Image orangeGoatImage = new Image(IMAGE_PATH +"orangeGoat.png");
    private static final Image yellowGoatImage = new Image(IMAGE_PATH +"yellowGoat.png");
    private static final Image peteImage = new Image(IMAGE_PATH + "pete.png");
    private static final Image mountainImage = new Image(IMAGE_PATH + "mountain.png");
    private static final Image cyanGoatImage = new Image(IMAGE_PATH +"cyanGoat.png");
    private static final Image magentaGoatImage = new Image(IMAGE_PATH +"magentaGoat.png");
    private static final Image purpleGoatImage = new Image(IMAGE_PATH +"purpleGoat.png");

    // private static final Image rightArrow = new Image(IMAGE_PATH + "right_arrow.png");
    // private static final Image leftArrow = new Image(IMAGE_PATH + "left_arrow.png");
    //private static final Image upArrow = new Image(IMAGE_PATH + "upArrow.png");
    // private static final Image downArrow = new Image(IMAGE_PATH + "down_arrow.png");

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        game = new PetesPike();
        game.registerObserver(this);
        
        // Setup individual components
        HBox commands = setupCommands(); 
        GridPane directionControls = setupDirectionControls(); // Directional buttons
        HBox gameControls = setupGameLoadControls(); 
        VBox controlPanel = new VBox(20, directionControls, commands);//, gameControls); 
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(10));

        VBox board = setupBoard();
        board.setPadding(new Insets(20));
        board.setAlignment(Pos.CENTER);
        board.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Combine board and control panel into a horizontal layout
        HBox mainLayout = new HBox(20, board, controlPanel);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        VBox finalLayout = new VBox(20, gameControls, mainLayout);

        Scene scene = new Scene(finalLayout, 1000, 600); 
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pete's Pike");
        primaryStage.show();

        updateBoard();

    }
    
     /**
      * generally for commands such as reset and hints, etc in the GUI
      * @return commandBox
      */

    private HBox setupCommands() { 
        Button hint = new Button("Get Hint");
        hint.setOnAction(e -> showHint());

        Button solve = new Button("SOLVE");
        solve.setOnAction(e -> solveGame());
        

        HBox commandBox = new HBox(10);
        commandBox.setAlignment(Pos.CENTER);
        commandBox.getChildren().addAll(hint, solve);

        return commandBox;
    }

    /**
     * this is for setting up the board where we just need to measure the board rows and columns. 
     * @return board
     */

    private VBox setupBoard() {
        buttonMap = new HashMap<>();
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        int numRows = game.getRows();
        int numCols = game.getCols();

        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numCols; col++){
                Button eachCell = makeButton(row, col);
                buttonMap.put(new Position(row, col), eachCell);
                eachCell.setMinSize(50, 50);
                eachCell.setMinHeight(25);
                eachCell.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                gridPane.add(eachCell, row, col);
            }
        }

        status = new Label("Start game");
        moveCount = new Label("Moves: 0");

        VBox board = new VBox(40, gridPane, status, moveCount);

        board.setAlignment(Pos.CENTER);

        return board;
    }

    
    /**
     * The helper method for reseting the current game
     */
    private void resetGame() {
        try {
            // Reinitialize the game
            String currentFilename = filenameField.getText();
            game = new PetesPike(currentFilename); // Start with a fresh instance of the game
            game.registerObserver(this); // Register this GUI as an observer
    
            // Reset the UI elements
            updateBoard();
            moveCount.setText("Moves: 0");
            status.setText("Game has been reset!");
    
            // Clear selected position
            selectedPosition = null;
        } catch (PetesPikeException e) {
            status.setText("Error resetting game: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    /**
     * method to start a new game based on the filename entered
     * @param filepath the filepath of the puzzle (game to be selected)
     * @throws PetesPikeException
     */
    private void newGame(String filepath) throws PetesPikeException {
        String filename = filenameField.getText();

        if (filename != null) {
            try {
                this.game = new PetesPike(filename);
                this.game.registerObserver(this);
        
                updateBoard();

                moveCount.setAccessibleHelp(filename);
                moveCount.setText("Moves: " + game.getMoveCount());
                status.setText("New Game Has Been Started");

        } catch (PetesPikeException ppe) {
            status.setText("Unable to Load New Game: " + ppe.getMessage());
        }
        
        } else {
            status.setText("Please enter a filename path");
        }
    }

    /**
     * Sets up the filenamefieild and the new game button
     * @return
     */
    private HBox setupGameLoadControls() {
        HBox gameLoadControls = new HBox(10);
        gameLoadControls.setAlignment(Pos.TOP_CENTER);
        gameLoadControls.setPadding(new Insets(10));

        reset.setOnAction(e -> resetGame());

        this.newGame.setOnAction(e -> {
            try {
                newGame(null);
            } catch (PetesPikeException e1) {
                e1.printStackTrace();
            }
        });

        filenameField.setText("enter game file here");
        filenameField.setPrefWidth(500);


        gameLoadControls.getChildren().addAll(reset, filenameField, newGame);
        return gameLoadControls;
    }


    /**
     * When a sucessfull move is completed 
     */

    @Override
    public void pieceMoved(Position from, Position to) {
        try {
            updateBoard();
        } catch (PetesPikeException e) {
            e.printStackTrace();
        }
    }

    public void moveSelectedPiece(Direction direction) {

        if (selectedPosition != null) {
            try {
                game.makeMove(new Move(selectedPosition, direction));
                updateBoard();
                if (game.getGameState() == GameState.WON) {
                    status.setText("congratulations, you scaled the mountain!!");
                }
                moveCount.setText("Moves: " + game.getMoveCount());
            } catch (PetesPikeException ppe) {
                status.setText("invalid move: " + ppe.getMessage());
            }
        } else {
            status.setText("No piece selected. select a piece first");
        }
    }

    /**
     * method to give a hint for move
     */
    private void showHint() {
        try {
            // Get the list of possible moves
            List<Move> possibleMoves = game.getPossibleMoves();

            // Check if moves are available
            if (possibleMoves.isEmpty()) {
                status.setText("No valid moves available.");
                return;
            }

            // Use the first move as the hint
            Move move = possibleMoves.get(0);
            Position startPosition = move.getPosition();
            Direction direction = move.getDirection();

            // Update the status message
            status.setText("Hint: Move from " + startPosition + " to " + direction);

            // Display the hint on the board
            //highlightHint(startPosition, direction);
        } catch (Exception e) {
            status.setText("Error showing hint: " + e.getMessage());
        }
    }



    /**
     * this allows for people to control where pieces move based on button press.
     */

    private GridPane setupDirectionControls() {

        Button upButton = new Button("↑");
        upButton.setOnAction(e -> moveSelectedPiece(Direction.UP));
        // ImageView ub = new ImageView(upArrow);
        // Ill come back and add make images
        // upButton.setGraphic(ub);

        Button downButton = new Button("↓");
        downButton.setOnAction(e -> moveSelectedPiece(Direction.DOWN));

        Button leftButton = new Button("←");
        leftButton.setOnAction(e -> moveSelectedPiece(Direction.LEFT));

        Button rightButton = new Button("→");
        // ImageView rb = new ImageView(rightArrow);
        // rb.prefHeight(4);
        // rb.prefHeight(4);
        // rightButton.setGraphic(rb);
        rightButton.setOnAction(e -> moveSelectedPiece(Direction.RIGHT));

        GridPane directionControls = new GridPane();
        directionControls.add(upButton, 1, 0);
        directionControls.add(leftButton, 0, 1);
        directionControls.add(rightButton, 2, 1);
        directionControls.add(downButton, 1, 2);
        directionControls.setHgap(5);
        directionControls.setVgap(5);
        directionControls.setAlignment(Pos.CENTER);

        return directionControls;

    }

    /**
     * this is to create basic buttons
     * @param row
     * @param col
     * @return button
     */

    private Button makeButton(int row, int col){
        Button button = new Button("Choose a square");
        button.setPrefSize(50, 50);
        button.setOnAction(event -> handleButton(row, col));

        ImageView imageView = new ImageView();
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setGraphic(imageView); 
        button.setPadding(new Insets(0));
        return button;
    }

    /**
     * handling buttons and updating board, and seeing if they made illegal move 
     * and also applies for most of the functions
     * @param row
     * @param col
     */

    private void handleButton(int row, int col) {
        try {
            game.makeMove(new Move(new Position(row, col), Direction.UP));
            updateBoard();
        } catch (PetesPikeException e) {System.out.println("Invalid move: " + e.getMessage());}
    }

    private void updateGameState(GameState state) {
        if (state == GameState.WON || state == GameState.NO_MOVES) {
            // Disable all buttons to prevent further moves
            for (Button button : buttonMap.values()) {
                button.setDisable(true);
            }
        } else {
            // Re-enable buttons if the game restarts
            for (Button button : buttonMap.values()) {
                button.setDisable(false);
            }
        }
    }

    /**
     * updates the board...
     * @throws PetesPikeException
     */

    private void updateBoard() throws PetesPikeException {
        int rows = game.getRows();
        int cols = game.getCols();
        gridPane.getChildren().clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Position position = new Position(row, col);
                Button button = new Button();
                button.setMinSize(50, 50);
                char symbol = game.getSymbolAt(position);
                
                ImageView imageView = new ImageView(getImageForSymbol(symbol));
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                button.setGraphic(imageView);
                
                button.setOnAction(event -> {
                    if (Character.isDigit(symbol) || symbol == PetesPike.PETE_SYMBOL) {
                        selectedPosition = position;
                        status.setText("Piece selected at " + position + ", choose direction.");
                    }
                });
                
                gridPane.add(button, col, row);
            }
        }

        for (Map.Entry<Position, Button> entry : buttonMap.entrySet()) {
            Position pos = entry.getKey();
            Button button = entry.getValue();
    
            // Clear any existing hint graphic
            button.setGraphic(null);
    
            // Update the button based on the current board state
            char symbol = game.getSymbolAt(pos);
            Image image = getImageForSymbol(symbol);
            if (image != null) {
                button.setGraphic(new ImageView(image));
            } else {
                button.setText(Character.toString(symbol));
            }
        }
    
        moveCount.setText("Moves: " + game.getMoveCount());
        status.setText("Game State: " + game.getGameState());  // Update the status text based on the game state
        updateGameState(game.getGameState());
        

    }

    /**
     * Get the symbol images for the board (Goats, Pete, and Mountain)
     * @param symbol the symbol
     * @return the image needed for that symbol
     */
    private Image getImageForSymbol(char symbol) {
        switch (symbol) {
            case 'P':
                return peteImage;
            case 'T':
                return mountainImage;
            case '-':
                return emptyImage;
            case '0':
                return blueGoatImage;
            case '1':
                return orangeGoatImage;
            case '2':
                return greenGoatImage;
            case '3':
                return goldGoatImage;
            case '4':
                return cyanGoatImage;
            case '5':
                return magentaGoatImage;
            case '6':
                return purpleGoatImage;
            case '7':
                return redGoatImage;
            case '8':
                return yellowGoatImage;
            default:
                return emptyImage;
                
        }
    }

    public void solveGame() {
        
    }

    public static void main(String[] args) {
        launch(args);
    }


}
