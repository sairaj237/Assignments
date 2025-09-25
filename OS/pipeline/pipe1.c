
#include<stdio.h>
#include<unistd.h>
#include<sys/types.h>

int main()
{
    pid_t x;
    int fd[2];
    int a[10]={1,2,3,4,5,6,7,8,9,10};
    int e;
    pipe(fd);
    x = fork();
    if( x==0 )
    {
        close(fd[1]);
        while((read(fd[0],&e, sizeof(e)))>0)
        {
            printf("e=%d\n", e);
        }
        close(fd[0]);
    }
    else
    {
        close(fd[0]);
        write(fd[1], a, sizeof(a));
        close(fd[1]);
    }
    return 0;
}
