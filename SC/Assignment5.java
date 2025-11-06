import java.util.*;

public class Assignment5 {

    static final int NUM_CITIES = 5;
    static final int NUM_ANTS = 10;
    static final int MAX_ITER = 100;
    static final double ALPHA = 1.0;
    static final double BETA = 5.0;
    static final double RHO = 0.5;
    static final double Q = 100;

    static double[][] distMat = {
        {0, 2, 2, 5, 7},
        {2, 0, 4, 8, 2},
        {2, 4, 0, 1, 3},
        {5, 8, 1, 0, 2},
        {7, 2, 3, 2, 0}
    };

    static double[][] pheromone = new double[NUM_CITIES][NUM_CITIES];
    static Random rand = new Random();

    static double randDouble() {
        return rand.nextDouble();
    }

    static int selectNextCity(int current, boolean[] visited) {
        double[] prob = new double[NUM_CITIES];
        double sum = 0;

        for (int j = 0; j < NUM_CITIES; j++) {
            if (!visited[j]) {
                double tau = Math.pow(pheromone[current][j], ALPHA);
                double eta = Math.pow(1.0 / (distMat[current][j] + 1e-6), BETA);
                prob[j] = tau * eta;
                sum += prob[j];
            }
        }

        double r = randDouble() * sum;
        double cum = 0;
        for (int j = 0; j < NUM_CITIES; j++) {
            if (!visited[j]) {
                cum += prob[j];
                if (cum >= r) return j;
            }
        }

        for (int j = 0; j < NUM_CITIES; j++)
            if (!visited[j]) return j;

        return -1;
    }

    public static void main(String[] args) {
        // initialize pheromone
        for (int i = 0; i < NUM_CITIES; i++)
            for (int j = 0; j < NUM_CITIES; j++)
                pheromone[i][j] = 1.0;

        double bestLength = Double.MAX_VALUE;
        List<Integer> bestTour = new ArrayList<>();

        for (int iter = 0; iter < MAX_ITER; iter++) {

            List<List<Integer>> antTours = new ArrayList<>();
            double[] antLengths = new double[NUM_ANTS];

            for (int k = 0; k < NUM_ANTS; k++) {
                boolean[] visited = new boolean[NUM_CITIES];
                int start = rand.nextInt(NUM_CITIES);
                List<Integer> tour = new ArrayList<>();

                visited[start] = true;
                tour.add(start);

                int current = start;
                for (int step = 1; step < NUM_CITIES; step++) {
                    int next = selectNextCity(current, visited);
                    tour.add(next);
                    visited[next] = true;
                    antLengths[k] += distMat[current][next];
                    current = next;
                }

                antLengths[k] += distMat[current][start];
                tour.add(start);

                if (antLengths[k] < bestLength) {
                    bestLength = antLengths[k];
                    bestTour = tour;
                }

                antTours.add(tour);
            }

            // Evaporation
            for (int i = 0; i < NUM_CITIES; i++)
                for (int j = 0; j < NUM_CITIES; j++)
                    pheromone[i][j] *= (1 - RHO);

            // Deposit pheromone
            for (int k = 0; k < NUM_ANTS; k++) {
                double contribution = Q / antLengths[k];
                List<Integer> tour = antTours.get(k);

                for (int i = 0; i < NUM_CITIES; i++) {
                    int from = tour.get(i);
                    int to = tour.get(i + 1);
                    pheromone[from][to] += contribution;
                    pheromone[to][from] += contribution;
                }
            }
        }

        System.out.println("Best length: " + bestLength);
        System.out.print("Best tour: ");
        for (int c : bestTour) System.out.print(c + " ");
    }
}
