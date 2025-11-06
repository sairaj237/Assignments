import java.util.ArrayList;
import java.util.Arrays;

public class Assignment2 {

    // Union of two fuzzy relations
    public static ArrayList<ArrayList<Double>> fuzzyUnion(ArrayList<ArrayList<Double>> R,
                                                          ArrayList<ArrayList<Double>> S) {
        ArrayList<ArrayList<Double>> result =
                new ArrayList<>();

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
        ArrayList<ArrayList<Double>> result =
                new ArrayList<>();

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
        ArrayList<ArrayList<Double>> result =
                new ArrayList<>();

        for (int i = 0; i < R.size(); i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < R.get(0).size(); j++) {
                row.add(1.0 - R.get(i).get(j));
            }
            result.add(row);
        }

        return result;
    }

    // Composition (R ○ S)
    public static ArrayList<ArrayList<Double>> fuzzyComposition(ArrayList<ArrayList<Double>> R,
                                                                ArrayList<ArrayList<Double>> S) {
        int m = R.size();           // rows of R
        int n = R.get(0).size();    // cols of R = rows of S
        int p = S.get(0).size();    // cols of S

        ArrayList<ArrayList<Double>> result =
                new ArrayList<>();

        for (int i = 0; i < m; i++) {
            ArrayList<Double> row = new ArrayList<>();

            for (int k = 0; k < p; k++) {
                double maxVal = 0.0;
                for (int j = 0; j < n; j++) {
                    maxVal = Math.max(maxVal, Math.min(R.get(i).get(j), S.get(j).get(k)));
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

    public static void main(String[] args) {

        // Relation R (2x3)
        ArrayList<ArrayList<Double>> R = new ArrayList<>();
        R.add(new ArrayList<>(Arrays.asList(0.2, 0.7, 1.0)));
        R.add(new ArrayList<>(Arrays.asList(0.5, 0.3, 0.9)));

        // Relation S (2x3)
        ArrayList<ArrayList<Double>> S = new ArrayList<>();
        S.add(new ArrayList<>(Arrays.asList(0.6, 0.4, 0.8)));
        S.add(new ArrayList<>(Arrays.asList(0.1, 0.9, 0.5)));

        System.out.println("Relation R:");
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

        // Relation T (3x2) for composition
        ArrayList<ArrayList<Double>> T = new ArrayList<>();
        T.add(new ArrayList<>(Arrays.asList(0.3, 0.6)));
        T.add(new ArrayList<>(Arrays.asList(0.8, 0.2)));
        T.add(new ArrayList<>(Arrays.asList(0.4, 0.9)));

        System.out.println("\nRelation T:");
        printRelation(T);

        var compRel = fuzzyComposition(R, T);

        System.out.println("\nComposition (R ○ T):");
        printRelation(compRel);
    }
}
