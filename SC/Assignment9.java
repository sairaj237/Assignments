import java.util.*;

public class Assignment9 {

    private static final int GENE_LENGTH = 8;
    private static final int POPULATION_SIZE = 6;

    Random rand = new Random();

    class Individual {
        ArrayList<Integer> genes;
        double fitness;

        Individual(int length) {
            genes = new ArrayList<>(Collections.nCopies(length, 0));
            fitness = 0.0;
        }

        void display() {
            System.out.print("["   );
            for (int i = 0; i < genes.size(); i++) {
                System.out.print(genes.get(i));
                if (i < genes.size() - 1) System.out.print(" ");
            }
            System.out.print("] Fitness: " + fitness);
        }
    }

    ArrayList<Individual> population = new ArrayList<>();

    void GAOperationsDemo() {
        initializePopulation();
    }

    void initializePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual ind = new Individual(GENE_LENGTH);
            for (int j = 0; j < GENE_LENGTH; j++) {
                ind.genes.set(j, rand.nextInt(2));
            }
            calculateFitness(ind);
            population.add(ind);
        }
    }

    void calculateFitness(Individual ind) {
        double sum = 0;
        for (int x : ind.genes) sum += x;
        ind.fitness = sum;
    }

    Individual rouletteWheelSelection() {
        double totalFit = population.stream().mapToDouble(p -> p.fitness).sum();
        double r = rand.nextDouble() * totalFit;
        double cumulative = 0;

        for (Individual ind : population) {
            cumulative += ind.fitness;
            if (cumulative >= r) return copy(ind);
        }
        return copy(population.get(population.size() - 1));
    }

    Individual tournamentSelection(int size) {
        Individual best = copy(population.get(rand.nextInt(POPULATION_SIZE)));

        for (int i = 1; i < size; i++) {
            Individual contender = population.get(rand.nextInt(POPULATION_SIZE));
            if (contender.fitness > best.fitness) best = copy(contender);
        }
        return best;
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

    Pair<Individual, Individual> singlePoint(Individual p1, Individual p2) {
        int point = 1 + rand.nextInt(GENE_LENGTH - 1);

        Individual c1 = new Individual(GENE_LENGTH);
        Individual c2 = new Individual(GENE_LENGTH);

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
        return new Pair<>(c1, c2);
    }

    // rest unchanged...

    Individual copy(Individual src) {
        Individual n = new Individual(GENE_LENGTH);
        n.genes = new ArrayList<>(src.genes);
        n.fitness = src.fitness;
        return n;
    }

    class Pair<A, B> {
        A a;
        B b;
        Pair(A a, B b) { this.a = a; this.b = b; }
    }

    public static void main(String[] args) {
        Assignment9 ga = new Assignment9();
        ga.GAOperationsDemo();
        ga.demonstrateAll();
    }

    // Added method to fix compile error
    void demonstrateAll() {
        System.out.println("Demonstrating all GA operations...");
        // You can add demonstration logic here, e.g.:
        System.out.println("Initial Population:");
        for (Individual ind : population) {
            ind.display();
            System.out.println();
        }
        // Add more demonstration code as needed
    }
}
