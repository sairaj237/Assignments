#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 50

struct Symbol {
    char label[20];
    int address;
} symtab[MAX];

int sym_count = 0;

// Function prototypes
int search_symbol(char label[]);
void add_symbol(char label[], int addr);
void write_intermediate(FILE *fp, int lc, char *line);

int main() {
    FILE *fp_in = fopen("input.asm", "r");
    FILE *fp_ic = fopen("intermediate.txt", "w");

    if (!fp_in || !fp_ic) {
        printf("❌ Error opening files.\n");
        return 1;
    }

    char line[100], label[20], opcode[20], operand1[20], operand2[20];
    int lc = 0;

    while (fgets(line, sizeof(line), fp_in)) {
        int tokens = sscanf(line, "%s %s %s %s", label, opcode, operand1, operand2);

        if (strcmp(label, "START") == 0 || strcmp(opcode, "START") == 0) {
            int start_addr;
            if (tokens == 2) sscanf(opcode, "%d", &start_addr);
            else sscanf(operand1, "%d", &start_addr);
            lc = start_addr;
            fprintf(fp_ic, "(AD,01) (C,%d)\n", lc);
            continue;
        }

        // If line has label, add to symbol table
        if (tokens == 4 || tokens == 3) {
            if (search_symbol(label) == -1)
                add_symbol(label, lc);
        }

        // Intermediate code generation (simplified)
        if (strcmp(opcode, "MOVER") == 0)
            fprintf(fp_ic, "%d) (IS,04) (%s) %s\n", lc, operand1, operand2);
        else if (strcmp(opcode, "MOVEM") == 0)
            fprintf(fp_ic, "%d) (IS,05) (%s) %s\n", lc, operand1, operand2);
        else if (strcmp(opcode, "ADD") == 0)
            fprintf(fp_ic, "%d) (IS,01) (%s) %s\n", lc, operand1, operand2);
        else if (strcmp(opcode, "DS") == 0) {
            fprintf(fp_ic, "%d) (DL,01) (C,%s)\n", lc, operand1);
            if (search_symbol(label) == -1)
                add_symbol(label, lc);
        }
        else if (strcmp(opcode, "DC") == 0) {
            fprintf(fp_ic, "%d) (DL,02) (C,%s)\n", lc, operand1);
            if (search_symbol(label) == -1)
                add_symbol(label, lc);
        }
        else if (strcmp(opcode, "END") == 0) {
            fprintf(fp_ic, "(AD,02)\n");
            break;
        }

        lc++;
    }

    fclose(fp_in);
    fclose(fp_ic);

    // Write symbol table
    FILE *fp_sym = fopen("symtab.txt", "w");
    fprintf(fp_sym, "Symbol\tAddress\n");
    for (int i = 0; i < sym_count; i++)
        fprintf(fp_sym, "%s\t%d\n", symtab[i].label, symtab[i].address);
    fclose(fp_sym);

    printf("✅ Pass-I completed. Intermediate and symbol table generated.\n");
    return 0;
}

// Search symbol by label
int search_symbol(char label[]) {
    for (int i = 0; i < sym_count; i++)
        if (strcmp(symtab[i].label, label) == 0)
            return i;
    return -1;
}

// Add symbol to symbol table
void add_symbol(char label[], int addr) {
    strcpy(symtab[sym_count].label, label);
    symtab[sym_count].address = addr;
    sym_count++;
}


// START 100
// MOVER AREG, ='5'
// MOVEM AREG, X
// X DS 1
// END
