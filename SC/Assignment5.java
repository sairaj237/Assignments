import java.util.*;

public class Assignment5 {

    static final int NUM_ANTS = 10;
    static final int MAX_ITER = 100;
    static final double ALPHA = 1.0;
    static final double BETA = 5.0;
    static final double RHO = 0.5;
    static final double Q = 100;

    static double[][] distMat;
    static double[][] pheromone;
    static int NUM_CITIES;

    static Random rand = new Random();

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

        double r = rand.nextDouble() * sum;
        double cum = 0;

        for (int j = 0; j < NUM_CITIES; j++) {
            if (!visited[j]) {
                cum += prob[j];
                if (cum >= r) return j;
            }
        }

        // fallback
        for (int j = 0; j < NUM_CITIES; j++)
            if (!visited[j]) return j;

        return -1;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of cities: ");
        NUM_CITIES = sc.nextInt();

        distMat = new double[NUM_CITIES][NUM_CITIES];
        pheromone = new double[NUM_CITIES][NUM_CITIES];

        System.out.println("Enter distance matrix (" + NUM_CITIES + "x" + NUM_CITIES + "):");
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                distMat[i][j] = sc.nextDouble();
            }
        }

        // initialize pheromones
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
                List<Integer> tour = new ArrayList<>();

                int start = rand.nextInt(NUM_CITIES);
                visited[start] = true;
                tour.add(start);

                int current = start;

                for (int step = 1; step < NUM_CITIES; step++) {
                    int next = selectNextCity(current, visited);
                    visited[next] = true;
                    tour.add(next);
                    antLengths[k] += distMat[current][next];
                    current = next;
                }

                antLengths[k] += distMat[current][start]; // return to start
                tour.add(start);

                if (antLengths[k] < bestLength) {
                    bestLength = antLengths[k];
                    bestTour = new ArrayList<>(tour);
                }

                antTours.add(tour);
            }

            // evaporation
            for (int i = 0; i < NUM_CITIES; i++)
                for (int j = 0; j < NUM_CITIES; j++)
                    pheromone[i][j] *= (1 - RHO);

            // deposit
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

            System.out.println("Iteration " + (iter + 1) + " - Best: " + bestLength);
        }

        System.out.println("\nBest tour:");
        for (int c : bestTour) System.out.print(c + " ");
        System.out.println("\nBest length: " + bestLength);
    }
}
