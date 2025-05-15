#include <stdio.h>

int main() {
    int n, i, j;
    printf("Enter number of processes: ");
    scanf("%d", &n);

    int bt[n], p[n], prio[n], wt[n], tat[n];
    for (i = 0; i < n; i++) {
        printf("Enter BT and Priority for P%d: ", i + 1);
        scanf("%d %d", &bt[i], &prio[i]);
        p[i] = i;
    }

    // Sort by priority
    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (prio[j] < prio[i]) {
                int tmp = prio[i]; prio[i] = prio[j]; prio[j] = tmp;
                tmp = bt[i]; bt[i] = bt[j]; bt[j] = tmp;
                tmp = p[i]; p[i] = p[j]; p[j] = tmp;
            }
        }
    }

    wt[0] = 0;
    for (i = 1; i < n; i++)
        wt[i] = wt[i - 1] + bt[i - 1];

    printf("\nProcess\tPriority\tBT\tWT\tTAT\n");
    for (i = 0; i < n; i++) {
        tat[i] = wt[i] + bt[i];
        printf("P%d\t%d\t\t%d\t%d\t%d\n", p[i] + 1, prio[i], bt[i], wt[i], tat[i]);
    }

    return 0;
}
