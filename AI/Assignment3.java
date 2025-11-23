import java.util.*;

public class FamilyTreeKB {

    static Map<String, String> fatherOf = new HashMap<>();
    static Map<String, String> motherOf = new HashMap<>();
    static Map<String, List<String>> childrenOf = new HashMap<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of family facts: ");
        int n = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter facts (examples: father(John, Mary), mother(Sara, Tom)):");
        for (int i = 0; i < n; i++) {
            String fact = sc.nextLine().trim();
            parseFact(fact);
        }

        System.out.println("\nKnowledge-base loaded.");
        System.out.println("Ask questions (examples: father(Mary, ?), siblings(John, ?))");
        System.out.println("Type 'exit' to stop.\n");

        while (true) {
            System.out.print("Query: ");
            String q = sc.nextLine().trim();

            if (q.equalsIgnoreCase("exit"))
                break;

            answerQuery(q);
        }
    }

    // ----------------------------- PARSE FACTS -----------------------------
    static void parseFact(String fact) {
        fact = fact.replace(" ", "");

        if (fact.startsWith("father(")) {
            String info = fact.substring(7, fact.length() - 1);
            String[] p = info.split(",");
            String father = p[0];
            String child = p[1];
            fatherOf.put(child, father);
            addChild(father, child);
        }

        else if (fact.startsWith("mother(")) {
            String info = fact.substring(7, fact.length() - 1);
            String[] p = info.split(",");
            String mother = p[0];
            String child = p[1];
            motherOf.put(child, mother);
            addChild(mother, child);
        }
    }

    static void addChild(String parent, String child) {
        childrenOf.putIfAbsent(parent, new ArrayList<>());
        childrenOf.get(parent).add(child);
    }

    // ----------------------------- PARSE QUERIES -----------------------------
    static void answerQuery(String q) {
        q = q.replace(" ", "");

        if (q.startsWith("father(")) {
            String inside = q.substring(7, q.length() - 1);
            String child = inside.split(",")[0];

            String ans = fatherOf.get(child);
            if (ans != null)
                System.out.println("Father of " + child + " is " + ans);
            else
                System.out.println("Unknown");
        }

        else if (q.startsWith("mother(")) {
            String inside = q.substring(7, q.length() - 1);
            String child = inside.split(",")[0];

            String ans = motherOf.get(child);
            if (ans != null)
                System.out.println("Mother of " + child + " is " + ans);
            else
                System.out.println("Unknown");
        }

        else if (q.startsWith("children(")) {
            String inside = q.substring(9, q.length() - 1);
            String parent = inside.split(",")[0];

            List<String> kids = childrenOf.getOrDefault(parent, new ArrayList<>());
            if (!kids.isEmpty())
                System.out.println("Children of " + parent + ": " + kids);
            else
                System.out.println("Unknown or none");
        }

        else if (q.startsWith("siblings(")) {
            String inside = q.substring(9, q.length() - 1);
            String person = inside.split(",")[0];

            List<String> sibs = getSiblings(person);
            if (!sibs.isEmpty())
                System.out.println("Siblings of " + person + ": " + sibs);
            else
                System.out.println("No siblings found");
        }

        else {
            System.out.println("Unknown query.");
        }
    }

    // ----------------------------- RULE: SIBLINGS -----------------------------
    static List<String> getSiblings(String person) {
        List<String> result = new ArrayList<>();

        String f = fatherOf.get(person);
        String m = motherOf.get(person);

        for (String child : fatherOf.keySet()) {
            if (!child.equals(person) && fatherOf.get(child) != null && fatherOf.get(child).equals(f))
                result.add(child);
        }

        for (String child : motherOf.keySet()) {
            if (!child.equals(person) && motherOf.get(child) != null && motherOf.get(child).equals(m))
                if (!result.contains(child))
                    result.add(child);
        }

        return result;
    }
}
