#include <stdio.h>
#include <stdbool.h>

#define MAX 10

int n, m; // number of processes and resource types
int Allocation[MAX][MAX], Max[MAX][MAX], Need[MAX][MAX], Available[MAX];

// Calculate Need matrix
void calculateNeed() {
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            Need[i][j] = Max[i][j] - Allocation[i][j];
}

// Safety algorithm
bool isSafe() {
    int Work[MAX], Finish[MAX] = {0}, safeSeq[MAX];
    for (int i = 0; i < m; i++)
        Work[i] = Available[i];

    int count = 0;
    while (count < n) {
        bool found = false;
        for (int i = 0; i < n; i++) {
            if (!Finish[i]) {
                int j;
                for (j = 0; j < m; j++)
                    if (Need[i][j] > Work[j])
                        break;

                if (j == m) {
                    for (int k = 0; k < m; k++)
                        Work[k] += Allocation[i][k];

                    safeSeq[count++] = i;
                    Finish[i] = 1;
                    found = true;
                }
            }
        }
        if (!found) {
            printf("⚠️ System is NOT in a safe state.\n");
            return false;
        }
    }

    printf("✅ System is in a safe state.\nSafe sequence: ");
    for (int i = 0; i < n; i++)
        printf("P%d ", safeSeq[i]);
    printf("\n");
    return true;
}

// Resource Request algorithm
void requestResources(int pid, int request[]) {
    // Step 1: Check if request ≤ Need
    for (int i = 0; i < m; i++) {
        if (request[i] > Need[pid][i]) {
            printf("❌ Error: Process has exceeded its maximum claim.\n");
            return;
        }
    }

    // Step 2: Check if request ≤ Available
    for (int i = 0; i < m; i++) {
        if (request[i] > Available[i]) {
            printf("❌ Error: Resources not available. Process must wait.\n");
            return;
        }
    }

    // Step 3: Pretend to allocate and check safety
    for (int i = 0; i < m; i++) {
        Available[i] -= request[i];
        Allocation[pid][i] += request[i];
        Need[pid][i] -= request[i];
    }

    if (isSafe()) {
        printf("✅ Request can be granted.\n");
    } else {
        // Rollback
        for (int i = 0; i < m; i++) {
            Available[i] += request[i];
            Allocation[pid][i] -= request[i];
            Need[pid][i] += request[i];
        }
        printf("❌ Request cannot be granted. System would be unsafe.\n");
    }
}

int main() {
    printf("Enter number of processes: ");
    scanf("%d", &n);
    printf("Enter number of resource types: ");
    scanf("%d", &m);

    printf("\nEnter Allocation matrix:\n");
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            scanf("%d", &Allocation[i][j]);

    printf("\nEnter Max matrix:\n");
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            scanf("%d", &Max[i][j]);

    printf("\nEnter Available resources:\n");
    for (int i = 0; i < m; i++)
        scanf("%d", &Available[i]);

    calculateNeed();

    if (!isSafe())
        return 0;

    char choice;
    do {
        int pid, request[MAX];
        printf("\nEnter process ID making request (0 to %d): ", n - 1);
        scanf("%d", &pid);
        printf("Enter request for each resource:\n");
        for (int i = 0; i < m; i++)
            scanf("%d", &request[i]);

        requestResources(pid, request);

        printf("\nDo you want to make another request? (y/n): ");
        scanf(" %c", &choice);
    } while (choice == 'y' || choice == 'Y');

    return 0;
}
