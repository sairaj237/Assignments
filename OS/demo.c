#include<stdio.h>
#include<unistd.h>
#include<sys/types.h>



int main(){


pid_t x;
x = fork();
if (x==0){
printf("in child x=%ld\n",x);
printf("in child getpid()=%ld\n",(long)getpid());
}
else{
printf("in parent x=%ld\n",x);
printf("in parent getpid()=%ld\n",(long)getpid());
}


return 0;
}
