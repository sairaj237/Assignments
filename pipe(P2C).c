#include <stdio.h>
#include <unistd.h>
#include <string.h>


int main(){

int fd[2];
pid_t pid;
char mess[]="hello from parent";
char buffer[100];


if(pipe(fd)==-1){
    perror("pipe failed");
    return 1;
}

pid=fork();

if(pid<0){
    perror("fork failed");
    return 1;
}
else if(pid==0){
    close(fd[1]);
    read(fd[0],buffer,sizeof(buffer));
    printf("child received: %s\n",buffer);
    close(fd[0]);
}
else{
    close(fd[0]);
    write(fd[1],mess,strlen(mess)+1);
    close(fd[1]);
}


    return 0;
}