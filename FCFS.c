#include<stdio.h>

int main(){
    int n,i;
printf("enter the no of processes");
scanf("%d",&n);

int bt[n],wt[n],tat[n];
printf("enter burst times");
for(int i=0;i<n;i++){
    printf("P%d:",i+1);
    scanf("%d",&bt[i]);
}

wt[0]=0;
for(int i=0;i<n;i++){
    wt[i]=wt[i-1]+bt[i-1];
}
printf("\nProcess\tBT\tWT\tTAT\n");
for(i=0;i<n;i++){
    tat[i]=wt[i]+bt[i];
    printf("P%d\t%d\t%d\n",i+1,bt[i],wt[i],tat[i]);
}

return 0;
}


    
