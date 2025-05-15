#include <stdio.h>

int main() {
    int n, tq, i;
    printf("Enter number of processes: ");
    scanf("%d", &n);
    printf("Enter time quantum: ");
    scanf("%d", &tq);

    int bt[n], rt[n], at[n], wt[n], tat[n];
    for (i = 0; i < n; i++) {
        printf("Enter AT and BT for P%d: ", i + 1);
        scanf("%d %d", &at[i], &bt[i]);
        rt[i] = bt[i];
    }

    int t = 0, done = 0;
    while (done < n) {
        int idle = 1;
        for (i = 0; i < n; i++) {
            if (at[i] <= t && rt[i] > 0) {
                idle = 0;
                if (rt[i] > tq) {
                    t += tq;
                    rt[i] -= tq;
                } else {
                    t += rt[i];
                    wt[i] = t - bt[i] - at[i];
                    rt[i] = 0;
                    tat[i] = wt[i] + bt[i];
                    done++;
                }
            }
        }
        if (idle) t++;
    }

    printf("\nProcess\tAT\tBT\tWT\tTAT\n");
    for (i = 0; i < n; i++)
        printf("P%d\t%d\t%d\t%d\t%d\n", i + 1, at[i], bt[i], wt[i], tat[i]);

    return 0;
}
