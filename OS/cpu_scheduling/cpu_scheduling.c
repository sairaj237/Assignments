#include <stdio.h>
#include <limits.h>
#include <stdbool.h>

#define MAX_PROCESSES 10

// Structure for a process
struct Process {
    int pid, arrival_time, burst_time, priority;
};

// Function to swap two processes
void swap(struct Process *a, struct Process *b) {
    struct Process temp = *a;
    *a = *b;
    *b = temp;
}

// FCFS Scheduling
void fcfs(struct Process processes[], int n) {
    printf("\nFCFS Scheduling:\n");
    int wait_time = 0, turnaround_time = 0, completion_time = 0;
    
    for (int i = 0; i < n; i++) {
        if (completion_time < processes[i].arrival_time)
            completion_time = processes[i].arrival_time;
        completion_time += processes[i].burst_time;
        turnaround_time += (completion_time - processes[i].arrival_time);
        wait_time += (completion_time - processes[i].arrival_time - processes[i].burst_time);
        printf("P%d: Completion Time = %d, Turnaround Time = %d, Waiting Time = %d\n", 
                processes[i].pid, completion_time, completion_time - processes[i].arrival_time, completion_time - processes[i].arrival_time - processes[i].burst_time);
    }
}

// SJF Preemptive Scheduling
void sjf_preemptive(struct Process processes[], int n) {
    printf("\nSJF Preemptive Scheduling:\n");
    int remaining_time[MAX_PROCESSES], complete = 0, current_time = 0, shortest = -1, min_time = INT_MAX;
    bool visited[MAX_PROCESSES] = {false};
    
    for (int i = 0; i < n; i++)
        remaining_time[i] = processes[i].burst_time;
    
    while (complete != n) {
        shortest = -1;
        min_time = INT_MAX;
        for (int i = 0; i < n; i++) {
            if (processes[i].arrival_time <= current_time && remaining_time[i] > 0 && remaining_time[i] < min_time) {
                min_time = remaining_time[i];
                shortest = i;
            }
        }
        
        if (shortest == -1) {
            current_time++;
            continue;
        }
        remaining_time[shortest]--;
        if (remaining_time[shortest] == 0) {
            complete++;
            int finish_time = current_time + 1;
            printf("P%d: Completion Time = %d\n", processes[shortest].pid, finish_time);
        }
        current_time++;
    }
}

// Priority Scheduling (Non-Preemptive)
void priority_scheduling(struct Process processes[], int n) {
    printf("\nPriority Scheduling (Non-Preemptive):\n");
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (processes[j].priority < processes[i].priority)
                swap(&processes[i], &processes[j]);
        }
    }
    fcfs(processes, n);
}

// Round Robin Scheduling
void round_robin(struct Process processes[], int n, int quantum) {
    printf("\nRound Robin Scheduling:\n");
    int remaining_time[MAX_PROCESSES];
    for (int i = 0; i < n; i++)
        remaining_time[i] = processes[i].burst_time;
    
    int current_time = 0, complete = 0;
    while (complete < n) {
        for (int i = 0; i < n; i++) {
            if (remaining_time[i] > 0) {
                if (remaining_time[i] > quantum) {
                    current_time += quantum;
                    remaining_time[i] -= quantum;
                } else {
                    current_time += remaining_time[i];
                    remaining_time[i] = 0;
                    complete++;
                    printf("P%d: Completion Time = %d\n", processes[i].pid, current_time);
                }
            }
        }
    }
}

int main() {
    int n, quantum;
    struct Process processes[MAX_PROCESSES];
    
    printf("Enter number of processes: ");
    scanf("%d", &n);
    
    printf("Enter Time Quantum for Round Robin: ");
    scanf("%d", &quantum);
    
    printf("Enter process details (PID, Arrival Time, Burst Time, Priority):\n");
    for (int i = 0; i < n; i++) {
        scanf("%d %d %d %d", &processes[i].pid, &processes[i].arrival_time, &processes[i].burst_time, &processes[i].priority);
    }
    
    // Sorting based on arrival time for FCFS
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (processes[j].arrival_time > processes[j + 1].arrival_time)
                swap(&processes[j], &processes[j + 1]);
        }
    }
    
    fcfs(processes, n);
    sjf_preemptive(processes, n);
    priority_scheduling(processes, n);
    round_robin(processes, n, quantum);
    
    return 0;
}
