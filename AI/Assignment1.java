import java.util.*;

public class EightPuzzleMatrix {

    static class Node {
        int[][] state;
        Node parent;

        Node(int[][] s, Node p) {
            state = s;
            parent = p;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ---- Input Start State ----
        System.out.println("Enter start matrix (use 0 for blank):");
        int[][] start = readMatrix(sc);

        // ---- Input Goal State ----
        System.out.println("Enter goal matrix (use 0 for blank):");
        int[][] goal = readMatrix(sc);

        System.out.println("\nRunning BFS...");
        solveBFS(start, goal);

        System.out.println("\nRunning DFS...");
        solveDFS(start, goal);
    }

    // ====================== BFS ======================
    static void solveBFS(int[][] start, int[][] goal) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(new Node(start, null));
        visited.add(matToString(start));

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (sameMatrix(current.state, goal)) {
                printSolution(current);
                return;
            }

            for (int[][] next : getNextStates(current.state)) {
                String key = matToString(next);
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.add(new Node(next, current));
                }
            }
        }

        System.out.println("No solution.");
    }

    // ====================== DFS ======================
    static void solveDFS(int[][] start, int[][] goal) {
        Stack<Node> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(new Node(start, null));
        visited.add(matToString(start));

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (sameMatrix(current.state, goal)) {
                printSolution(current);
                return;
            }

            for (int[][] next : getNextStates(current.state)) {
                String key = matToString(next);
                if (!visited.contains(key)) {
                    visited.add(key);
                    stack.push(new Node(next, current));
                }
            }
        }

        System.out.println("No solution.");
    }

    // ====================== Next States ======================
    static int[][] copy(int[][] m) {
        int[][] c = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                c[i][j] = m[i][j];
        return c;
    }

    static List<int[][]> getNextStates(int[][] state) {
        List<int[][]> list = new ArrayList<>();

        int r = 0, c = 0;

        // find zero position
        search:
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (state[i][j] == 0) {
                    r = i;
                    c = j;
                    break search;
                }

        // possible moves: up, down, left, right
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < 3 && nc >= 0 && nc < 3) {
                int[][] newState = copy(state);
                newState[r][c] = newState[nr][nc];
                newState[nr][nc] = 0;
                list.add(newState);
            }
        }

        return list;
    }

    // ====================== Helpers ======================
    static boolean sameMatrix(int[][] a, int[][] b) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (a[i][j] != b[i][j])
                    return false;
        return true;
    }

    static String matToString(int[][] m) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : m)
            for (int v : row)
                sb.append(v);
        return sb.toString();
    }

    static int[][] readMatrix(Scanner sc) {
        int[][] m = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                m[i][j] = sc.nextInt();
        return m;
    }

    static void printMatrix(int[][] m) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(m[i][j] + " ");
            System.out.println();
        }
    }

    static void printSolution(Node goal) {
        List<int[][]> path = new ArrayList<>();

        while (goal != null) {
            path.add(goal.state);
            goal = goal.parent;
        }

        Collections.reverse(path);

        System.out.println("Solution:");
        for (int[][] m : path) {
            printMatrix(m);
            System.out.println();
        }

        System.out.println("Moves: " + (path.size() - 1));
    }
}
