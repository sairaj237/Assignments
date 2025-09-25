#include <iostream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <limits>
using namespace std;

const int NUM_CITIES = 5;
const int NUM_ANTS = 10;
const int MAX_ITER = 100;
const double ALPHA = 1.0;   // pheromone importance
const double BETA = 5.0;    // heuristic importance
const double RHO = 0.5;     // pheromone evaporation
const double Q = 100;       // pheromone deposit factor

// Distance matrix (symmetric TSP)
double distMat[NUM_CITIES][NUM_CITIES] = {
    {0, 2, 2, 5, 7},
    {2, 0, 4, 8, 2},
    {2, 4, 0, 1, 3},
    {5, 8, 1, 0, 2},
    {7, 2, 3, 2, 0}
};

// Pheromone matrix
double pheromone[NUM_CITIES][NUM_CITIES];

// Generate a random double in [0,1]
double randDouble() {
    return (double) rand() / RAND_MAX;
}

// Choose next city based on transition probability
int selectNextCity(int current, const vector<bool>& visited) {
    vector<double> probs(NUM_CITIES, 0.0);
    double sum = 0.0;

    for (int j = 0; j < NUM_CITIES; j++) {
        if (!visited[j]) {
            double tau = pow(pheromone[current][j], ALPHA);
            double eta = pow(1.0 / (distMat[current][j] + 1e-6), BETA);
            probs[j] = tau * eta;
            sum += probs[j];
        }
    }

    // roulette wheel selection
    double r = randDouble() * sum;
    double cum = 0.0;
    for (int j = 0; j < NUM_CITIES; j++) {
        if (!visited[j]) {
            cum += probs[j];
            if (cum >= r) return j;
        }
    }

    // fallback
    for (int j = 0; j < NUM_CITIES; j++) {
        if (!visited[j]) return j;
    }

    return -1;
}

int main() {
    srand((unsigned) time(0));

    // Initialize pheromones
    for (int i = 0; i < NUM_CITIES; i++) {
        for (int j = 0; j < NUM_CITIES; j++) {
            pheromone[i][j] = 1.0;
        }
    }

    double bestLength = numeric_limits<double>::infinity();
    vector<int> bestTour;

    for (int iter = 0; iter < MAX_ITER; iter++) {
        vector<vector<int>> antTours(NUM_ANTS);
        vector<double> antLengths(NUM_ANTS, 0.0);

        // Construct tours
        for (int k = 0; k < NUM_ANTS; k++) {
            int start = rand() % NUM_CITIES;
            vector<bool> visited(NUM_CITIES, false);
            visited[start] = true;
            antTours[k].push_back(start);

            int current = start;
            for (int step = 1; step < NUM_CITIES; step++) {
                int next = selectNextCity(current, visited);
                antTours[k].push_back(next);
                visited[next] = true;
                antLengths[k] += distMat[current][next];
                current = next;
            }

            // return to start
            antLengths[k] += distMat[current][start];
            antTours[k].push_back(start);

            if (antLengths[k] < bestLength) {
                bestLength = antLengths[k];
                bestTour = antTours[k];
            }
        }

        // Evaporation
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                pheromone[i][j] *= (1 - RHO);
            }
        }

        // Deposit pheromone
        for (int k = 0; k < NUM_ANTS; k++) {
            double contribution = Q / antLengths[k];
            for (int i = 0; i < NUM_CITIES; i++) {
                int from = antTours[k][i];
                int to = antTours[k][i+1];
                pheromone[from][to] += contribution;
                pheromone[to][from] += contribution;
            }
        }
    }

    // Print result
    cout << "Best tour length: " << bestLength << endl;
    cout << "Best tour: ";
    for (int city : bestTour) cout << city << " ";
    cout << endl;

    return 0;
}
