package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class PetesPikeTest {
    @Test
    public void testGetMoveCount() throws PetesPikeException {
        // setup
        PetesPike pike = new PetesPike("data/petes_pike_5_5_2_0.txt");
        int expected = 0;

        // invoke
        int actual = pike.getMoveCount();

        // analyze
        assertEquals(expected, actual);

    }

    @Test
    public void testGetState() throws PetesPikeException {
        // setup
        PetesPike pike = new PetesPike("data/petes_pike_5_5_2_0.txt");
        GameState expected = GameState.NEW;

        // invoke
        GameState actual = pike.getGameState();

        //ananlyze
        assertEquals(expected, actual);
    }

    @Test
    public void getMountainTop() throws PetesPikeException {
        // setup
        PetesPike pike = new PetesPike("data/petes_pike_5_5_2_0.txt");
        Position expected = new Position(2, 2);
        
        // invoke
        Position actual = pike.getMountainTop();

        // analyze
        assertEquals(expected, actual);

    }

    @Test
    public void testMove() throws PetesPikeException {
        // setup
        PetesPike pike = new PetesPike("data/petes_pike_5_5_4_0.txt");
        int expected = 1;

        // invoke
        pike.makeMove(new Move(new Position(0, 2), Direction.DOWN));


        // analyze
        assertEquals(expected, pike.getMoveCount());
    }



}
