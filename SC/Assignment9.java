import java.util.*;

public class Assignment9 {

    private static int GENE_LENGTH;
    private static int POPULATION_SIZE;
    private static double MUTATION_RATE;

    Random rand = new Random();
    ArrayList<Individual> population = new ArrayList<>();

    class Individual {
        ArrayList<Integer> genes;
        double fitness;

        Individual() {
            genes = new ArrayList<>(Collections.nCopies(GENE_LENGTH, 0));
        }

        void display() {
            System.out.print("[ ");
            for (int g : genes) System.out.print(g + " ");
            System.out.println("] Fitness: " + fitness);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Population Size: ");
        POPULATION_SIZE = sc.nextInt();

        System.out.print("Enter Gene Length: ");
        GENE_LENGTH = sc.nextInt();

        System.out.print("Enter Mutation Rate (0-1): ");
        MUTATION_RATE = sc.nextDouble();

        Assignment9 ga = new Assignment9();
        ga.GAOperationsDemo();
        ga.demonstrateAll();
    }

    void GAOperationsDemo() {
        initializePopulation();
    }

    void initializePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual ind = new Individual();
            for (int j = 0; j < GENE_LENGTH; j++)
                ind.genes.set(j, rand.nextInt(2));
            calculateFitness(ind);
            population.add(ind);
        }
    }

    void calculateFitness(Individual ind) {
        double sum = 0;
        for (int x : ind.genes) sum += x;
        ind.fitness = sum;
    }

    // ----- SELECTION -----
    Individual rouletteWheelSelection() {
        double totalFit = population.stream()
                .mapToDouble(p -> p.fitness).sum();

        double r = rand.nextDouble() * totalFit;
        double cumulative = 0;

        for (Individual ind : population) {
            cumulative += ind.fitness;
            if (cumulative >= r) return copy(ind);
        }
        return copy(population.get(POPULATION_SIZE - 1));
    }

    Individual tournamentSelection(int size) {
        Individual best = population.get(rand.nextInt(POPULATION_SIZE));
        for (int i = 1; i < size; i++) {
            Individual contender = population.get(rand.nextInt(POPULATION_SIZE));
            if (contender.fitness > best.fitness) best = contender;
        }
        return copy(best);
    }

    Individual rankSelection() {
        ArrayList<Individual> sorted = new ArrayList<>(population);
        sorted.sort(Comparator.comparingDouble(a -> a.fitness));

        double totalRank = (POPULATION_SIZE * (POPULATION_SIZE + 1)) / 2.0;
        double r = rand.nextDouble() * totalRank;

        double cumulative = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            cumulative += (i + 1);
            if (cumulative >= r) return copy(sorted.get(i));
        }
        return copy(sorted.get(POPULATION_SIZE - 1));
    }

    // ----- CROSSOVERS -----
    class Pair {
        Individual c1, c2;
        Pair(Individual a, Individual b) { this.c1 = a; this.c2 = b; }
    }

    Pair singlePoint(Individual p1, Individual p2) {
        int point = 1 + rand.nextInt(GENE_LENGTH - 1);
        Individual c1 = new Individual();
        Individual c2 = new Individual();

        for (int i = 0; i < GENE_LENGTH; i++) {
            if (i < point) {
                c1.genes.set(i, p1.genes.get(i));
                c2.genes.set(i, p2.genes.get(i));
            } else {
                c1.genes.set(i, p2.genes.get(i));
                c2.genes.set(i, p1.genes.get(i));
            }
        }
        calculateFitness(c1);
        calculateFitness(c2);
        return new Pair(c1, c2);
    }

    Pair twoPoint(Individual p1, Individual p2) {
        int p = rand.nextInt(GENE_LENGTH);
        int q = rand.nextInt(GENE_LENGTH);
        if (p > q) { int t = p; p = q; q = t; }

        Individual c1 = new Individual();
        Individual c2 = new Individual();

        for (int i = 0; i < GENE_LENGTH; i++) {
            if (i >= p && i <= q) {
                c1.genes.set(i, p2.genes.get(i));
                c2.genes.set(i, p1.genes.get(i));
            } else {
                c1.genes.set(i, p1.genes.get(i));
                c2.genes.set(i, p2.genes.get(i));
            }
        }
        calculateFitness(c1);
        calculateFitness(c2);
        return new Pair(c1, c2);
    }

    Pair uniformCrossover(Individual p1, Individual p2) {
        Individual c1 = new Individual();
        Individual c2 = new Individual();

        for (int i = 0; i < GENE_LENGTH; i++) {
            if (rand.nextBoolean()) {
                c1.genes.set(i, p1.genes.get(i));
                c2.genes.set(i, p2.genes.get(i));
            } else {
                c1.genes.set(i, p2.genes.get(i));
                c2.genes.set(i, p1.genes.get(i));
            }
        }
        calculateFitness(c1);
        calculateFitness(c2);
        return new Pair(c1, c2);
    }

    // ----- MUTATION -----
    void mutation(Individual ind) {
        for (int i = 0; i < GENE_LENGTH; i++) {
            if (rand.nextDouble() < MUTATION_RATE) {
                ind.genes.set(i, 1 - ind.genes.get(i));
            }
        }
        calculateFitness(ind);
    }

    // Utility
    Individual copy(Individual src) {
        Individual n = new Individual();
        n.genes = new ArrayList<>(src.genes);
        n.fitness = src.fitness;
        return n;
    }

    // ----- DISPLAY RESULTS -----
    void demonstrateAll() {
        System.out.println("\nInitial Population:");
        population.forEach(Individual::display);

        System.out.println("\nRoulette Selection:");
        rouletteWheelSelection().display();

        System.out.println("\nTournament Selection:");
        tournamentSelection(3).display();

        System.out.println("\nRank Selection:");
        rankSelection().display();

        if (population.size() >= 6) {
            System.out.println("\nSingle Point Crossover:");
            Pair s = singlePoint(population.get(0), population.get(1));
            s.c1.display();
            s.c2.display();

            System.out.println("\nTwo Point Crossover:");
            Pair t = twoPoint(population.get(2), population.get(3));
            t.c1.display();
            t.c2.display();

            System.out.println("\nUniform Crossover:");
            Pair u = uniformCrossover(population.get(4), population.get(5));
            u.c1.display();
            u.c2.display();
        }

        System.out.println("\nMutation Example:");
        Individual m = copy(population.get(0));
        System.out.println("Before:");
        m.display();
        mutation(m);
        System.out.println("After:");
        m.display();
    }
}
