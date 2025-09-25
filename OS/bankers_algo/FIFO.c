#include <stdio.h>

int isHit(int frame[], int n, int page) {
    for (int i = 0; i < n; i++) {
        if (frame[i] == page)
            return 1;
    }
    return 0;
}

int search(int pages[], int start, int n, int page) {
    for (int i = start; i < n; i++) {
        if (pages[i] == page)
            return i;
    }
    return -1;
}

void printFrames(int frame[], int capacity) {
    for (int i = 0; i < capacity; i++) {
        if (frame[i] != -1)
            printf("%2d ", frame[i]);
        else
            printf("-- ");
    }
}

int fifo(int pages[], int n, int capacity) {
    int frame[capacity];
    int front = 0, rear = 0, count = 0, faults = 0;

    for (int i = 0; i < capacity; i++)
        frame[i] = -1;

    printf("\nFIFO Page Replacement Simulation:\n");
    printf("Step | Page | Frame State       | Fault\n");
    printf("--------------------------------------------\n");

    for (int i = 0; i < n; i++) {
        int fault = 0;

        if (!isHit(frame, capacity, pages[i])) {
            frame[rear] = pages[i];
            rear = (rear + 1) % capacity;
            if (count < capacity)
                count++;
            faults++;
            fault = 1;
        }

        printf("%3d  | %4d | ", i + 1, pages[i]);
        printFrames(frame, capacity);
        printf(" | %s\n", fault ? "Yes" : "No ");
    }

    return faults;
}

int optimal(int pages[], int n, int capacity) {
    int frame[capacity];
    int faults = 0;

    for (int i = 0; i < capacity; i++)
        frame[i] = -1;

    printf("\nOptimal Page Replacement Simulation:\n");
    printf("Step | Page | Frame State       | Fault\n");
    printf("--------------------------------------------\n");

    for (int i = 0; i < n; i++) {
        int fault = 0;

        if (isHit(frame, capacity, pages[i])) {
            printf("%3d  | %4d | ", i + 1, pages[i]);
            printFrames(frame, capacity);
            printf(" | No \n");
            continue;
        }

        int replace = -1, farthest = i + 1;

        for (int j = 0; j < capacity; j++) {
            if (frame[j] == -1) {
                replace = j;
                break;
            }

            int k = search(pages, i + 1, n, frame[j]);
            if (k == -1) {
                replace = j;
                break;
            } else if (k > farthest) {
                farthest = k;
                replace = j;
            }
        }

        frame[replace] = pages[i];
        faults++;
        fault = 1;

        printf("%3d  | %4d | ", i + 1, pages[i]);
        printFrames(frame, capacity);
        printf(" | %s\n", fault ? "Yes" : "No ");
    }

    return faults;
}

int main() {
    int pages[100], n, capacity;

    printf("Enter number of pages: ");
    scanf("%d", &n);

    printf("Enter page reference string: ");
    for (int i = 0; i < n; i++)
        scanf("%d", &pages[i]);

    printf("Enter number of frames: ");
    scanf("%d", &capacity);

    int fifo_faults = fifo(pages, n, capacity);
    printf("\nTotal Page Faults (FIFO): %d\n", fifo_faults);

    int optimal_faults = optimal(pages, n, capacity);
    printf("\nTotal Page Faults (Optimal): %d\n", optimal_faults);

    return 0;
}
