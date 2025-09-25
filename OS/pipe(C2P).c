#include <stdio.h>
#include <unistd.h>
#include <string.h>

int main() {
    int fd[2];
    pid_t pid;
    char message[] = "Hello from child!";
    char buffer[100];

    if (pipe(fd) == -1) {
        perror("pipe failed");
        return 1;
    }

    pid = fork();

    if (pid < 0) {
        perror("fork failed");
        return 1;
    }
    else if (pid == 0) {
        // Child process
        close(fd[0]); // Close read end
        write(fd[1], message, strlen(message) + 1);
        close(fd[1]);
    }
    else {
        // Parent process
        close(fd[1]); // Close write end
        read(fd[0], buffer, sizeof(buffer));
        printf("👨 Parent received: %s\n", buffer);
        close(fd[0]);
    }

    return 0;
}
