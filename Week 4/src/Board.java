import java.util.*;

/**
 * Created by Bazna on 10/20/2015.
 */
public class Board {
    private static final int EMPTY_TILE_VALUE = 0;
    private int[][] tiles;
    private Board[] neighbors;
    private int emptyTileRow;
    private int emptyTileColumn;

    // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.tiles = new int[blocks.length][blocks.length];
        for (int iRow = 0; iRow < blocks.length; iRow++) {
            for (int iCol = 0; iCol < blocks.length; iCol++) {
                this.tiles[iRow][iCol] = blocks[iRow][iCol];
                if (blocks[iRow][iCol] == EMPTY_TILE_VALUE) {
                    emptyTileRow = iRow;
                    emptyTileColumn = iCol;
                }
            }
        }
    }

    // board dimension N
    public int dimension() {
        return tiles.length;
    }

    private void swap(int sourceRow, int sourceColumn, int destinationRow, int destinationColumn) {
        int temporaryCell = tiles[sourceRow][sourceColumn];
        tiles[sourceRow][sourceColumn] = tiles[destinationRow][destinationColumn];
        tiles[destinationRow][destinationColumn] = temporaryCell;
    }

    // swaps two cells
    private void swapWithEmptyTile(int sourceRow, int sourceColumn) {
        swap(sourceRow, sourceColumn, emptyTileRow, emptyTileColumn);
        emptyTileRow = sourceRow;
        emptyTileColumn = sourceColumn;
    }

    private int expectedValue(int iRow, int iCol) {
        return iRow * dimension() + iCol + 1;
    }

    private int getGoalRow(int cellValue) {
        return (cellValue - 1) / dimension();
    }

    private int getGoalCol(int cellValue) {
        return (cellValue - 1) % dimension();
    }

    // number of tiles out of place
    public int hamming() {
        int hammingValue = 0;
        for (int iRow = 0; iRow < dimension(); iRow++) {
            for (int iCol = 0; iCol < dimension(); iCol++) {
                if (tiles[iRow][iCol] != EMPTY_TILE_VALUE && tiles[iRow][iCol] != expectedValue(iRow, iCol))
                    ++hammingValue;
            }
        }

        return hammingValue;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanValue = 0;
        for (int iRow = 0; iRow < dimension(); iRow++) {
            for (int iCol = 0; iCol < dimension(); iCol++) {
                int tileValue = tiles[iRow][iCol];
                if (tileValue != EMPTY_TILE_VALUE && tileValue != expectedValue(iRow, iCol))
                    manhattanValue += Math.abs(iRow - getGoalRow(tileValue)) + Math.abs(iCol - getGoalCol(tileValue));
            }
        }

        return manhattanValue;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        final int srcRow = 0, destRow = dimension() - 1;
        int srcColumn = tiles[srcRow][0] != EMPTY_TILE_VALUE ? 0 : 1;
        int destColumn = tiles[destRow][dimension() - 1] != EMPTY_TILE_VALUE ? dimension() - 1 : dimension() - 2;

        Board twinBoard = new Board(tiles);

        // swap tiles
        twinBoard.swap(srcRow, srcColumn, destRow, destColumn);

        return twinBoard;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;

        if (y == null)
            return false;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board)y;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // update all neighbors
    private void updateNeighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        // consider the move-from-left neighbor
        if (emptyTileColumn != 0) {
            Board board = new Board(tiles);
            board.swapWithEmptyTile(emptyTileRow, emptyTileColumn - 1);
            neighbors.add(board);
        }

        // consider the move-from-right neighbor
        if (emptyTileColumn != dimension() - 1) {
            Board board = new Board(tiles);
            board.swapWithEmptyTile(emptyTileRow, emptyTileColumn + 1);
            neighbors.add(board);
        }

        // consider the move-from-top neighbor
        if (emptyTileRow != 0) {
            Board board = new Board(tiles);
            board.swapWithEmptyTile(emptyTileRow - 1, emptyTileColumn);
            neighbors.add(board);
        }

        // consider the move-from-bottom neighbor
        if (emptyTileRow != dimension() - 1) {
            Board board = new Board(tiles);
            board.swapWithEmptyTile(emptyTileRow + 1, emptyTileColumn);
            neighbors.add(board);
        }

        this.neighbors = neighbors.toArray(new Board[neighbors.size()]);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return  new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                if (neighbors == null) {
                    updateNeighbors();
                }
                return new NeighborIterator();
            }
        };
    }

    private class NeighborIterator implements Iterator<Board> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < neighbors.length;
        }

        @Override
        public Board next() {
            if (hasNext()) {
                return neighbors[index++];
            } else {
                throw new NoSuchElementException("There is no next neighbor.");
            }
        }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int iRow = 0; iRow < dimension(); iRow++) {
            for (int iColumn = 0; iColumn < dimension(); iColumn++) {
                s.append(String.format("%2d ", tiles[iRow][iColumn]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args)  {

    }
}