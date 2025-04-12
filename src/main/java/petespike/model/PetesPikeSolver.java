package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver> {
    private PetesPike petesPike;
    private List<Move> moves;

    public PetesPikeSolver(PetesPike petesPike, List<Move> moves) {
        this.petesPike = new PetesPike(petesPike); // Ensure deep copy
        this.moves = new ArrayList<>(moves);  // Deep copy the moves list
    }

    @Override
    public Collection<PetesPikeSolver> getSuccessors() {
        List<PetesPikeSolver> successors = new ArrayList<>();
        List<Move> possibleMoves = petesPike.getPossibleMoves();

        System.out.println("Possible Moves: " + possibleMoves);

        for (Move move : possibleMoves) {
            PetesPike copy = new PetesPike(petesPike);

            try {
                copy.makeMove(move);
                List<Move> newMoves = new ArrayList<>(moves);
                newMoves.add(move);
                PetesPikeSolver successor = new PetesPikeSolver(copy, newMoves);
                successors.add(successor);

                //System.out.println("Successor Moves: " + successor.getMoves());
            } catch (PetesPikeException e) {
            }
        }

        return successors;
    }


    public List<Move> getMoves() {
        return moves;
    }

    @Override
    public boolean isValid() {
        return petesPike.getGameState() != GameState.NO_MOVES;
    }

    @Override
    public boolean isGoal() {
        return petesPike.getPetePosition().equals(petesPike.getMountainTop());
    }

    @Override
    public String toString() {
        return "Moves: " + moves + "\n" + petesPike.toString();
    }

    public static PetesPikeSolver solve(PetesPike petesPike, boolean debug) {
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(debug);
        PetesPikeSolver initialConfig = new PetesPikeSolver(petesPike, new ArrayList<>());
        return backtracker.solve(initialConfig);
    }

    public static PetesPikeSolver solve(PetesPike petesPike) {
        return solve(petesPike, false);
    }
}
