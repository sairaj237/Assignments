#include <stdio.h>
#include <limits.h>

// Function to check if a page is present in frames
int isPresent(int frames[], int frame_count, int page) {
    for (int i = 0; i < frame_count; i++) {
        if (frames[i] == page)
            return i;
    }
    return -1;
}

// FIFO Page Replacement Algorithm
void fifo(int pages[], int n, int frame_count) {
    printf("\nFIFO Page Replacement:\n");
    int frames[frame_count];
    int front = 0, page_faults = 0;
    for (int i = 0; i < frame_count; i++)
        frames[i] = -1;
    
    for (int i = 0; i < n; i++) {
        if (isPresent(frames, frame_count, pages[i]) == -1) {
            frames[front] = pages[i];
            front = (front + 1) % frame_count;
            page_faults++;
        }
        printf("Frames: ");
        for (int j = 0; j < frame_count; j++)
            printf("%d ", frames[j]);
        printf("\n");
    }
    printf("Total Page Faults: %d\n", page_faults);
}

// LRU Page Replacement Algorithm
void lru(int pages[], int n, int frame_count) {
    printf("\nLRU Page Replacement:\n");
    int frames[frame_count], recent[frame_count];
    int page_faults = 0;
    for (int i = 0; i < frame_count; i++) {
        frames[i] = -1;
        recent[i] = -1;
    }
    
    for (int i = 0; i < n; i++) {
        int index = isPresent(frames, frame_count, pages[i]);
        
        if (index == -1) { // Page fault
            int lru = 0;
            for (int j = 1; j < frame_count; j++) {
                if (recent[j] < recent[lru])
                    lru = j;
            }
            frames[lru] = pages[i];
            page_faults++;
            recent[lru] = i; // Update recent use
        } else {
            recent[index] = i; // Update recent use if found
        }
        
        printf("Frames: ");
        for (int j = 0; j < frame_count; j++)
            printf("%d ", frames[j]);
        printf("\n");
    }
    printf("Total Page Faults: %d\n", page_faults);
}

// Optimal Page Replacement Algorithm
void optimal(int pages[], int n, int frame_count) {
    printf("\nOptimal Page Replacement:\n");
    int frames[frame_count];
    int page_faults = 0;
    for (int i = 0; i < frame_count; i++)
        frames[i] = -1;
    
    for (int i = 0; i < n; i++) {
        if (isPresent(frames, frame_count, pages[i]) == -1) {
            int replace_index = -1, farthest = i;
            for (int j = 0; j < frame_count; j++) {
                int next_use = INT_MAX;
                for (int k = i + 1; k < n; k++) {
                    if (frames[j] == pages[k]) {
                        next_use = k;
                        break;
                    }
                }
                if (next_use > farthest) {
                    farthest = next_use;
                    replace_index = j;
                }
            }
            frames[replace_index == -1 ? 0 : replace_index] = pages[i];
            page_faults++;
        }
        printf("Frames: ");
        for (int j = 0; j < frame_count; j++)
            printf("%d ", frames[j]);
        printf("\n");
    }
    printf("Total Page Faults: %d\n", page_faults);
}

int main() {
    int n, frame_count;
    printf("Enter number of pages: ");
    scanf("%d", &n);
    int pages[n];
    printf("Enter page reference sequence: ");
    for (int i = 0; i < n; i++)
        scanf("%d", &pages[i]);
    printf("Enter number of frames: ");
    scanf("%d", &frame_count);
    
    fifo(pages, n, frame_count);
    lru(pages, n, frame_count);
    optimal(pages, n, frame_count);
    
    return 0;
}
