import java.util.ArrayList;
import java.util.Random;

public class Assignment4 {

    static final int N = 5;
    static final int NUM_ANTS = 10;
    static final int NUM_ITERATIONS = 100;
    static final double ALPHA = 1.0;
    static final double BETA = 5.0;
    static final double EVAPORATION = 0.5;
    static final double Q = 100;

    static double[][] distances = {
            {0, 2, 2, 5, 7},
            {2, 0, 4, 8, 2},
            {2, 4, 0, 1, 3},
            {5, 8, 1, 0, 2},
            {7, 2, 3, 2, 0}
    };

    static double[][] pheromones = new double[N][N];

    static Random rand = new Random();

    // Initialize pheromones
    static void initializePheromones() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                pheromones[i][j] = 1.0;
    }

    // Choose next city based on probability
    static int chooseNextCity(int current, boolean[] visited) {
        double[] probabilities = new double[N];
        double sum = 0.0;

        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromones[current][i], ALPHA)
                        * Math.pow(1.0 / distances[current][i], BETA);
                sum += probabilities[i];
            }
        }

        double r = rand.nextDouble() * sum;
        double cumulative = 0.0;

        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                cumulative += probabilities[i];
                if (cumulative >= r)
                    return i;
            }
        }

        // fallback (rare)
        for (int i = 0; i < N; i++)
            if (!visited[i])
                return i;

        return -1;
    }

    // Run the ACO algorithm
    static void runACO() {
        initializePheromones();

        int bestLength = Integer.MAX_VALUE;
        ArrayList<Integer> bestTour = new ArrayList<>();

        for (int iter = 0; iter < NUM_ITERATIONS; iter++) {

            ArrayList<ArrayList<Integer>> allTours = new ArrayList<>();
            int[] tourLengths = new int[NUM_ANTS];

            for (int k = 0; k < NUM_ANTS; k++) {

                boolean[] visited = new boolean[N];
                ArrayList<Integer> tour = new ArrayList<>();

                int current = rand.nextInt(N);
                visited[current] = true;
                tour.add(current);

                for (int step = 1; step < N; step++) {
                    int next = chooseNextCity(current, visited);
                    tour.add(next);
                    visited[next] = true;
                    tourLengths[k] += distances[current][next];
                    current = next;
                }

                // return to start
                tourLengths[k] += distances[current][tour.get(0)];
                tour.add(tour.get(0));

                allTours.add(tour);

                if (tourLengths[k] < bestLength) {
                    bestLength = tourLengths[k];
                    bestTour = new ArrayList<>(tour);
                }
            }

            // evaporate pheromones
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    pheromones[i][j] *= (1 - EVAPORATION);

            // deposit new pheromones
            for (int k = 0; k < NUM_ANTS; k++) {
                double contribution = Q / tourLengths[k];
                ArrayList<Integer> tour = allTours.get(k);

                for (int i = 0; i < N; i++) {
                    int from = tour.get(i);
                    int to = tour.get(i + 1);
                    pheromones[from][to] += contribution;
                    pheromones[to][from] += contribution;
                }
            }

            System.out.println("Iteration " + (iter + 1) + " Best length: " + bestLength);
        }

        System.out.println("\nBest tour found:");
        for (int c : bestTour)
            System.out.print(c + " ");
        System.out.println("\nTour length: " + bestLength);
    }

    public static void main(String[] args) {
        runACO();
    }
}
