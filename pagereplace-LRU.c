#include <stdio.h>

int main() {
    int frames, pages[50], temp[50], totalPages, count = 0, m, n, position, k, lru[50], time = 0, flag1, flag2;

    printf("Enter number of pages: ");
    scanf("%d", &totalPages);

    printf("Enter the page numbers: ");
    for (m = 0; m < totalPages; m++) {
        scanf("%d", &pages[m]);
    }

    printf("Enter number of frames: ");
    scanf("%d", &frames);

    for (m = 0; m < frames; m++) {
        temp[m] = -1;
    }

    printf("\nPage\tFrames\n");

    for (m = 0; m < totalPages; m++) {
        flag1 = flag2 = 0;
        for (k = 0; k < frames; k++) {
            if (temp[k] == pages[m]) {
                time++;
                lru[k] = time;
                flag1 = flag2 = 1;
                break;
            }
        }

        if (!flag1) {
            for (k = 0; k < frames; k++) {
                if (temp[k] == -1) {
                    time++;
                    temp[k] = pages[m];
                    lru[k] = time;
                    flag2 = 1;
                    break;
                }
            }
        }

        if (!flag2) {
            int min = lru[0], pos = 0;
            for (k = 1; k < frames; k++) {
                if (lru[k] < min) {
                    min = lru[k];
                    pos = k;
                }
            }
            time++;
            temp[pos] = pages[m];
            lru[pos] = time;
        }

        printf("%d\t", pages[m]);
        for (k = 0; k < frames; k++) {
            printf("%d ", temp[k]);
        }
        printf("\n");
    }

    return 0;
}
