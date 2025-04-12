package petespike.model;

import java.util.Objects;

public class Position {
    private int row;
    private int col;

    /**
     * Constructor
     * @param row
     * @param col
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * returns the row
     * @return the row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * returns the column
     * @return the column
     */
    public int getCol() {
        return this.col;
    }

    /** 
     * toString method
     * @return String representation of class
     */
    @Override
    public String toString(){
        return "Position [row =" + row + ", col =" + col + "]";
    }

    @Override
        public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Dertermines if instances of the same class are equivalent
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position)obj;
            return this.row == other.row && this.col == other.col;
        } else {
            return false;
        }
    }

    

}
