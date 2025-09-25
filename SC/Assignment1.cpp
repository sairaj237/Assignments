#include <iostream>   // for input/output
#include <vector>     // to use vectors (dynamic arrays)
#include <algorithm>  // for max() and min() functions
using namespace std;


// Rule: take the maximum membership value for each element
vector<double> fuzzyUnion(const vector<double>& A, const vector<double>& B) {
    vector<double> result;  // store the union result
    for (size_t i = 0; i < A.size(); i++) {        // loop through each element
        result.push_back(max(A[i], B[i]));        
    }
    return result;  
}


// Rule: take the minimum membership value for each element
vector<double> fuzzyIntersection(const vector<double>& A, const vector<double>& B) {
    vector<double> result;  // store the intersection result
    for (size_t i = 0; i < A.size(); i++) {        // loop through each element
        result.push_back(min(A[i], B[i]));         
    }
    return result;  // return intersection result
}


// Rule: subtract membership value from 1
vector<double> fuzzyComplement(const vector<double>& A) {
    vector<double> result;  
    for (double val : A) {                  // loop through each membership value
        result.push_back(1.0 - val);        // µA' = 1 - µA
    }
    return result;  // return complement result
}



void printSet(const vector<double>& set) {
    cout << "{ ";
    for (double val : set) {      // loop through each membership value
        cout << val << " ";       
    }
    cout << "}" << endl;         
}

int main() {
    // Example fuzzy sets A and B with membership values
    // Each value is between 0 and 1
    vector<double> A = {0.2, 0.7, 0.5, 1.0};  
    vector<double> B = {0.6, 0.3, 0.8, 0.4};  

    // Print original sets
    cout << "Set A: "; printSet(A);
    cout << "Set B: "; printSet(B);

    // Perform fuzzy operations
    vector<double> unionSet = fuzzyUnion(A, B);              // A ∪ B
    vector<double> intersectionSet = fuzzyIntersection(A, B); // A ∩ B
    vector<double> complementA = fuzzyComplement(A);          // A'

    // Print results
    cout << "Union (A ∪ B): "; printSet(unionSet);
    cout << "Intersection (A ∩ B): "; printSet(intersectionSet);
    cout << "Complement (A'): "; printSet(complementA);

    return 0;  // program ends successfully
}
