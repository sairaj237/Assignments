#include <stdio.h>
#include <pthread.h>

void* thread_function(void* arg){
    printf("hello ");
    return NULL;
}

int main(){
    pthread_t thread;

    if(pthread_create(&thread,NULL,thread_function,NULL)){
        fprintf(stderr,"error creating thread");
        return 1;
    }

    if(pthread_join(thread,NULL)){
        fprintf(stderr,"error joining");
        return 2;
    }
    printf("thread has finished");

    return 0;
}