public class EightQueens {

    final int N = 8;
    int[] board = new int[N];  
    // board[i] = column position of queen in row i

    public static void main(String[] args) {
        EightQueens q = new EightQueens();
        q.solve();
    }

    // -----------------------------------------------------
    // Start solving
    // -----------------------------------------------------
    void solve() {
        if (placeQueen(0)) {
            System.out.println("Solution found:");
            printBoard();
        } else {
            System.out.println("No solution.");
        }
    }

    // -----------------------------------------------------
    // Recursive DFS search
    // -----------------------------------------------------
    boolean placeQueen(int row) {
        if (row == N) return true;  // all queens placed

        for (int col = 0; col < N; col++) {
            if (isSafe(row, col)) {
                board[row] = col;

                if (placeQueen(row + 1))
                    return true;

                // backtrack automatically by trying new col
            }
        }
        return false;
    }

    // -----------------------------------------------------
    // Check if placing a queen is safe
    // -----------------------------------------------------
    boolean isSafe(int row, int col) {

        for (int r = 0; r < row; r++) {
            int c = board[r];

            if (c == col) return false;                 // same column
            if (Math.abs(r - row) == Math.abs(c - col)) return false; // diagonal
        }
        return true;
    }

    // -----------------------------------------------------
    // Print final chessboard
    // -----------------------------------------------------
    void printBoard() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r] == c)
                    System.out.print(" Q ");
                else
                    System.out.print(" . ");
            }
            System.out.println();
        }
    }
}
