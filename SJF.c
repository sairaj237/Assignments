#include <stdio.h>
#include <limits.h>

int main() {
    int n, i, t = 0, smallest, finish_time;
    printf("Enter number of processes: ");
    scanf("%d", &n);

    int bt[n], rt[n], at[n], wt[n], tat[n], completed = 0;
    for (i = 0; i < n; i++) {
        printf("Enter AT and BT for P%d: ", i + 1);
        scanf("%d %d", &at[i], &bt[i]);
        rt[i] = bt[i];
    }

    while (completed != n) {
        smallest = -1;
        int min_bt = INT_MAX;

        for (i = 0; i < n; i++) {
            if (at[i] <= t && rt[i] > 0 && rt[i] < min_bt) {
                min_bt = rt[i];
                smallest = i;
            }
        }

        if (smallest == -1) {
            t++;
            continue;
        }

        rt[smallest]--;
        if (rt[smallest] == 0) {
            completed++;
            finish_time = t + 1;
            tat[smallest] = finish_time - at[smallest];
            wt[smallest] = tat[smallest] - bt[smallest];
        }
        t++;
    }

    printf("\nProcess\tAT\tBT\tWT\tTAT\n");
    for (i = 0; i < n; i++)
        printf("P%d\t%d\t%d\t%d\t%d\n", i + 1, at[i], bt[i], wt[i], tat[i]);

    return 0;
}
