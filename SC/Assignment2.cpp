#include <iostream>   // for input/output
#include <vector>     // to use dynamic 2D arrays (matrices)
#include <algorithm>  // for max() and min() functions
using namespace std;


vector<vector<double>> fuzzyUnion(const vector<vector<double>>& R, const vector<vector<double>>& S) {
    // create a matrix of same size as R (and S)
    vector<vector<double>> result(R.size(), vector<double>(R[0].size()));
    
    // loop through rows
    for (size_t i = 0; i < R.size(); i++) {
        // loop through columns
        for (size_t j = 0; j < R[0].size(); j++) {
            // union rule: take maximum of membership values
            result[i][j] = max(R[i][j], S[i][j]);
        }
    }
    return result; // return the resulting matrix
}


vector<vector<double>> fuzzyIntersection(const vector<vector<double>>& R, const vector<vector<double>>& S) {
    // create result matrix with same size
    vector<vector<double>> result(R.size(), vector<double>(R[0].size()));
    
    // loop through all cells
    for (size_t i = 0; i < R.size(); i++) {
        for (size_t j = 0; j < R[0].size(); j++) {
            // intersection rule: take minimum of membership values
            result[i][j] = min(R[i][j], S[i][j]);
        }
    }
    return result;
}


vector<vector<double>> fuzzyComplement(const vector<vector<double>>& R) {
    // create result matrix of same size
    vector<vector<double>> result(R.size(), vector<double>(R[0].size()));
    
    for (size_t i = 0; i < R.size(); i++) {
        for (size_t j = 0; j < R[0].size(); j++) {
            // complement rule: subtract value from 1
            result[i][j] = 1.0 - R[i][j];
        }
    }
    return result;
}

// Function to calculate composition of two fuzzy relations (R ○ S)
vector<vector<double>> fuzzyComposition(const vector<vector<double>>& R, const vector<vector<double>>& S) {
    size_t m = R.size();       // number of rows in R
    size_t n = R[0].size();    // number of columns in R (also rows in S)
    size_t p = S[0].size();    // number of columns in S

    // initialize result matrix with zeros (m x p)
    vector<vector<double>> result(m, vector<double>(p, 0.0));

    // loop through rows of R
    for (size_t i = 0; i < m; i++) {
        // loop through columns of S
        for (size_t k = 0; k < p; k++) {
            double maxVal = 0.0; // store maximum(min(R[i][j], S[j][k]))
            
            // loop through intermediate set (columns of R / rows of S)
            for (size_t j = 0; j < n; j++) {
                // fuzzy composition rule: max over min
                maxVal = max(maxVal, min(R[i][j], S[j][k]));
            }
            result[i][k] = maxVal; // store best value in result
        }
    }
    return result;
}


void printRelation(const vector<vector<double>>& R) {
    for (const auto& row : R) {  // loop through each row
        cout << "{ ";
        for (double val : row) { // loop through values in row
            cout << val << " ";  // print membership value
        }
        cout << "}" << endl;     // end of row
    }
}

int main() {
    // Define fuzzy relation R (2x3 matrix)
    vector<vector<double>> R = {
        {0.2, 0.7, 1.0},
        {0.5, 0.3, 0.9}
    };

    // Define fuzzy relation S (2x3 matrix)
    vector<vector<double>> S = {
        {0.6, 0.4, 0.8},
        {0.1, 0.9, 0.5}
    };

    // Print the relations
    cout << "Relation R:" << endl; 
    printRelation(R);
    cout << "Relation S:" << endl; 
    printRelation(S);

    // Perform fuzzy union, intersection, and complement
    auto unionRel = fuzzyUnion(R, S);
    auto interRel = fuzzyIntersection(R, S);
    auto compR = fuzzyComplement(R);

    // Print results
    cout << "\nUnion (R ∪ S):" << endl; 
    printRelation(unionRel);

    cout << "\nIntersection (R ∩ S):" << endl; 
    printRelation(interRel);

    cout << "\nComplement (R'):" << endl; 
    printRelation(compR);

    // Define another relation T (3x2 matrix) for composition
    vector<vector<double>> T = {
        {0.3, 0.6},
        {0.8, 0.2},
        {0.4, 0.9}
    };

    cout << "\nRelation T (for composition):" << endl; 
    printRelation(T);

    // Compute composition (R ○ T)
    auto compRel = fuzzyComposition(R, T);
    cout << "\nComposition (R ○ T):" << endl; 
    printRelation(compRel);

    return 0; // program ends successfully
}
