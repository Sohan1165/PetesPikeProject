package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import petespike.model.Position;

public class PositionTest {

    @Test
    public void testConstructor() {
        // setup
        Position position = new Position(3, 5);
        int expectedRow = 3;
        int expectedCol = 5;

        // invoke
        int actualRow = position.getRow();
        int actualCol = position.getCol();

        // analyze
        assertEquals(expectedRow, actualRow);
        assertEquals(expectedCol, actualCol);
        
    }

    @Test
    public void testGetRow() {
        // setup
        Position position = new Position(3, 5);
        int expected = 3;

        // invoke
        int actual = position.getRow();

        // analzye
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCol() {
        // setup
        Position position = new Position(3, 5);
        int expected = 5;

        // invoke 
        int actual = position.getCol();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        // setup 
        Position position = new Position(3, 5);
        String expected = "Position [row =3, col =5]";

        // invoke
        String actual = position.toString();

        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        //setup
        Position position = new Position(3,5);
        Position position2 = new Position(3,5);
        boolean expected = true;

        //invoke
        boolean actual = position.equals(position2);

        //analyze
        assertEquals(expected, actual);
    }
}
