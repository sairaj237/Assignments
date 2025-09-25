#include <stdio.h>
#include <stdbool.h>

#define P 5 // Number of processes
#define R 3 // Number of resources

// Function to check if the system is in a safe state
bool isSafe(int available[], int max[P][R], int allocation[P][R], int need[P][R], int safeSequence[]) {
    bool finish[P] = {false};
    int work[R];
    for (int i = 0; i < R; i++) {
        work[i] = available[i];
    }
    
    int count = 0;
    while (count < P) {
        bool found = false;
        for (int i = 0; i < P; i++) {
            if (!finish[i]) {
                bool canAllocate = true;
                for (int j = 0; j < R; j++) {
                    if (need[i][j] > work[j]) {
                        canAllocate = false;
                        break;
                    }
                }
                
                if (canAllocate) {
                    for (int j = 0; j < R; j++) {
                        work[j] += allocation[i][j];
                    }
                    safeSequence[count++] = i;
                    finish[i] = true;
                    found = true;
                }
            }
        }
        
        if (!found) {
            return false; // System is not in a safe state
        }
    }
    return true;
}

// Function to handle resource requests
bool requestResources(int process, int request[], int available[], int max[P][R], int allocation[P][R], int need[P][R]) {
    for (int i = 0; i < R; i++) {
        if (request[i] > need[process][i]) {
            printf("Error: Process %d has exceeded its maximum claim.\n", process);
            return false;
        }
        if (request[i] > available[i]) {
            printf("Process %d must wait, not enough resources available.\n", process);
            return false;
        }
    }
    
    // Temporarily allocate resources
    for (int i = 0; i < R; i++) {
        available[i] -= request[i];
        allocation[process][i] += request[i];
        need[process][i] -= request[i];
    }
    
    int safeSequence[P];
    if (isSafe(available, max, allocation, need, safeSequence)) {
        printf("Request granted. System remains in a safe state.\n");
        return true;
    } else {
        printf("Request denied. System would enter an unsafe state.\n");
        for (int i = 0; i < R; i++) {
            available[i] += request[i];
            allocation[process][i] -= request[i];
            need[process][i] += request[i];
        }
        return false;
    }
}

int main() {
    int available[R] = {3, 3, 2};
    int max[P][R] = {
        {7, 5, 3},
        {3, 2, 2},
        {9, 0, 2},
        {2, 2, 2},
        {4, 3, 3}
    };
    int allocation[P][R] = {
        {0, 1, 0},
        {2, 0, 0},
        {3, 0, 2},
        {2, 1, 1},
        {0, 0, 2}
    };
    int need[P][R];
    for (int i = 0; i < P; i++) {
        for (int j = 0; j < R; j++) {
            need[i][j] = max[i][j] - allocation[i][j];
        }
    }
    
    int safeSequence[P];
    if (isSafe(available, max, allocation, need, safeSequence)) {
        printf("System is in a safe state. Safe sequence: ");
        for (int i = 0; i < P; i++) {
            printf("P%d ", safeSequence[i]);
        }
        printf("\n");
    } else {
        printf("System is NOT in a safe state!\n");
    }
    
    int process = 1;
    int request[R] = {1, 0, 2};
    printf("Process %d requesting resources: {1, 0, 2}\n", process);
    requestResources(process, request, available, max, allocation, need);
    
    return 0;
}
