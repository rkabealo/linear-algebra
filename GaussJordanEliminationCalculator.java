import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * A program which reads in a valid matrix (checks for valid number of rows,
 * number of columns, and valid elements) and reduces it using Gauss-Jordan
 * elimination.
 *
 * @author Ruksana Kabealo
 *
 */
public final class GaussJordanEliminationCalculator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private GaussJordanEliminationCalculator() {
    }

    /**
     * A double used in comparison.
     */
    public static double EPSILON = 1e-15;

    /**
     * Prints a small welcome banner for the user.
     */
    public static void printWelcomeBanner() {
        System.out.println(
                "*************************************************************");
        System.out.println(
                "*                                                           *");
        System.out.println(
                "*   Welcome to the Gauss-Jordan Elimination Calculator!     *");
        System.out.println(
                "*                                                           *");
        System.out.println(
                "*************************************************************");
        System.out.println();
    }

    /**
     * A method to format the output to avoid -0.0 errors.
     *
     * @param number
     *            the number being formatted
     * @return formattedValue the formatted version of number
     */
    public static String format(double number) {
        DecimalFormat df = new DecimalFormat("#,##0.0000000000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String formattedValue = df.format(number);
        formattedValue = formattedValue.replaceAll("^-(?=0(.0*)?$)", "");
        return formattedValue;
    }

    /**
     * Checks whether an entered number is a positive integer. If not,
     * repeatedly prompts user until it is. If it is, quits method.
     *
     * @param prompt
     *            the interpretation of the number the user is entering
     * @param input
     *            used to read in from the user
     * @return number the positive integer
     */
    public static int getPositiveInteger(String prompt, Scanner input) {
        int number = 0;

        while (true) {
            System.out.print("Enter " + prompt);
            try {
                number = Integer.parseInt(input.next());

                if (number >= 0) {
                    break; // will only get to here if input was an integer
                } else {
                    System.out.println("Invalid input for rows. "
                            + "Must be POSITIVE integer. Try again.");
                }
            } catch (NumberFormatException ignore) {
                System.out.println("Invalid input for rows. "
                        + "Must be positive INTEGER. Try again.");
            }
        }

        return number;
    }

    /**
     * Reduces a single row by creating a single one (if possible). If a leading
     * one is created, modifies matrix to eliminate all other elements in the
     * column of the leading one to zeros.
     *
     * @param i
     *            the initial row of the matrix to start reducing from
     * @param j
     *            the initial column of the matrix to start reducing from
     * @param numColumns
     *            the number of columns in matrix
     * @param numRows
     *            the number of rows in matrix
     * @param matrix
     *            the matrix being modified
     *
     */
    public static void reduceRowAndColumn(int i, int j, int numColumns,
            int numRows, double[] matrix) {
        boolean reduced = false;
        // Reassign parameters for use within the method
        int row = i;
        int leadingRow = i;
        int column = j;
        int leadingColumn = j;

        /*
         * Get leading element
         */
        while (column < numColumns && !reduced) {
            double leadingNumber = matrix[row * numColumns + column];
            /*
             * If the leading number in the column is NOT a 0, we check if it's
             * a 1.
             */
            if (Math.abs(leadingNumber - 0) > EPSILON) {

                if (Math.abs(leadingNumber - 1) > EPSILON) {
                    /*
                     * The leading number is not a 1. Store its position and
                     * loop through and divide all elements of the row by this
                     * number.
                     */
                    double dividend = leadingNumber;
                    leadingRow = row;
                    leadingColumn = column;

                    for (int postLeading = 0; postLeading < numColumns; postLeading++) {
                        matrix[row * numColumns + postLeading] /= dividend;
                    }
                    reduced = true;
                } else {
                    // There's already a leading 1. Do nothing.
                    reduced = true;
                    leadingRow = row;
                    leadingColumn = column;
                }

            } else {
                // The element is a 0
                column++;
            }
        }

        /*
         * If a leading 1 is created, turn all elements above and below the
         * leading 1 into 0s
         */
        if (reduced) {
            /*
             * Subtract off the correct multiple of the row with the leading 1
             * from all rows ABOVE it
             */
            row = 0;
            while (row < leadingRow) {
                double next = matrix[row * numColumns + leadingColumn];
                /*
                 * If the leading number in the column is NOT a 0, we get it to
                 * be a 0
                 */
                if (Math.abs(next - 0) > EPSILON) {

                    double multiple = matrix[row * numColumns + leadingColumn];

                    for (int c = 0; c < numColumns; c++) {
                        matrix[row * numColumns
                                + c] -= matrix[leadingRow * numColumns + c]
                                        * multiple;
                    }
                }
                row++;
            }

            /*
             * Subtract off the correct multiple of the row with the leading 1
             * from all rows below it.
             */
            row = leadingRow + 1;
            while (row < numRows) {
                double nextDown = matrix[row * numColumns + leadingColumn];
                /*
                 * If the leading number in the column is NOT a 0, we get it to
                 * be a 0
                 */
                if (Math.abs(nextDown - 0) > EPSILON) {

                    double multiple = matrix[row * numColumns + leadingColumn];

                    for (int c = 0; c < numColumns; c++) {
                        matrix[row * numColumns
                                + c] -= matrix[leadingRow * numColumns + c]
                                        * multiple;
                    }
                }
                row++;
            }
        }

    }

    /**
     * Prints a matrix.
     *
     *
     * @param numRows
     *            the number of rows in the matrix
     * @param numColumns
     *            the number of columns in the matrix
     * @param matrix
     *            the matrix to be printed
     *
     */
    public static void printMatrix(int numRows, int numColumns,
            double[] matrix) {

        System.out.println();

        if (numRows == 0 || numColumns == 0) {
            System.out.println("The matrix is empty!");
        } else if (numRows == 1 && numColumns == 1) {
            double temp = matrix[0 * numColumns + 0];
            String formattedTemp = format(temp);
            System.out.format("|%-25s|", formattedTemp);
            System.out.println();
        } else if (numColumns == 1) {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    double temp = matrix[i * numColumns + j];
                    String formattedTemp = format(temp);
                    System.out.format("|%-25s|", formattedTemp);
                }
                System.out.println();
            }
        } else {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    double temp = matrix[i * numColumns + j];
                    String formattedTemp = format(temp);
                    if (j == 0) {
                        System.out.format("|%-25s", formattedTemp + " ");
                    } else if (j == numColumns - 1) {
                        System.out.format("%-25s|", formattedTemp + " ");
                    } else {
                        System.out.format("%-25s", formattedTemp + " ");
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Print a welcome banner for the user
        printWelcomeBanner();

        /*
         * Read in the dimensions of the matrix from the user.
         */
        int numRows = getPositiveInteger("# of rows in the matrix: ", input);
        int numColumns = getPositiveInteger("# of columns in the matrix: ",
                input);

        /*
         * Initialize the matrix. It's a single dimensional array to allow for
         * faster element access (a two-dimensional array would be more readable
         * by implementers, but much slower).
         *
         */
        double[] matrix = new double[numRows * numColumns];

        /*
         * Allow the user to populate the matrix with entries. Uses a while loop
         * with condition numColumns != 0 to ensure program doesn't try to read
         * elements for 1x0 matrix (0x1 and 0x0 are taken care of by the inner
         * for-loop).
         */
        int i = 0;
        while (i < numRows && numColumns != 0) {

            double temp = 0.0;

            System.out.print(
                    "Enter " + numColumns + " elements for row " + i + ": ");

            for (int j = 0; j < numColumns; j++) {

                // Check each element entered is valid
                while (true) {
                    try {
                        temp = Double.parseDouble(input.next());
                        break; // will only get to here if input was an double
                    } catch (NumberFormatException ignore) {
                        System.out.println(
                                "Invalid input for rows. Must be a double. Try again.");
                    }
                }

                // Add the individual element to the row
                matrix[i * numColumns + j] = temp;
            }
            i++;
        }

        // Printing the original matrix
        System.out.println("The original matrix: ");
        printMatrix(numRows, numColumns, matrix);

        /*
         * Row reduces the matrix by taking care of one row and one column at a
         * time.
         */
        for (int r = 0; r < numRows; r++) {
            reduceRowAndColumn(r, 0, numColumns, numRows, matrix);
        }

        System.out.println("The row reduced matrix: ");
        printMatrix(numRows, numColumns, matrix);

        // Close input scanner
        input.close();
    }

}
