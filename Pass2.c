#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 100

struct Symbol {
    char label[20];
    int address;
} symtab[MAX];

int sym_count = 0;

// Load symbol table into memory
void load_symtab() {
    FILE *fp = fopen("symtab.txt", "r");
    if (!fp) {
        printf("❌ Cannot open symbol table.\n");
        exit(1);
    }

    char temp[20];
    fscanf(fp, "%s %s", temp, temp);  // Skip header

    while (fscanf(fp, "%s %d", symtab[sym_count].label, &symtab[sym_count].address) != EOF) {
        sym_count++;
    }

    fclose(fp);
}

// Get symbol address
int get_sym_address(char label[]) {
    for (int i = 0; i < sym_count; i++) {
        if (strcmp(symtab[i].label, label) == 0)
            return symtab[i].address;
    }
    return -1;
}

// Get register code
int get_reg_code(char reg[]) {
    if (strcmp(reg, "AREG") == 0) return 1;
    if (strcmp(reg, "BREG") == 0) return 2;
    if (strcmp(reg, "CREG") == 0) return 3;
    if (strcmp(reg, "DREG") == 0) return 4;
    return 0;
}

int main() {
    FILE *fp_in = fopen("intermediate.txt", "r");
    FILE *fp_out = fopen("machinecode.txt", "w");

    if (!fp_in || !fp_out) {
        printf("❌ Cannot open input/output file.\n");
        return 1;
    }

    load_symtab();

    char line[100];
    int lc;

    while (fgets(line, sizeof(line), fp_in)) {
        if (line[0] == '(') {
            // e.g., (AD,01) (C,100) — ignore assembler directives
            continue;
        }

        sscanf(line, "%d", &lc);
        fprintf(fp_out, "%d ", lc);

        char *token = strtok(line, " ");
        token = strtok(NULL, " "); // skip LC token

        if (token && strstr(token, "(IS")) {
            int opcode;
            sscanf(token, "(IS,%d)", &opcode);
            fprintf(fp_out, "%02d ", opcode);

            // Get register
            token = strtok(NULL, " ");
            int reg = 0;
            if (token)
                sscanf(token, "(%[^)])", token), reg = get_reg_code(token);

            fprintf(fp_out, "%d ", reg);

            // Get operand (symbol or constant)
            token = strtok(NULL, " ");
            if (token[0] == '=') {
                int val;
                sscanf(token, "=''%d''", &val); // simplified literal
                fprintf(fp_out, "%03d\n", val);
            } else if (token[0] == 'C' || token[0] == '(') {
                int val;
                sscanf(token, "(C,%d)", &val);
                fprintf(fp_out, "%03d\n", val);
            } else {
                int addr = get_sym_address(token);
                fprintf(fp_out, "%03d\n", addr);
            }
        } else if (token && strstr(token, "(DL,02)")) {  // DC
            token = strtok(NULL, " ");
            int val;
            sscanf(token, "(C,%d)", &val);
            fprintf(fp_out, "00 0 %03d\n", val);
        } else if (token && strstr(token, "(DL,01)")) {  // DS
            fprintf(fp_out, "00 0 000\n");
        }
    }

    fclose(fp_in);
    fclose(fp_out);

    printf("✅ Pass-II completed. Machine code written to machinecode.txt\n");
    return 0;
}
