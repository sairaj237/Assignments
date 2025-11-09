import java.util.ArrayList;
import java.util.Scanner;

public class Assignment2 {

    // Union of two fuzzy relations
    public static ArrayList<ArrayList<Double>> fuzzyUnion(ArrayList<ArrayList<Double>> R,
                                                          ArrayList<ArrayList<Double>> S) {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();

        for (int i = 0; i < R.size(); i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < R.get(0).size(); j++) {
                row.add(Math.max(R.get(i).get(j), S.get(i).get(j)));
            }
            result.add(row);
        }
        return result;
    }

    // Intersection of two fuzzy relations
    public static ArrayList<ArrayList<Double>> fuzzyIntersection(ArrayList<ArrayList<Double>> R,
                                                                 ArrayList<ArrayList<Double>> S) {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();

        for (int i = 0; i < R.size(); i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < R.get(0).size(); j++) {
                row.add(Math.min(R.get(i).get(j), S.get(i).get(j)));
            }
            result.add(row);
        }

        return result;
    }

    // Complement of a fuzzy relation
    public static ArrayList<ArrayList<Double>> fuzzyComplement(ArrayList<ArrayList<Double>> R) {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();

        for (int i = 0; i < R.size(); i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < R.get(0).size(); j++) {
                row.add(1.0 - R.get(i).get(j));
            }
            result.add(row);
        }

        return result;
    }

    // Composition (R ○ T)
    public static ArrayList<ArrayList<Double>> fuzzyComposition(ArrayList<ArrayList<Double>> R,
                                                                ArrayList<ArrayList<Double>> T) {
        int m = R.size();
        int n = R.get(0).size();
        int p = T.get(0).size();

        ArrayList<ArrayList<Double>> result = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            ArrayList<Double> row = new ArrayList<>();

            for (int k = 0; k < p; k++) {
                double maxVal = 0.0;
                for (int j = 0; j < n; j++) {
                    maxVal = Math.max(maxVal, Math.min(R.get(i).get(j), T.get(j).get(k)));
                }
                row.add(maxVal);
            }
            result.add(row);
        }

        return result;
    }

    // Print matrix
    public static void printRelation(ArrayList<ArrayList<Double>> R) {
        for (ArrayList<Double> row : R) {
            System.out.print("{ ");
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println("}");
        }
    }

    // Read matrix input from user
    public static ArrayList<ArrayList<Double>> readRelation(Scanner sc, int rows, int cols, String name) {
        ArrayList<ArrayList<Double>> R = new ArrayList<>();
        System.out.println("Enter relation " + name + " values (" + rows + "x" + cols + "):");

        for (int i = 0; i < rows; i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(sc.nextDouble());
            }
            R.add(row);
        }
        return R;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input size for R and S
        System.out.print("Enter rows and columns for relations R and S: ");
        int r = sc.nextInt();
        int c = sc.nextInt();

        ArrayList<ArrayList<Double>> R = readRelation(sc, r, c, "R");
        ArrayList<ArrayList<Double>> S = readRelation(sc, r, c, "S");

        System.out.println("\nRelation R:");
        printRelation(R);

        System.out.println("Relation S:");
        printRelation(S);

        var unionRel = fuzzyUnion(R, S);
        var interRel = fuzzyIntersection(R, S);
        var compR = fuzzyComplement(R);

        System.out.println("\nUnion (R ∪ S):");
        printRelation(unionRel);

        System.out.println("\nIntersection (R ∩ S):");
        printRelation(interRel);

        System.out.println("\nComplement (R'):");
        printRelation(compR);

        // Input size for T to perform composition
        System.out.print("\nEnter rows and columns for relation T: ");
        int tR = sc.nextInt();
        int tC = sc.nextInt();

        ArrayList<ArrayList<Double>> T = readRelation(sc, tR, tC, "T");

        System.out.println("\nRelation T:");
        printRelation(T);

        if (c != tR) {
            System.out.println("\nComposition not possible: Columns of R must equal rows of T");
        } else {
            var compRel = fuzzyComposition(R, T);
            System.out.println("\nComposition (R ○ T):");
            printRelation(compRel);
        }

        sc.close();
    }
}
