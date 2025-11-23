import java.util.*;

public class MinimaxTicTacToe {

    static char[][] board = {
            { ' ', ' ', ' ' },
            { ' ', ' ', ' ' },
            { ' ', ' ', ' ' }
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char player = 'X';  // human
        char ai = 'O';      // computer

        while (true) {
            printBoard();

            if (isGameOver()) {
                showResult();
                break;
            }

            if (player == 'X') {
                System.out.print("Enter your move (row col): ");
                int r = sc.nextInt();
                int c = sc.nextInt();

                if (board[r][c] == ' ') {
                    board[r][c] = 'X';
                    player = 'O';
                } else {
                    System.out.println("Invalid move, try again.");
                }
            }

            else {
                System.out.println("AI is thinking...");
                int[] bestMove = findBestMove();
                board[bestMove[0]][bestMove[1]] = 'O';
                player = 'X';
            }
        }
    }

    // ------------------ MINIMAX ------------------
    static int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[r][c] == ' ') {
                    board[r][c] = 'O';
                    int score = minimax(false);
                    board[r][c] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = r;
                        bestCol = c;
                    }
                }

        return new int[]{bestRow, bestCol};
    }

    static int minimax(boolean isMaximizing) {

        if (checkWin('O')) return 1;     // AI wins
        if (checkWin('X')) return -1;    // Human wins
        if (isFull()) return 0;          // Draw

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (board[r][c] == ' ') {
                        board[r][c] = 'O';
                        bestScore = Math.max(bestScore, minimax(false));
                        board[r][c] = ' ';
                    }
            return bestScore;
        }

        else {
            int bestScore = Integer.MAX_VALUE;

            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (board[r][c] == ' ') {
                        board[r][c] = 'X';
                        bestScore = Math.min(bestScore, minimax(true));
                        board[r][c] = ' ';
                    }
            return bestScore;
        }
    }

    // ------------------ GAME CHECKS ------------------
    static boolean checkWin(char p) {
        for (int i = 0; i < 3; i++)
            if (board[i][0] == p && board[i][1] == p && board[i][2] == p)
                return true;

        for (int j = 0; j < 3; j++)
            if (board[0][j] == p && board[1][j] == p && board[2][j] == p)
                return true;

        if (board[0][0] == p && board[1][1] == p && board[2][2] == p)
            return true;

        if (board[0][2] == p && board[1][1] == p && board[2][0] == p)
            return true;

        return false;
    }

    static boolean isFull() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[r][c] == ' ')
                    return false;
        return true;
    }

    static boolean isGameOver() {
        return checkWin('X') || checkWin('O') || isFull();
    }

    // ------------------ DISPLAY ------------------
    static void printBoard() {
        System.out.println("-------------");
        for (int r = 0; r < 3; r++) {
            System.out.print("| ");
            for (int c = 0; c < 3; c++) {
                System.out.print(board[r][c] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    static void showResult() {
        printBoard();
        if (checkWin('X'))
            System.out.println("You win!");
        else if (checkWin('O'))
            System.out.println("AI wins!");
        else
            System.out.println("Draw!");
    }
}
