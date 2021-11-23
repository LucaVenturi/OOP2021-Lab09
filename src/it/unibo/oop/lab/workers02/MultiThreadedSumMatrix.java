package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    /**
     * @param nthread
     */
    public MultiThreadedSumMatrix(final int nthread) {
        super();
        this.nthread = nthread;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startRow;
        private final int nRows;
        private long res;

        Worker(final double[][] matrix, final int startRow, final int nRows) {
            super();
            this.matrix = matrix;
            this.startRow = startRow;
            this.nRows = nRows;
        }

        @Override
        public void run() {
            System.out.println("Working from row " + startRow + " to row " + (startRow + nRows - 1));
            for (int i = startRow; i < startRow + nRows && i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    this.res += matrix[i][j];
                }
            }
        }

        /**
         * @return
         *      Result of the sum of all the values in the matrix.
         */
        public long getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int sizeRow = matrix.length % nthread + matrix.length / nthread;
        final List<Worker> workers = new ArrayList<>(nthread);

        for (int startRow = 0; startRow < matrix.length; startRow += sizeRow) {
            workers.add(new Worker(matrix, startRow, sizeRow));
        }

        for (final Thread w:workers) {
            w.start();
        }

        long sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        return sum;
    }

}
