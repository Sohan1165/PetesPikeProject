package petespike.view;
import java.util.List;
import java.util.Scanner;

import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeSolver;
import petespike.model.Position;

public class PetesPikeCLI {
    private PetesPike game;
    private String currentFilename;

    public PetesPikeCLI(PetesPike game, String filename) {
        this.game = game;
        this.currentFilename = filename;
    }

    /**
     * displays the list of commands 
     */
    public void help() {
        System.out.println("Commands: ");
        System.out.println("\thelp - this help menu");
        System.out.println("\tboard - display current board");
        System.out.println("\treset - reset current puzzle to the start");
        System.out.println("\tnew - <puzzle_filename> - start a new puzzle");
        System.out.println("\tmove - <row> <col> <direction> - move the piece at <row>, <col> " 
                        +    "\n\t\twhere <direction> one of u(p), d(own), l(eft), r(ight)");
        System.out.println("\thint - get a valid move, if one exists");
        System.out.println("\tsolve - automatically play the game to the end if a solution exists");
        System.out.println("\tquit - quit");
    }
    
    /**
     * gets a valid move if it exists
     * @param game
     * @throws PetesPikeException 
     */
    public void hint() {
        try {
            // Try to solve the game and get the solution
            PetesPikeSolver solution = PetesPikeSolver.solve(game);
    
            // If no solution is found or no moves are available, inform the player
            if (solution == null || solution.getMoves().isEmpty()) {
                System.out.println("No valid move found.");
                return;
            }
    
            // If there is a valid solution, show the first move
            Move nextMove = solution.getMoves().get(0);
            Position from = nextMove.getPosition();
            Direction direction = nextMove.getDirection();
    
            // Print the hint in the correct format
            System.out.println("Try: (" + from.getRow() + "," + from.getCol() + ") " + direction);
        } catch (Exception e) {
            System.out.println("Error generating hint: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Attempts to solve the with th 
     */
    public void solve() {
        try {
            PetesPikeSolver solution = PetesPikeSolver.solve(game, true);  // Pass debug flag
            
            if (solution != null) {
                System.out.println("Solution found!");
                List<Move> moves = solution.getMoves();
                for (Move move : moves) {
                    System.out.println("Move: " + move);
                    game.makeMove(move);  // Apply each move to the current game state
                    System.out.println(game);  // Display the updated board
                }
            } else {
                System.out.println("No solution found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while solving the game: " + e.getMessage());
        }
    }
    
    

    /**
     * ends the game
     */
    public static void quit() {
        System.out.println("Goodbye!");
    }

    /**
     * displays the board 
     */
    public void board() {
        System.out.print(" ");
        for (int col = 0; col < this.game.getCols(); col++) {
            System.out.print(col + " ");
        }
        System.out.println();
        
        for (int row = 0; row < this.game.getRows(); row++) {
            System.out.print(row + " ");
            for (int col = 0; col < game.getCols(); col++) {
                try {
                    char symbolColor = game.getSymbolAt(new Position(row, col));
                    String color = symbol(symbolColor);
                    System.out.print(color + " ");
                } catch (PetesPikeException e) {
                    System.out.println(AsciiColorCodes.RED + "? " + AsciiColorCodes.RESET);
                }
            }
            System.out.println();
        }
        System.out.println("Moves: " + game.getMoveCount());
    }
    
    /** 
     * @param symbol
     * @return String
     */
    public static String symbol(char symbol) {
        // Pete's symbol
        if (symbol == PetesPike.PETE_SYMBOL) {
            return AsciiColorCodes.RED + "P" + AsciiColorCodes.RESET;
        }
        
        if (PetesPike.GOAT_SYMBOLS.contains(symbol)) {
            String goatColor = getGoatColor(symbol);
            return goatColor + "G" + AsciiColorCodes.RESET;
        }
        
        if (symbol == PetesPike.MOUNTAINTOP_SYMBOL) {
            return AsciiColorCodes.BLUE + "+" + AsciiColorCodes.RESET;
        }
        
        if (symbol == PetesPike.EMPTY_SYMBOL) {
            return AsciiColorCodes.LT_GRAY + "-" + AsciiColorCodes.RESET;
        }
        
        return Character.toString(symbol);
    }
    
    /** 
     * @param goatSymbol
     * @return String
     */
    private static String getGoatColor(char goatSymbol) {
        // Return color based on the goat symbol
        switch (goatSymbol) {
            case '0': return AsciiColorCodes.BLUE;    
            case '1': return AsciiColorCodes.ORANGE;  
            case '2': return AsciiColorCodes.GREEN;   
            case '3': return AsciiColorCodes.GOLD;   
            case '4': return AsciiColorCodes.CYAN;     
            case '5': return AsciiColorCodes.MAGENTA;  
            case '6': return AsciiColorCodes.PURPLE;     
            case '7': return AsciiColorCodes.RED;      
            case '8': return AsciiColorCodes.YELLOW;     
            case '9': return AsciiColorCodes.LT_GRAY;  
            default: return AsciiColorCodes.RESET;     // Default
        }
    }
    
    /**
     * move the piece in a certain direction
     * @param command
     */
    public void move(String command) {
        if (game.getGameState() == GameState.WON) {
            System.out.println("There must be an active game to use this command");
            return;
        }
        if (game.getGameState() == GameState.NO_MOVES) {
            System.out.println("No moves left. Reset or start a new game.");
            return;
        }
    
        try {
            String[] parts = command.split("\\s+");
    
            if (parts.length != 4) {
                System.out.println("Invalid command");
                return;
            }
    
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            String directionStr = parts[3].toLowerCase();
            
            // added functionality to using the lowercase letters like in the example. 
            switch(directionStr) {
                case "u":
                    directionStr = "UP";
                    break;
                case "d":
                    directionStr = "DOWN";
                    break;  
                case "l":
                    directionStr = "LEFT";
                    break;
                case "r":
                    directionStr = "RIGHT";
                    break;
                
            }

            Direction direction;
            try {
                direction = Direction.valueOf(directionStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid direction. Allowed directions are: u(p), l(eft), r(ight), d(own)");
                return;
            }
    
            Position position = new Position(row, col);
            Move move = new Move(position, direction);
    
            game.makeMove(move);
            board();  // Display board and the move

    
            // Display game end messages if applicable
            if (game.getGameState() == GameState.WON) {
                System.out.println("Congratulations! You have scaled the Mountain!!!");
            } else if (game.getGameState() == GameState.NO_MOVES) {
                System.out.println("No moves available.");
            }
    
        } catch (NumberFormatException e) {
            System.out.println("Invalid row or column. They should be integers.");
        } catch (PetesPikeException e) {
            System.out.println("Move failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * resets the board to the initial board configuration
     */
    public void reset() {
        try {
            this.game = new PetesPike(currentFilename); 
            System.out.println("Game has been reset.");
            board();
        } catch (PetesPikeException e) {
            System.out.println("Error resetting the game: " + e.getMessage());
        }
    }
    
    /**
     * start a new game with a provided filename
     * @param filename the provided filename
     */
    public void newGame (String filename) {
        try {
            this.game = new PetesPike(filename);
            this.currentFilename = filename;
            board();
        } catch (PetesPikeException e) {
            System.out.println(e.getMessage());
        }
      

    }
    


    public static void main(String[] args) throws PetesPikeException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter a filename: ");
            String filename = scanner.nextLine();
            PetesPikeCLI cli = new PetesPikeCLI(new PetesPike(filename), filename);
            boolean finished = false;
            System.out.println("Puzzle filename: " + filename);
            cli.help();
            cli.board();
            while (!finished) {
                System.out.println("Command: ");
                String command = scanner.nextLine().trim();
                String[] parts  = command.split("\\s+");
                String enteredCommand = parts[0].toLowerCase();

                switch(enteredCommand) {
                    case "help":
                        cli.help();
                        break;
                    case "board":
                        cli.board();
                        break;
                    case "reset":
                        cli.reset();
                        break;
                    case "new":
                        if (parts.length > 1) {
                            String newFilename = parts[1];
                            cli.newGame(newFilename);
                        }
                        break;
                    case "move":
                        if (parts.length == 4) {
                            cli.move(command);
                        }
                        break;
                    case "hint":
                        cli.hint();
                        break;
                    case "solve":
                        cli.solve();
                        break;
                    case "quit":
                        finished = true;
                        quit();
                        break;
                    default:
                        System.out.println("Invalid Command: " + command);
                }


            }

        } catch (PetesPikeException e) {
            System.out.println(e.getMessage());
        }
    }
}
