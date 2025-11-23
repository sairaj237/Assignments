import java.util.*;

public class AStar {

    static class Node {
        int row, col;
        int g, h, f;
        Node parent;

        Node(int r, int c) {
            row = r;
            col = c;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // grid input
        System.out.print("Enter grid rows: ");
        int R = sc.nextInt();

        System.out.print("Enter grid cols: ");
        int C = sc.nextInt();

        int[][] grid = new int[R][C];

        System.out.println("Enter grid (0 = free, 1 = blocked):");
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++)
                grid[i][j] = sc.nextInt();

        System.out.print("Enter start row and col: ");
        int sr = sc.nextInt();
        int sc2 = sc.nextInt();

        System.out.print("Enter goal row and col: ");
        int gr = sc.nextInt();
        int gc2 = sc.nextInt();

        List<Node> path = aStar(grid, sr, sc2, gr, gc2);

        if (path == null)
            System.out.println("No path found.");
        else {
            System.out.println("Path found:");
            for (Node n : path)
                System.out.println("(" + n.row + ", " + n.col + ")");
        }
    }

    // --------------------------------------------------------------
    // A* Algorithm
    // --------------------------------------------------------------
    static List<Node> aStar(int[][] grid, int sr, int sc, int gr, int gc) {

        int R = grid.length;
        int C = grid[0].length;

        Node[][] nodes = new Node[R][C];
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++)
                nodes[i][j] = new Node(i, j);

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        boolean[][] closed = new boolean[R][C];

        Node start = nodes[sr][sc];
        Node goal = nodes[gr][gc];

        start.g = 0;
        start.h = manhattan(sr, sc, gr, gc);
        start.f = start.h;

        open.add(start);

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!open.isEmpty()) {

            Node current = open.poll();
            closed[current.row][current.col] = true;

            if (current == goal) {
                return reconstructPath(goal);
            }

            for (int[] d : dirs) {
                int nr = current.row + d[0];
                int nc = current.col + d[1];

                if (nr < 0 || nr >= R || nc < 0 || nc >= C)
                    continue;

                if (grid[nr][nc] == 1)
                    continue;

                if (closed[nr][nc])
                    continue;

                Node neighbor = nodes[nr][nc];
                int newG = current.g + 1;

                // found a better path
                if (newG < neighbor.g || !open.contains(neighbor)) {
                    neighbor.g = newG;
                    neighbor.h = manhattan(nr, nc, gr, gc);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;

                    if (!open.contains(neighbor))
                        open.add(neighbor);
                }
            }
        }

        return null; // no path
    }

    // heuristic
    static int manhattan(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    // reconstruct final path
    static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node cur = goal;

        while (cur != null) {
            path.add(cur);
            cur = cur.parent;
        }

        Collections.reverse(path);
        return path;
    }
}
