import java.util.ArrayList;
import java.util.Random;

public class Assignment3 {

    // Objective function: Sphere -> sum(x^2)
    static double objective(ArrayList<Double> position) {
        double sum = 0.0;
        for (double x : position) sum += x * x;
        return sum;
    }

    // Random double in range [min, max]
    static double randDouble(Random rand, double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    public static void main(String[] args) {

        Random rand = new Random();

        int dim = 5;
        int wolves = 20;
        int maxIter = 100;
        double lb = -10.0, ub = 10.0;

        // Initialize wolf positions
        ArrayList<ArrayList<Double>> positions = new ArrayList<>();
        for (int i = 0; i < wolves; i++) {
            ArrayList<Double> pos = new ArrayList<>();
            for (int d = 0; d < dim; d++)
                pos.add(randDouble(rand, lb, ub));
            positions.add(pos);
        }

        // Alpha, Beta, Delta
        ArrayList<Double> Alpha = new ArrayList<>();
        ArrayList<Double> Beta = new ArrayList<>();
        ArrayList<Double> Delta = new ArrayList<>();

        double AlphaScore = Double.POSITIVE_INFINITY;
        double BetaScore  = Double.POSITIVE_INFINITY;
        double DeltaScore = Double.POSITIVE_INFINITY;

        // Main Loop
        for (int iter = 0; iter < maxIter; iter++) {

            for (int i = 0; i < wolves; i++) {

                double fitness = objective(positions.get(i));

                // Rank wolves
                if (fitness < AlphaScore) {
                    DeltaScore = BetaScore; Delta = new ArrayList<>(Beta);
                    BetaScore  = AlphaScore; Beta  = new ArrayList<>(Alpha);
                    AlphaScore = fitness;    Alpha = new ArrayList<>(positions.get(i));
                }
                else if (fitness < BetaScore) {
                    DeltaScore = BetaScore; Delta = new ArrayList<>(Beta);
                    BetaScore = fitness;    Beta  = new ArrayList<>(positions.get(i));
                }
                else if (fitness < DeltaScore) {
                    DeltaScore = fitness;   Delta = new ArrayList<>(positions.get(i));
                }
            }

            double a = 2.0 - iter * (2.0 / maxIter);

            // Update positions
            for (int i = 0; i < wolves; i++) {
                for (int d = 0; d < dim; d++) {

                    double r1 = rand.nextDouble(), r2 = rand.nextDouble();
                    double A1 = 2 * a * r1 - a;
                    double C1 = 2 * r2;
                    double D_alpha = Math.abs(C1 * Alpha.get(d) - positions.get(i).get(d));
                    double X1 = Alpha.get(d) - A1 * D_alpha;

                    r1 = rand.nextDouble(); r2 = rand.nextDouble();
                    double A2 = 2 * a * r1 - a;
                    double C2 = 2 * r2;
                    double D_beta = Math.abs(C2 * Beta.get(d) - positions.get(i).get(d));
                    double X2 = Beta.get(d) - A2 * D_beta;

                    r1 = rand.nextDouble(); r2 = rand.nextDouble();
                    double A3 = 2 * a * r1 - a;
                    double C3 = 2 * r2;
                    double D_delta = Math.abs(C3 * Delta.get(d) - positions.get(i).get(d));
                    double X3 = Delta.get(d) - A3 * D_delta;

                    double newPos = (X1 + X2 + X3) / 3.0;

                    if (newPos < lb) newPos = lb;
                    if (newPos > ub) newPos = ub;

                    positions.get(i).set(d, newPos);
                }
            }

            System.out.println("Iteration " + (iter + 1) + " best fitness = " + AlphaScore);
        }

        System.out.print("\nBest solution found (Alpha wolf): ");
        for (double x : Alpha) System.out.print(x + " ");

        System.out.println("\nBest objective value = " + AlphaScore);
    }
}
