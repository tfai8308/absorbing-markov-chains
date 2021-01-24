import javax.swing.*;
import java.util.Arrays;

public class MarkovMaths {

    int[][] inputMatrix;
    int[] result;

    public MarkovMaths() {

    }

    public int[] getResult() {
        return result;
    }

    public void buildResult() {
        result = solution(inputMatrix);
    }

    public void buildInputMatrix(JTable table) {
        inputMatrix = new int[table.getRowCount()][table.getColumnCount()];
        for(int i = 0; i < table.getRowCount(); i++) {
            for(int k = 0; k < table.getColumnCount(); k++) {
                inputMatrix[i][k] = Integer.parseInt(table.getValueAt(i, k).toString());
            }
        }
    }

    private int[] solution(int[][] m) {

        //Getting information
        boolean[] terminalRows = new boolean[m.length];

        int[] rowTracker = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            rowTracker[i] = i;
        }

        //Getting row denominators
        int[] denominators = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            for (int k = 0; k < m[0].length; k++) {
                denominators[i] += m[i][k];
            }
        }

        //Any row with a >0 denominator is skipped
        for (int i = 0; i < m.length; i++) {
            if (denominators[i] != 0) {
                terminalRows[i] = false;
                continue;
            } else {
                terminalRows[i] = true;
                m[i][i] = 1;
            }
        }

        //Matrix Canonizing
        int destination = 0;
        for (int i = 0; i < terminalRows.length; i++) {
            if (terminalRows[i]) {
                int[] rTemp = m[i];
                m[i] = m[destination];
                m[destination] = rTemp;

                for (int k = 0; k < m[0].length; k++) {
                    int cTemp = m[k][destination];
                    m[k][destination] = m[k][i];
                    m[k][i] = cTemp;
                }

                boolean terminalTemp = terminalRows[i];
                terminalRows[i] = terminalRows[destination];
                terminalRows[destination] = terminalTemp;

                int rowTrackTemp = rowTracker[i];
                rowTracker[i] = rowTracker[destination];
                rowTracker[destination] = rowTrackTemp;

                destination++;
            }
        }

        //Recalculating denominators[]
        denominators = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            for (int k = 0; k < m[0].length; k++) {
                denominators[i] += m[i][k];
            }
        }

        //Finding common denominator
        int numTerminalRows = 1;
        for (int i = 0; i < denominators.length; i++) {
            if (denominators[i] != 1) {
                numTerminalRows = i;
                break;
            }
        }

        //Special case: 1 terminal row
        if (numTerminalRows == 1) {
            return new int[]{1, 1};
        }

        //Reshaping denominator array
        int[] denArr = new int[m.length - numTerminalRows];
        for (int i = 0; i < denArr.length; i++) {
            denArr[i] = denominators[i + numTerminalRows];
        }

        //Extracting A and B
        double[][] A = new double[m.length - numTerminalRows][numTerminalRows];
        double[][] B = new double[A.length][A.length];

        //Mapping values into A
        for (int i = numTerminalRows; i < m.length; i++) {
            for (int k = 0; k < numTerminalRows; k++) {
                A[i - numTerminalRows][k] = m[i][k];
            }
        }

        //Mapping values into B
        for (int i = numTerminalRows; i < m.length; i++) {
            for (int k = numTerminalRows; k < m.length; k++) {
                B[i - numTerminalRows][k - numTerminalRows] = m[i][k];
            }
        }
        //END Canonizing

        //Double conversion
        for (int i = 0; i < A.length; i++) {
            for (int k = 0; k < A[0].length; k++) {
                A[i][k] = A[i][k] / denArr[i];
            }
            for (int j = 0; j < B[0].length; j++) {
                B[i][j] = B[i][j] / denArr[i];
            }
        }

        //Solution formula: A = F^-1 * S

        //Creating F^-1 = I - B
        //B --> F^-1
        for (int i = 0; i < B.length; i++) {
            for (int k = 0; k < B[0].length; k++) {
                if (i == k) {
                    B[i][k] = 1 - B[i][k];
                } else {
                    B[i][k] = -B[i][k];
                }
            }
        }

        //Gaussian elimination F^-1

        for (int i = 0; i < B.length; i++) {
            if (B[i][i] == 1) {
                //Do nothing
            } else {
                double numerator = B[i][i];
                for (int k = i; k < B.length; k++) {
                    B[i][k] = B[i][k] / numerator;
                }

                for (int j = 0; j < A[i].length; j++) {
                    A[i][j] = A[i][j] / numerator;
                }
            }

            //Converting to row-echelon form
            for (int n = i + 1; n < B.length; n++) {
                double val = -B[n][i];
                for (int p = i; p < B[0].length; p++) {
                    B[n][p] = B[n][p] + (val * B[i][p] / B[i][i]);
                }
                for (int q = 0; q < A[0].length; q++) {
                    A[n][q] = A[n][q] + (val * A[i][q]) / B[i][i];
                }

            }

        }

        //Converting to identity matrix
        for (int i = B.length - 1; i >= 0; i--) {
            for (int n = i - 1; n >= 0; n--) {
                double val = -B[n][i];
                for (int p = i; p < B[0].length; p++) {
                    B[n][p] = B[n][p] + (val * B[i][p] / B[i][i]);
                }
                for (int q = 0; q < A[0].length; q++) {
                    A[n][q] = A[n][q] + (val * A[i][q]) / B[i][i];
                }

            }

        }
        //END Gaussian elimination

        //Finding s0 row
        int zeroRow = -1;
        for (int i = 0; i < rowTracker.length; i++) {
            if (rowTracker[i] == 0) {
                zeroRow = i;
                break;
            }
        }

        //Finding common denominators of solution
        double error = 0.0000000000001;
        int[][] ans = decimalToFract(A[zeroRow - numTerminalRows], error);

        int commonAnsDen = findLCM(Arrays.copyOf(ans[1], ans[1].length));

        //Scaling numerators
        for (int i = 0; i < ans[0].length; i++) {
            ans[0][i] = ans[0][i] * (commonAnsDen / ans[1][i]);
        }

        //Building final solution
        int[] solution = new int[ans[0].length + 1];
        for (int i = 0; i < ans[0].length; i++) {
            solution[i] = ans[0][i];
        }
        solution[solution.length - 1] = commonAnsDen;

        return solution;
    }

    private int findLCM(int[] arr) {
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        while (!sameValue(arr)) {
            int s = smallestValueIndex(arr);
            arr[s] += arrCopy[s];
        }
        return arr[0];
    }

    private int[][] decimalToFract(double[] row, double terror) {
        int[][] solution = new int[2][row.length];

        for (int i = 0; i < row.length; i++) {

            //Throwing out integer
            int temp = (int) Math.floor(row[i]);
            row[i] -= temp;

            //row[i] is practically 0 or 1
            if (row[i] < terror) {
                solution[0][i] = 0;
                solution[1][i] = 1;
                continue;
            } else if (row[i] > (1 - terror)) {
                solution[0][i] = 1;
                solution[1][i] = 1;
                continue;
            }

            //Stern-Brocot Walk
            int lowerN = 0;
            int lowerD = 1;
            int upperN = 1;
            int upperD = 1;
            int middleN;
            int middleD;
            while (true) {
                middleN = lowerN + upperN;
                middleD = lowerD + upperD;
                if (middleD * (row[i] + terror) < middleN) {
                    upperN = middleN;
                    upperD = middleD;
                } else if (middleD * (row[i] - terror) > middleN) {
                    lowerN = middleN;
                    lowerD = middleD;
                } else {
                    solution[0][i] = middleN;
                    solution[1][i] = middleD;
                    break;
                }
            }
        }
        return solution;
    }

    private boolean sameValue(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] != arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private int smallestValueIndex(int[] arr) {
        int index = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[index]) {
                index = i;
            }
        }
        return index;
    }


}
