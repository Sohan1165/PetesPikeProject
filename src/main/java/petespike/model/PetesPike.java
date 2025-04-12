package petespike.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PetesPike {
    public static final char MOUNTAINTOP_SYMBOL = 'T';
    public static final char EMPTY_SYMBOL = '-';
    public static final char PETE_SYMBOL = 'P';
    public final static Set<Character> GOAT_SYMBOLS = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8'));
    
    private int moveCount;
    public GameState gameState;
    public char[][] board;
    public Position petePosition;
    public Position mountainPosition;
    private List<Position> goatPositions = new ArrayList<>();
    private PetesPikeObserver observer;
    
    public PetesPike() {
        this.moveCount = 0;
        this.gameState = GameState.NEW;
    
        int rows = 5, cols = 5;
        this.board = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            Arrays.fill(board[row], EMPTY_SYMBOL);
        }

    }
    

    /**
     * Constructs a PetesPike object with pieces, Pete and goats, initially organized in the pattern stored in the given filename
     * @param filename given filename
     * @throws PetesPikeException
     */
    public PetesPike(String filename) throws PetesPikeException {
        this.moveCount = 0;
        this.gameState = GameState.NEW;

        this.goatPositions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine().trim();
            if (line == null) {
                throw new PetesPikeException("Invalid file");
            }

            String[] lines = line.split("\\s+");
            if (lines.length != 2) {
                throw new PetesPikeException("The Fist line must have rows and columns");
            }

            int rows = Integer.parseInt(lines[0]);
            int cols = Integer.parseInt(lines[1]);
            board = new char[rows][cols];

            for (int row = 0; row < rows; row++) {
                line = reader.readLine();
                if (line == null) {throw new PetesPikeException("End of file" + (row + 1));}
                line = line.trim();
                for (int col = 0; col < cols; col++) {
                    char symbol = line.charAt(col);
                    this.board[row][col] = symbol;
                    
                    if (symbol == PETE_SYMBOL) {petePosition = new Position(row, col);} 
                    else if (GOAT_SYMBOLS.contains(symbol)) {goatPositions.add(new Position(row, col));}
                    else if (symbol == MOUNTAINTOP_SYMBOL) {mountainPosition = new Position(row, col);}
                }
            }
        //Catch block for the IO Exception
        } catch (IOException e) {   
            throw new PetesPikeException("Error reading file: " + e.getMessage());
        }
    }

    public PetesPike (PetesPike other) {
        this.moveCount = other.moveCount;
        this.gameState = other.gameState;
        this.petePosition = new Position(other.petePosition.getRow(), other.petePosition.getCol());
        this.mountainPosition = new Position(other.mountainPosition.getRow(), other.mountainPosition.getCol());
        
        
        // new deep copy of the board
        this.board = new char[other.getRows()][other.getCols()];
        for (int row = 0; row < other.getRows(); row++) {
            for (int col = 0; col < other.getCols(); col++) {
                this.board[row][col] = other.board [row][col];
            }
        }

        // another deep copy of goats
        this.goatPositions = new ArrayList<>();
        for (Position goatPos : other.goatPositions) {
            this.goatPositions.add(new Position(goatPos.getRow(), goatPos.getCol()));
        }
        this.observer = null;
    }

    /**
     * returns the MoveCount
     * @return MoveCount
     */
    public int getMoveCount() {
        return this.moveCount;
    }

    /**
     * returns the amount of rows
     * @return
     */
    public int getRows() {
        return this.board.length;
    }

    /**
     * returns the amount of columns in the board
     * @return
     */
    public int getCols() {
        return board[0].length;
    }

    /**
     * returns the gameState
     * @return gameState
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * // helper method to determine if a position is in inbounds. 
     * @param row
     * @param col
     * @return
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }
    

    /**
     * attempts to move the piece identified by Move.position in the direction identified by Move.direction. 
     * @param move
     * @throws PetesPikeException
     */
    public void makeMove(Move move) throws PetesPikeException {
        Position startPosition = move.getPosition();
        Direction direction = move.getDirection();
    
        // Check if the starting position is within bounds
        if (!inBounds(startPosition.getRow(), startPosition.getCol())) {
            throw new PetesPikeException("Invalid starting position");
        }
    
        char startSymbol = board[startPosition.getRow()][startPosition.getCol()];
        if (startSymbol != PETE_SYMBOL && !GOAT_SYMBOLS.contains(startSymbol)) {
            throw new PetesPikeException("No piece at the specified position");
        }
    
        // Determine row and column increments based on direction
        int row = 0; 
        int column = 0;
        switch (direction) {
            case UP:    
                row = -1; 
                break;
            case DOWN:  
                row = 1; 
                break;
            case LEFT:  
                column = -1; 
                break;
            case RIGHT: 
                column = 1; 
                break;
        }
    
        int newRow = startPosition.getRow() + row;
        int newCol = startPosition.getCol() + column;
        boolean hasStoppingPiece = false;
    
        // Traverse until finding a stopping piece or reaching out of bounds
        while (inBounds(newRow, newCol)) {
            if (board[newRow][newCol] != EMPTY_SYMBOL && board[newRow][newCol] != MOUNTAINTOP_SYMBOL) {
                hasStoppingPiece = true;
                break;
            }
            newRow += row;
            newCol += column;
        }
    
        if (!hasStoppingPiece) {
            throw new PetesPikeException("There is no piece in that direction to stop you");
        }
    
        // Step back to the last valid position
        newRow -= row;
        newCol -= column;
    
        // If the piece started on the mountain top, restore the mountain top symbol  || It did not work, not quite sure how to do this. 
        // if (board[startPosition.getRow()][startPosition.getCol()] == MOUNTAINTOP_SYMBOL) {
        //     board[startPosition.getRow()][startPosition.getCol()] = MOUNTAINTOP_SYMBOL;
        // } else {
        board[startPosition.getRow()][startPosition.getCol()] = EMPTY_SYMBOL;
        //}
        board[newRow][newCol] = startSymbol;
    
        // Update the position of Pete or the Goat
        if (startSymbol == PETE_SYMBOL) {
            petePosition = new Position(newRow, newCol);
        } else {
            goatPositions.remove(startPosition);
            goatPositions.add(new Position(newRow, newCol));
        }
        // Make sure the Mountain posistion stays even when a piece leaves it. (figured it out)
        if (board[this.mountainPosition.getRow()][mountainPosition.getCol()] == EMPTY_SYMBOL) {
            board[this.mountainPosition.getRow()][mountainPosition.getCol()] = MOUNTAINTOP_SYMBOL;
        }
    
        // Increment the move count
        this.moveCount++;

        if (petePosition.equals(this.mountainPosition)) {
            gameState = GameState.WON;
        } 
        // Check if no valid moves remain for Pete
        else if (this.getPossibleMoves().isEmpty()) {
            this.gameState = GameState.NO_MOVES;
        } 
        // Otherwise, set game state to in progress
        else {
            this.gameState = GameState.IN_PROGRESS;
        }
    }
    




    /**
     * Sees where the symbols are and in which position as well as making sure it is not out of range. 
     * @param position
     * @return
     * @throws PetesPikeException
     */
    public char getSymbolAt(Position position) throws PetesPikeException{
        if (position.getRow() < 0 || position.getRow() >= this.getRows() || position.getCol() < 0 || position.getCol() >= this.getCols()) {
            throw new PetesPikeException("Position out of range");
        }

        return board[position.getRow()][position.getCol()];
    }

    /**
     * returns the position of the mountain
     * @return
     */
    public Position getMountainTop() {
        return this.mountainPosition;
    }

    public Position getPetePosition() {
        return this.petePosition;
    }
    
    /**
     * returns a List of all valid piece moves that a player may choose given the current board configuration.
     * @return a list of possible moves the player can make.
     */
    public List<Move> getPossibleMoves() {
        List<Move> possibleMoves = new ArrayList<>();
    
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Position position = new Position(row, col);
                char piece = board[row][col];
    
                if (piece == PETE_SYMBOL || GOAT_SYMBOLS.contains(piece)) {
                    for (Direction direction : Direction.values()) {
                        if (isValidMove(position, direction)) {
                            possibleMoves.add(new Move(position, direction));
                        }
                    }
                }
            }
        }
        
        return possibleMoves;
    }
       
    /**
     * helper function to determine what is a valid move. 
     * @param start
     * @param direction
     * @return
     */
    private boolean isValidMove(Position start, Direction direction) {
        int row = start.getRow();
        int col = start.getCol();
        boolean reachedObstacle = false;
    
        while (true) {
            switch (direction) {
                case UP:    row--; break;
                case DOWN:  row++; break;
                case LEFT:  col--; break;
                case RIGHT: col++; break;
            }
    
            if (!inBounds(row, col)) {
                return false;
            }
    
            if (board[row][col] == MOUNTAINTOP_SYMBOL) {
                return true; // Valid move if it reaches the mountaintop
            }
    
            if (board[row][col] != EMPTY_SYMBOL) {
                reachedObstacle = true; // Valid move if it encounters another piece
            }
    
            if (reachedObstacle && board[row][col] == EMPTY_SYMBOL) {
                return true; // Valid move if it finds empty space after an obstacle
            }
        }
    }

    /**
     * registers observer to be notified when a piece is moved
     * @param obs observer 
     */
    public void registerObserver(PetesPikeObserver obs) {
        this.observer = obs;
    }

    /**
     * notifies an observer that a piece has moved
     * @param from
     * @param to
     */
    public void notifyObserver(Position from, Position to) {
        if (this.observer != null) {
            this.observer.pieceMoved(from, to);
        }
    }
         
    
}   