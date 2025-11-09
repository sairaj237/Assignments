import java.util.ArrayList;
import java.util.Scanner;

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

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of elements in fuzzy sets A and B: ");
        int n = sc.nextInt();

        ArrayList<Double> A = new ArrayList<>();
        ArrayList<Double> B = new ArrayList<>();

        // Input for A
        System.out.println("Enter values of Set A:");
        for (int i = 0; i < n; i++) {
            A.add(sc.nextDouble());
        }

        // Input for B
        System.out.println("Enter values of Set B:");
        for (int i = 0; i < n; i++) {
            B.add(sc.nextDouble());
        }

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

        sc.close();
    }
}
