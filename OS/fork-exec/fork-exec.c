#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    pid_t pid = fork(); // Create a child process

    if (pid < 0) {
        perror("Fork failed");
        exit(1);
    }
    else if (pid == 0) {
        // Child process
        printf("Child process (PID: %d) executing ls command...\n", getpid());
        execlp("ls", "ls", "-l", NULL); // Execute ls -l command
        perror("Exec failed"); // If exec fails
        exit(1);
    }
    else {
        // Parent process
        wait(NULL); // Wait for child to complete
        printf("Parent process (PID: %d) finished execution.\n", getpid());
    }
    return 0;
}

