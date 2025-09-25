#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <cmath>
#include <algorithm>

using namespace std;

// Number of nodes
const int N = 5;

// Parameters
const int NUM_ANTS = 10;
const int NUM_ITERATIONS = 100;
const double ALPHA = 1.0; // Importance of pheromone
const double BETA = 5.0;  // Importance of distance
const double EVAPORATION = 0.5;
const double Q = 100;

// Graph distances (symmetric)
double distances[N][N] = {
    {0, 2, 2, 5, 7},
    {2, 0, 4, 8, 2},
    {2, 4, 0, 1, 3},
    {5, 8, 1, 0, 2},
    {7, 2, 3, 2, 0}};

// Pheromone levels on edges
double pheromones[N][N];

// Initialize pheromones
void initializePheromones()
{
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            pheromones[i][j] = 1.0;
}

// Choose next city probabilistically
int chooseNextCity(int current, const vector<bool> &visited)
{
    vector<double> probabilities(N, 0.0);
    double sum = 0.0;

    // Calculate probability for each unvisited city
    for (int i = 0; i < N; i++)
    {
        if (!visited[i])
        {
            probabilities[i] = pow(pheromones[current][i], ALPHA) * pow(1.0 / distances[current][i], BETA);
            sum += probabilities[i];
        }
    }

    // Roulette wheel selection
    double r = ((double)rand() / RAND_MAX) * sum;
    double cumulative = 0.0;
    for (int i = 0; i < N; i++)
    {
        if (!visited[i])
        {
            cumulative += probabilities[i];
            if (cumulative >= r)
                return i;
        }
    }

    // Fallback (should not happen)
    for (int i = 0; i < N; i++)
    {
        if (!visited[i])
            return i;
    }
    return -1;
}

// Run the ACO algorithm
void runACO()
{
    initializePheromones();
    int bestLength = 1e9;
    vector<int> bestTour;

    for (int iter = 0; iter < NUM_ITERATIONS; iter++)
    {
        vector<vector<int>> allTours(NUM_ANTS);
        vector<int> tourLengths(NUM_ANTS, 0);

        // Each ant builds a tour
        for (int k = 0; k < NUM_ANTS; k++)
        {
            vector<bool> visited(N, false);
            vector<int> tour;
            int current = rand() % N;
            tour.push_back(current);
            visited[current] = true;

            for (int step = 1; step < N; step++)
            {
                int nextCity = chooseNextCity(current, visited);
                tour.push_back(nextCity);
                visited[nextCity] = true;
                tourLengths[k] += distances[current][nextCity];
                current = nextCity;
            }
            // Return to start city
            tourLengths[k] += distances[current][tour[0]];
            tour.push_back(tour[0]);

            allTours[k] = tour;

            if (tourLengths[k] < bestLength)
            {
                bestLength = tourLengths[k];
                bestTour = tour;
            }
        }

        // Evaporate pheromones
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                pheromones[i][j] *= (1 - EVAPORATION);

        // Deposit new pheromones
        for (int k = 0; k < NUM_ANTS; k++)
        {
            double contribution = Q / tourLengths[k];
            for (int i = 0; i < N; i++)
            {
                int from = allTours[k][i];
                int to = allTours[k][i + 1];
                pheromones[from][to] += contribution;
                pheromones[to][from] += contribution;
            }
        }

        cout << "Iteration " << iter + 1 << " Best length: " << bestLength << endl;
    }

    cout << "\nBest tour found:\n";
    for (int city : bestTour)
        cout << city << " ";
    cout << "\nTour length: " << bestLength << endl;
}

int main()
{
    srand(time(0));
    runACO();
    return 0;
}