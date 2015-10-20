/**
 * Created by Bazna on 10/20/2015.
 */
/*
Corner cases.
You may assume that the constructor receives an N-by-N array containing the N2 integers between 0 and N2 ? 1,
where 0 represents the blank square.

Performance requirements.
Your implementation should support all Board methods in time proportional to N2 (or better) in the worst case.
*/
public class Board {
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {

    }
    // board dimension N
    public int dimension() {

    }

    // number of blocks out of place
    public int hamming()
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {

    }
    // is this board the goal board?
    public boolean isGoal() {

    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

    }
    // does this board equal y?
    public boolean equals(Object y) {

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

    }

    // string representation of this board (in the output format specified below)
    public String toString() {

    }

    // unit tests (not graded)
    public static void main(String[] args)  {

    }
}