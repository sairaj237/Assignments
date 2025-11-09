import java.util.*;

class GeneticAlgorithm {
    int POP_SIZE;
    int GENERATIONS;
    double MUTATION_RATE;
    double CROSSOVER_RATE;
    static final int CHROM_LENGTH = 20;
    static final double LOWER = -10;
    static final double UPPER = 10;

    Random rand = new Random();

    class Individual {
        double[] genes = new double[CHROM_LENGTH];
        double fitness;

        Individual() {
            for (int i = 0; i < CHROM_LENGTH; i++)
                genes[i] = rand.nextDouble();
        }
    }

    ArrayList<Individual> population = new ArrayList<>();
    Individual best;

    GeneticAlgorithm(int pop, int gen, double mut, double cross) {
        this.POP_SIZE = pop;
        this.GENERATIONS = gen;
        this.MUTATION_RATE = mut;
        this.CROSSOVER_RATE = cross;
        initPopulation();
    }

    double decode(double[] g, int s, int e) {
        double sum = 0;
        for (int i = s; i < e; i++) sum += g[i];
        double norm = sum / (e - s);
        return LOWER + norm * (UPPER - LOWER);
    }

    double fitness(double[] g) {
        double x = decode(g, 0, CHROM_LENGTH / 2);
        double y = decode(g, CHROM_LENGTH / 2, CHROM_LENGTH);
        return x*x + y*y;
    }

    void initPopulation() {
        for (int i = 0; i < POP_SIZE; i++) {
            Individual ind = new Individual();
            ind.fitness = fitness(ind.genes);
            population.add(ind);
        }
        best = population.get(0);
        updateBest();
    }

    void updateBest() {
        for (Individual ind : population)
            if (ind.fitness < best.fitness)
                best = ind;
    }

    Individual selectParent() {
        Individual a = population.get(rand.nextInt(POP_SIZE));
        Individual b = population.get(rand.nextInt(POP_SIZE));
        return a.fitness < b.fitness ? a : b;
    }

    Individual[] crossover(Individual p1, Individual p2) {
        Individual c1 = new Individual();
        Individual c2 = new Individual();

        if (rand.nextDouble() < CROSSOVER_RATE) {
            int point = rand.nextInt(CHROM_LENGTH - 1) + 1;
            for (int i = 0; i < CHROM_LENGTH; i++) {
                if (i < point) {
                    c1.genes[i] = p1.genes[i];
                    c2.genes[i] = p2.genes[i];
                } else {
                    c1.genes[i] = p2.genes[i];
                    c2.genes[i] = p1.genes[i];
                }
            }
        } else {
            c1.genes = p1.genes.clone();
            c2.genes = p2.genes.clone();
        }
        return new Individual[]{c1, c2};
    }

    void mutate(Individual ind) {
        for (int i = 0; i < CHROM_LENGTH; i++)
            if (rand.nextDouble() < MUTATION_RATE)
                ind.genes[i] = rand.nextDouble();
    }

    void evolve() {
        System.out.println("\nRunning GA for f(x,y) = x^2 + y^2 ...\n");

        for (int g = 0; g < GENERATIONS; g++) {
            ArrayList<Individual> newPop = new ArrayList<>();
            newPop.add(best);

            while (newPop.size() < POP_SIZE) {
                Individual p1 = selectParent();
                Individual p2 = selectParent();
                Individual[] kids = crossover(p1, p2);

                mutate(kids[0]);
                mutate(kids[1]);

                kids[0].fitness = fitness(kids[0].genes);
                kids[1].fitness = fitness(kids[1].genes);

                newPop.add(kids[0]);
                if (newPop.size() < POP_SIZE) newPop.add(kids[1]);
            }

            population = newPop;
            updateBest();

            if ((g+1) % 10 == 0)
                System.out.println("Generation " + (g+1) + " Best = " + best.fitness);
        }

        double bestX = decode(best.genes, 0, CHROM_LENGTH/2);
        double bestY = decode(best.genes, CHROM_LENGTH/2, CHROM_LENGTH);

        System.out.println("\nFinal Best Solution:");
        System.out.println("x = " + bestX);
        System.out.println("y = " + bestY);
        System.out.println("f(x,y) = " + best.fitness);
    }
}

public class Assignment8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter population size: ");
        int pop = sc.nextInt();

        System.out.print("Enter number of generations: ");
        int gen = sc.nextInt();

        System.out.print("Enter mutation rate (0-1): ");
        double mut = sc.nextDouble();

        System.out.print("Enter crossover rate (0-1): ");
        double cross = sc.nextDouble();

        GeneticAlgorithm ga = new GeneticAlgorithm(pop, gen, mut, cross);
        ga.evolve();
    }
}
