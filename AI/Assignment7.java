import java.util.*;

public class ForwardChaining {

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
            String line = sc.nextLine().trim();
            rules.add(parseRule(line));
        }

        // -----------------------------
        // Query
        // -----------------------------
        System.out.print("Enter query: ");
        String query = sc.nextLine().trim();

        // -----------------------------
        // Forward chaining
        // -----------------------------
        if (forwardChain(facts, rules, query))
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
            premises = p;
            conclusion = c;
        }
    }

    // ---------------------------------------------
    // Parse a rule string
    // Example: A & B -> C
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

    // ---------------------------------------------
    // Forward Chaining Algorithm
    // ---------------------------------------------
    static boolean forwardChain(Set<String> facts, List<Rule> rules, String query) {

        boolean addedNewFact = true;

        while (addedNewFact) {
            addedNewFact = false;

            for (Rule rule : rules) {

                // Check if all premises are in facts
                boolean allTrue = true;
                for (String p : rule.premises) {
                    if (!facts.contains(p)) {
                        allTrue = false;
                        break;
                    }
                }

                // If rule is satisfied, add conclusion
                if (allTrue && !facts.contains(rule.conclusion)) {
                    facts.add(rule.conclusion);
                    addedNewFact = true;

                    if (rule.conclusion.equals(query))
                        return true;
                }
            }
        }

        return facts.contains(query);
    }
}
