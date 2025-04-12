package petespike.model;

public class Move {
    private Position position;
    private Direction direction;

    /**
     * Constructor
     * @param position 
     * @param direction
     */
    public Move(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    /**
     * returns the position
     * @return the position
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * returns the direction
     * @return the direction
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * ToString method
     * @return string representation of class
     */
    @Override
    public String toString() {
        return "Move [position=" + this.position + ", direction=" + this.direction + "]";
    }

    /**
     * Generates hashcode for the class
     */
    @Override
    public int hashCode() {
        final int prime = 29;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        return result;
    }

    /** 
     * dertermines if instances of the same class are equivalent
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Move)) {
            return false;
        }
        Move other = (Move) obj;
        return this.position.equals(other.position) && this.direction == other.direction;
    }

    
}
