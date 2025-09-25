#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    char str1[10] = "Hello";
    char str2[10];
    pid_t pid;
    int fd[2];
    pipe(fd);

    pid = fork();

    if (pid == 0) {
        // Child process
        close(fd[0]); // Close the read end
        write(fd[1], str1, strlen(str1) + 1);
        close(fd[1]);
    } else if (pid > 0) {
        // Parent process
        close(fd[1]); // Close the write end
        read(fd[0], str2, strlen(str1) + 1);
        close(fd[0]);
        printf("msg = %s\n", str2);
    } else {
        // Fork failed
        perror("fork");
        return 1;
    }

    return 0;
}