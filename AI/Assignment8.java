import java.util.*;

public class BackwardChaining {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // -----------------------------
        // Input facts
        // -----------------------------
        System.out.print("Enter number of facts: ");
        int f = sc.nextInt();
        sc.nextLine();

        Set<String> facts = new HashSet<>();

        System.out.println("Enter facts:");
        for (int i = 0; i < f; i++) {
            facts.add(sc.nextLine().trim());
        }

        // -----------------------------
        // Input rules
        // -----------------------------
        System.out.print("Enter number of rules: ");
        int r = sc.nextInt();
        sc.nextLine();

        List<Rule> rules = new ArrayList<>();

        System.out.println("Enter rules in the form: A & B -> C");
        for (int i = 0; i < r; i++) {
            rules.add(parseRule(sc.nextLine().trim()));
        }

        // -----------------------------
        // Query
        // -----------------------------
        System.out.print("Enter query: ");
        String query = sc.nextLine().trim();

        // -----------------------------
        // Backward chaining
        // -----------------------------
        if (backwardChain(query, facts, rules, new HashSet<>()))
            System.out.println("Query '" + query + "' is TRUE.");
        else
            System.out.println("Query '" + query + "' cannot be proven.");
    }

    // ---------------------------------------------
    // Rule structure
    // ---------------------------------------------
    static class Rule {
        List<String> premises;
        String conclusion;

        Rule(List<String> p, String c) {
            this.premises = p;
            this.conclusion = c;
        }
    }

    // ---------------------------------------------
    // Parse rule
    // ---------------------------------------------
    static Rule parseRule(String s) {
        String[] parts = s.split("->");

        String left = parts[0].trim();
        String right = parts[1].trim();

        String[] conds = left.split("&");
        List<String> premises = new ArrayList<>();

        for (String c : conds)
            premises.add(c.trim());

        return new Rule(premises, right);
    }

    // ------------------------------------------------
    // Backward Chaining Algorithm
    // ------------------------------------------------
    static boolean backwardChain(String goal,
                                 Set<String> facts,
                                 List<Rule> rules,
                                 Set<String> visited) {

        // Already proven
        if (facts.contains(goal))
            return true;

        // Avoid infinite loops
        if (visited.contains(goal))
            return false;

        visited.add(goal);

        // Try all rules with conclusion = goal
        for (Rule rule : rules) {

            if (rule.conclusion.equals(goal)) {
                boolean allTrue = true;

                for (String p : rule.premises) {
                    if (!backwardChain(p, facts, rules, visited)) {
                        allTrue = false;
                        break;
                    }
                }

                if (allTrue) {
                    facts.add(goal);  // store derived fact
                    return true;
                }
            }
        }

        return false;
    }
}
