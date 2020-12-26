package org.demo;

import org.ejml.data.FixedMatrix3x3_64F;
import org.paukov.combinatorics3.Generator;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HillCipherTask {
    static long countPossibleKeys = 0L;
    static long countCandidateKeys = 0L;
    static final int KEY_LENGTH = 9;
    static final int MATRIX_SIZE = 3;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Program started ...");

        long duration = -System.currentTimeMillis();
        List<List<Integer>> keyList = HelperUtil.KeyGenerator.generateKeys(KEY_LENGTH);
        countPossibleKeys = keyList.size();

        keyList.parallelStream()
                .forEach((key) -> {
                    int mat[][] = buildMatrix(key, MATRIX_SIZE);
                    if (matrixHasInverse(mat, MATRIX_SIZE)) {
                        synchronized ("lock") {
                            countCandidateKeys++;
                        }
                    }
                });

        duration += System.currentTimeMillis();

        //wait other threads finish their work
        Thread.currentThread().join(2000);
        System.out.println("Elapsed time : %d millisecond ".formatted(duration));
        System.out.println("%d x %d Matrix:  \nPossible keys %d \nCandidate Keys %d".formatted(MATRIX_SIZE, MATRIX_SIZE, countPossibleKeys, countCandidateKeys));
        System.out.println("Program stopped");
    }


    static int[][] buildMatrix(List<Integer> letters, int n) {
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = letters.get(i * n + j);
            }
        }
        return matrix;
    }

    static boolean matrixHasInverse(int[][] mat, int size) {
        long det = HelperUtil.MatrixUtil.determinantOfMatrix(mat, size);
        //determinant is different than 0 then matrix is invertible
        return det != 0;
    }

}
