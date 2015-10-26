import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Bazna on 10/20/2015.
 */
public class Solver {
    private ArrayDeque<Board> solutionBoards;

    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode parent;

        SearchNode(int moves, Board board, SearchNode parent) {
            this.board = board;
            this.moves = moves;
            this.parent = parent;
        }

        int getPriority() {
            return board.manhattan() + moves;
        }

        int getMoves() {
            return moves;
        }

        Board getBoard() {
            return board;
        }

        SearchNode getParent() {
            return parent;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // check input
        if (initial == null)
            throw new NullPointerException();

        solutionBoards = new ArrayDeque<>();

        solve(initial);
    }

    private boolean isBoardInSolution(SearchNode currentNode, Board board) {
        SearchNode node = currentNode;
        while (node != null) {
            if (node.getBoard().equals(board))
                return true;
            node = node.getParent();
        }
        return false;
    }

    // Returns true if the goal is reached.
    private SearchNode solveIteration(MinPQ<SearchNode> priorityQueue) {
        SearchNode currentNode = priorityQueue.delMin();
        int numberOfMoves = currentNode.getMoves();

        // stop when goal is reached
        if (currentNode.getBoard().isGoal())
            return currentNode;

        // add all neighbors to the priority queue
        for (Board board : currentNode.getBoard().neighbors()) {
            if (!isBoardInSolution(currentNode, board))
                priorityQueue.insert(new SearchNode(numberOfMoves + 1, board, currentNode));
        }

        return null;
    }

    private void solve(Board initialBoard) {
        final Comparator<SearchNode> PriorityComparator = (SearchNode p1, SearchNode p2) -> Integer.compare(p1.getPriority(), p2.getPriority());
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<>(PriorityComparator);
        MinPQ<SearchNode> priorityQueue = new MinPQ<>(PriorityComparator);

        // add initial board
        priorityQueue.insert(new SearchNode(0, initialBoard, null));
        twinPriorityQueue.insert(new SearchNode(0, initialBoard, null));

        SearchNode goalNode = null;
        while (!priorityQueue.isEmpty() && !twinPriorityQueue.isEmpty()) {
           goalNode = solveIteration(priorityQueue);
           if (goalNode != null) {
               break;
           }

           if (solveIteration(twinPriorityQueue) != null) {
               break;
           }
        }

        SearchNode node = goalNode;
        while (node != null) {
            solutionBoards.addFirst(node.getBoard());
            node = node.getParent();
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solutionBoards.size() != 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solutionBoards.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? solutionBoards : null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}