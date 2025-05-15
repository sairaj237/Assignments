#include <stdio.h>
#include <pthread.h>

void* printMessage(void *arg){
    char* mess=(char*)arg;
    printf("%s\n",message);
    return NULL;
}


int main(){
    pthread_t t1,t2;
    pthread_create(&t1,NULL,printMessage,"thread 1:hello from thread")
    pthread_create(&t2, NULL, printMessage, "Thread 2: Running concurrently!");


    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    return 0;
}