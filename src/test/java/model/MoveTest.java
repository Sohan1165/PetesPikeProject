package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.Position;

public class MoveTest {

    @Test
    public void testgetDirection() {
        //setup
        Position position = new Position(2, 4);
        Move m1 = new Move(position, Direction.UP);
        Direction expected = Direction.UP;

        //invoke
        Direction actual = m1.getDirection();

        //analyze
        assertEquals(expected, actual);
    }
    
    @Test
    public void testgetPosition() {
        //setup
        Position position = new Position(2, 4);
        Move m1 = new Move(position, Direction.UP);
        Position expected = position;

        //invoke
        Position actual = m1.getPosition();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        //setup
        Position position = new Position(3, 4);


        Move m1 = new Move(position, Direction.UP);
        String expected = "Move [position=Position [row =3, col =4], direction=UP]";

        //invoke 
        String actual = m1.toString();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        //setup
        Position position = new Position(2, 3); 
        Position position2 = new Position(2, 3); 

        Move m1 = new Move(position, Direction.DOWN);
        Move m2 = new Move(position2, Direction.DOWN);
        boolean expected = true;

        //invoke
        boolean actual = m1.equals(m2);

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsFalse() {
        //setup
        Position position1 = new Position(3, 4);
        Position position2 = new Position(2, 4);

        Move m1 = new Move(position1, Direction.UP);
        Move m2 = new Move (position2, Direction.DOWN);
        boolean expected = false;

        //invoke 
        boolean actual = m1.equals(m2);

        //analyze
        assertEquals(expected, actual);
    }
}
