import java.util.ArrayList;
import java.util.Arrays;

public class Assignment1 {

    // Union: max(A[i], B[i])
    public static ArrayList<Double> fuzzyUnion(ArrayList<Double> A, ArrayList<Double> B) {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            result.add(Math.max(A.get(i), B.get(i)));
        }
        return result;
    }

    // Intersection: min(A[i], B[i])
    public static ArrayList<Double> fuzzyIntersection(ArrayList<Double> A, ArrayList<Double> B) {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            result.add(Math.min(A.get(i), B.get(i)));
        }
        return result;
    }

    // Complement: 1 - A[i]
    public static ArrayList<Double> fuzzyComplement(ArrayList<Double> A) {
        ArrayList<Double> result = new ArrayList<>();
        for (double val : A) {
            result.add(1.0 - val);
        }
        return result;
    }

    public static void printSet(ArrayList<Double> set) {
        System.out.print("{ ");
        for (double val : set) {
            System.out.print(val + " ");
        }
        System.out.println("}");
    }

    public static void main(String[] args) {

        ArrayList<Double> A = new ArrayList<>(Arrays.asList(0.2, 0.7, 0.5, 1.0));
        ArrayList<Double> B = new ArrayList<>(Arrays.asList(0.6, 0.3, 0.8, 0.4));

        System.out.print("Set A: ");
        printSet(A);

        System.out.print("Set B: ");
        printSet(B);

        ArrayList<Double> unionSet = fuzzyUnion(A, B);
        ArrayList<Double> intersectionSet = fuzzyIntersection(A, B);
        ArrayList<Double> complementA = fuzzyComplement(A);

        System.out.print("Union (A ∪ B): ");
        printSet(unionSet);

        System.out.print("Intersection (A ∩ B): ");
        printSet(intersectionSet);

        System.out.print("Complement (A'): ");
        printSet(complementA);
    }
}
