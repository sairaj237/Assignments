import java.util.*;

public class CSP {

    static List<String> variables = new ArrayList<>();
    static List<String> colors = new ArrayList<>();
    static Map<String, List<String>> neighbors = new HashMap<>();
    static Map<String, String> assignment = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ----- Variables -----
        System.out.print("Enter number of variables: ");
        int n = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter variable names:");
        for (int i = 0; i < n; i++) {
            String var = sc.nextLine().trim();
            variables.add(var);
            neighbors.put(var, new ArrayList<>());
        }

        // ----- Colors -----
        System.out.print("Enter number of colors: ");
        int c = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter color names:");
        for (int i = 0; i < c; i++) {
            colors.add(sc.nextLine().trim());
        }

        // ----- Constraints -----
        System.out.print("Enter number of constraints: ");
        int cons = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter constraints (format: A B means A ≠ B):");
        for (int i = 0; i < cons; i++) {
            String a = sc.next();
            String b = sc.next();
            neighbors.get(a).add(b);
            neighbors.get(b).add(a);
        }

        System.out.println("\nSolving CSP...");
        if (backtrack()) {
            System.out.println("Solution found:");
            for (String var : variables)
                System.out.println(var + " -> " + assignment.get(var));
        } else {
            System.out.println("No solution.");
        }
    }

    // ------------------ BACKTRACKING ------------------
    static boolean backtrack() {
        if (assignment.size() == variables.size())
            return true;

        // choose unassigned variable
        String var = null;
        for (String v : variables)
            if (!assignment.containsKey(v)) {
                var = v;
                break;
            }

        for (String color : colors) {
            if (isValid(var, color)) {
                assignment.put(var, color);

                if (backtrack())
                    return true;

                assignment.remove(var);
            }
        }
        return false;
    }

    // ------------------ VALIDATION ------------------
    static boolean isValid(String var, String color) {
        for (String nb : neighbors.get(var)) {
            if (assignment.containsKey(nb) && assignment.get(nb).equals(color))
                return false;
        }
        return true;
    }
}
