package com.hellkni9ht.percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.function.Function;

/**
 * Created by Bazna on 9/20/2015.
 */
public class PercolationStats {

    private double threshold[];
    private int numberOfExperiments;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        // check inputs
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();

        // initialize
        numberOfExperiments = T;
        threshold = new double[numberOfExperiments];

        // do experiments numberOfExperiments times
        doExperiments(N);
    }

    // performs numberOfExperiments independent computational experiments on an N-by-N grid
    private void doExperiments(int N) {
        for (int iExperiment = 0; iExperiment < numberOfExperiments; iExperiment++) {
            threshold[iExperiment] = calculateThreshold(N);
        }
    }


    private double calculateThreshold(int N) {
        Percolation percolation = new Percolation(N);
        int openSites = 0;
        while (!percolation.percolates()) {
            int iRow = StdRandom.uniform(1, N + 1);
            int iCol = StdRandom.uniform(1, N + 1);

            if (!percolation.isOpen(iRow, iCol)) {
                percolation.open(iRow, iCol);
                openSites++;
            }
        }

        return openSites / (double)(N * N);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(numberOfExperiments);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(numberOfExperiments);
    }

    // test client (described below)
    public static void main(String[] args) {
        int N = -1, T = -1;

        // extract two command-line arguments N and T
        if (args.length >= 2) {

            Function<String, Integer> parseArgument = (String argument) -> {
                try {
                    return Integer.parseInt(argument);
                } catch (NumberFormatException e) {
                    System.err.println("Argument" + argument + " must be an integer.");
                    System.exit(1);
                    // java interpreter is happy
                    return -1;
                }
            };

            N = parseArgument.apply(args[0]);
            T = parseArgument.apply(args[1]);
        }

        PercolationStats stats = new PercolationStats(N, T);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
