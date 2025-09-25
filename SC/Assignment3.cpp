#include <iostream>   // for input/output
#include <vector>     // to use vectors (dynamic arrays)
#include <cmath>      // for fabs(), pow(), etc.
#include <cstdlib>    // for rand()
#include <ctime>      // for seeding random generator
#include <limits>     // for infinity (to initialize best scores)
using namespace std;

// Objective function: Sphere function (minimization problem)
// f(x) = sum(x_i^2), global minimum is at (0,0,...,0) with value 0
double objective(const vector<double>& position) {
    double sum = 0.0;
    for (double x : position) sum += x * x; // sum of squares
    return sum;
}

// Generate random double between [min, max]
double randDouble(double min, double max) {
    return min + (max - min) * ((double)rand() / RAND_MAX);
}

int main() {
    srand(time(0));  // seed random number generator with current time

    int dim = 5;                // dimension of problem (number of variables)
    int wolves = 20;            // number of wolves in pack
    int maxIter = 100;          // maximum number of iterations
    double lb = -10.0, ub = 10.0; // lower and upper bounds of search space

    // Initialize positions of wolves randomly in search space
    vector<vector<double>> positions(wolves, vector<double>(dim));
    for (int i = 0; i < wolves; i++)
        for (int d = 0; d < dim; d++)
            positions[i][d] = randDouble(lb, ub);

    // Alpha, Beta, Delta wolves (top 3 best solutions found so far)
    vector<double> Alpha(dim), Beta(dim), Delta(dim);
    double AlphaScore = numeric_limits<double>::infinity(); // best fitness
    double BetaScore  = numeric_limits<double>::infinity(); // 2nd best
    double DeltaScore = numeric_limits<double>::infinity(); // 3rd best

    // ================= MAIN LOOP ==================
    for (int iter = 0; iter < maxIter; iter++) {
        // Evaluate all wolves
        for (int i = 0; i < wolves; i++) {
            double fitness = objective(positions[i]);

            // Update Alpha, Beta, Delta based on fitness
            if (fitness < AlphaScore) {
                DeltaScore = BetaScore; Delta = Beta;   // shift down
                BetaScore = AlphaScore; Beta = Alpha;
                AlphaScore = fitness; Alpha = positions[i]; // new best
            }
            else if (fitness < BetaScore) {
                DeltaScore = BetaScore; Delta = Beta;
                BetaScore = fitness; Beta = positions[i]; // new 2nd best
            }
            else if (fitness < DeltaScore) {
                DeltaScore = fitness; Delta = positions[i]; // new 3rd best
            }
        }

        // 'a' decreases linearly from 2 to 0, controls exploration/exploitation
        double a = 2.0 - iter * (2.0 / maxIter);

        // Update position of each wolf
        for (int i = 0; i < wolves; i++) {
            for (int d = 0; d < dim; d++) {
                // Influence of Alpha wolf
                double r1 = randDouble(0,1), r2 = randDouble(0,1);
                double A1 = 2*a*r1 - a;   // coefficient A
                double C1 = 2*r2;         // coefficient C
                double D_alpha = fabs(C1*Alpha[d] - positions[i][d]); // distance to alpha
                double X1 = Alpha[d] - A1*D_alpha; // candidate position wrt alpha

                // Influence of Beta wolf
                r1 = randDouble(0,1); r2 = randDouble(0,1);
                double A2 = 2*a*r1 - a;
                double C2 = 2*r2;
                double D_beta = fabs(C2*Beta[d] - positions[i][d]);
                double X2 = Beta[d] - A2*D_beta;

                // Influence of Delta wolf
                r1 = randDouble(0,1); r2 = randDouble(0,1);
                double A3 = 2*a*r1 - a;
                double C3 = 2*r2;
                double D_delta = fabs(C3*Delta[d] - positions[i][d]);
                double X3 = Delta[d] - A3*D_delta;

                // New position = average influence of Alpha, Beta, Delta
                positions[i][d] = (X1 + X2 + X3) / 3.0;

                // Keep the wolf inside search boundaries
                if (positions[i][d] < lb) positions[i][d] = lb;
                if (positions[i][d] > ub) positions[i][d] = ub;
            }
        }

        // Print best fitness found so far (progress)
        cout << "Iteration " << iter+1 << " best fitness = " << AlphaScore << endl;
    }

    // ================= FINAL RESULT ==================
    cout << "\nBest solution found (Alpha wolf): ";
    for (double x : Alpha) cout << x << " "; // print coordinates of best wolf
    cout << "\nBest objective value = " << AlphaScore << endl;

    return 0; // program ends successfully
}
