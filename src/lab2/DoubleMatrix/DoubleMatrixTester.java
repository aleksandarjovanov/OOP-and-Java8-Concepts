package lab2.DoubleMatrix;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InsufficientElementsException extends Exception {
    /*
    public InsufficientElementsException () {};
    public String getMessage() { return String.format("Insufficient number of elements"); }
     */
    @Override
    public String getMessage() {
        return "Insufficient number of elements";
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException() {
    }

    ;

    public String getMessage() {
        return String.format("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException() {
    }

    ;

    public String getMessage() {
        return String.format("Invalid column number");
    }
}

final class DoubleMatrix{
    private final double [][] matrix;
    private final int m;
    private final int n;

    public DoubleMatrix(double a[], int m, int n) throws InsufficientElementsException {
        if(a.length < (m * n)) throw new InsufficientElementsException();

        this.m = m;
        this.n = n;

        matrix = new double[m][n];
        for(int i = 0; i < m; i++){
            System.arraycopy(a, (a.length - (m * n)) + (i * n), matrix[i], 0, n);
        }
    }

    public String getDimensions(){
        return String.format("[%d x %d]", this.m, this.n);
    }

    public int rows() {
        return this.m;
    }

    public int columns() {
        return this.n;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        /* Assuming the row is given starting from 1 and not 0 */
        row -= 1;
        if (row < 0 || row >= rows()) {
            throw new InvalidRowNumberException();
        }

        return Arrays.stream(matrix[row])
                .max()
                .getAsDouble();
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        /* Assuming the row is given starting from 1 and not 0 */
        column -= 1;
        if (column < 0 || column >= columns()) {
            throw new InvalidColumnNumberException();
        }

        int finalColumn = column;
        return IntStream.range(0, m)
                .mapToDouble(i -> matrix[i][finalColumn])
                .max().getAsDouble();

    }

    public double sum(){
        return IntStream.range(0, m)
                .mapToDouble(i -> Arrays.stream(matrix[i])
                        .sum())
                .sum();
    }

    public double[] toSortedArray(){
        return Arrays.stream(matrix)
                .flatMapToDouble(Arrays::stream)
                .boxed()                                         // sort method for primitive type streams like DoubleStream dose not have impl with Comparator interface
                .sorted(Collections.reverseOrder())
                .mapToDouble(Double::doubleValue)       // UNBOXING so they can fit in a double[]
                .toArray();
    }

    @Override
    public String toString() {

        return Arrays.stream(matrix)
                .map(this::formString)
                .collect(Collectors.joining("\n"));
    }



    public String formString(double[] d){
        return Arrays.stream(d)
                 .mapToObj(elem -> String.format("%.2f", elem))
                 .collect(Collectors.joining("\t"));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleMatrix)) return false;

        DoubleMatrix that = (DoubleMatrix) o;

        if (m != that.m) return false;
        if (n != that.n) return false;
        return Arrays.deepEquals(matrix, that.matrix);      // default intellij equals metod, not java 7+
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(matrix);
        result = 31 * result + m;
        result = 31 * result + n;
        return result;
    }

}

class MatrixReader{

    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner scanner = new Scanner(input);

        int m = scanner.nextInt();
        int n = scanner.nextInt();
        int len = m * n;
        double [] array = new double[len];
        IntStream.range(0, len).forEach(i -> array[i] = scanner.nextDouble());
        return new DoubleMatrix(array, m, n);
    }
}

public class DoubleMatrixTester {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}












