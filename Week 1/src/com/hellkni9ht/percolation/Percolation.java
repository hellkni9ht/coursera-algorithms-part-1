package com.hellkni9ht.percolation;

import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Created by Bazna on 9/20/2015.
 */
public class Percolation {

    private enum SiteState  {
        BLOCKED((byte)0), OPEN((byte)1), FULL((byte)2);
        SiteState(byte value) {
            this.value = value;
        }

        byte value;
    };

    private SiteState[] site;
    private int gridSize = 0;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufPercolate;
    private int topSiteId;
    private int bottomSiteId;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException();

        gridSize = N;
        site = new SiteState[gridSize * gridSize];
        topSiteId = N * N;
        bottomSiteId = N * N + 1;
        uf = new WeightedQuickUnionUF(N * N + 1);
        ufPercolate = new WeightedQuickUnionUF(N * N + 2);
    }

    private boolean isOutOfBounds(int i, int j) {
        return i > gridSize || j > gridSize || i < 1 || j < 1;
    }

    private boolean isSiteOpen(int i, int j) {
        return SiteState.OPEN == site[siteId(i, j)];
    }

    private int siteId(int i, int j) {
        return ((i - 1) * gridSize + (j - 1));
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (isOutOfBounds(i, j))
            throw new IndexOutOfBoundsException();

        int currentSiteId = siteId(i, j);
        if (i == 1) {
            uf.union(currentSiteId, topSiteId);
            ufPercolate.union(currentSiteId, topSiteId);
        }
        if (i == gridSize)
            ufPercolate.union(currentSiteId, bottomSiteId);

        site[currentSiteId] = SiteState.OPEN;

        // bind to the top site
        if (i > 1 && isSiteOpen(i - 1, j)) {
            uf.union(currentSiteId, siteId(i - 1, j));
            ufPercolate.union(currentSiteId, siteId(i - 1, j));
        }

        // bind to the bottom site
        if (i < gridSize && isSiteOpen(i + 1, j)) {
            uf.union(currentSiteId, siteId(i + 1, j));
            ufPercolate.union(currentSiteId, siteId(i + 1, j));
        }

        // bind to the left site
        if (j > 1 && isSiteOpen(i, j - 1)) {
            uf.union(currentSiteId, siteId(i, j - 1));
            ufPercolate.union(currentSiteId, siteId(i, j - 1));
        }

        // bind to the right site
        if (j < gridSize && isSiteOpen(i, j + 1)) {
            uf.union(currentSiteId, siteId(i, j + 1));
            ufPercolate.union(currentSiteId, siteId(i, j + 1));
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        if (isOutOfBounds(i, j))
            throw new IndexOutOfBoundsException();
        return SiteState.OPEN == site[siteId(i, j)];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        if (isOutOfBounds(i, j))
            throw new IndexOutOfBoundsException();

        if (!isSiteOpen(i, j))
            return false;

        return uf.connected(topSiteId, siteId(i, j));
    }

    // does the system percolate?
    public boolean percolates() {
        return ufPercolate.connected(topSiteId, bottomSiteId);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
