package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Genotype {
    private final List<Integer> genes;
    private int currentGeneIndex;
    private final Random random = new Random();

    public Genotype(int size) {
        this.genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(random.nextInt(8));
        }
        this.currentGeneIndex = random.nextInt(size);
    }

    public Genotype(Genotype parent1, Genotype parent2, int energy1, int energy2) {
        this.genes = new ArrayList<>();

        int totalEnergy = Math.max(1, energy1 + energy2);
        int size = parent1.genes.size();

        int genesFromStronger = (int) Math.round(size * ((double) Math.max(energy1, energy2) / totalEnergy));

        int splitPoint = Math.max(1, Math.min(size - 1, genesFromStronger));

        Genotype stronger = energy1 >= energy2 ? parent1 : parent2;
        Genotype weaker = energy1 >= energy2 ? parent2 : parent1;

        boolean leftSideStronger = random.nextBoolean();

        if (leftSideStronger) {
            genes.addAll(stronger.genes.subList(0, splitPoint));
            genes.addAll(weaker.genes.subList(splitPoint, size));
        } else {
            genes.addAll(weaker.genes.subList(0, size - splitPoint));
            genes.addAll(stronger.genes.subList(size - splitPoint, size));
        }

        this.currentGeneIndex = random.nextInt(size);
    }

    public int getCurrentGene() {
        return genes.get(currentGeneIndex);
    }

    public void advanceGene() {
        currentGeneIndex = (currentGeneIndex + 1) % genes.size();
    }

    public void mutate(int minMutations, int maxMutations) {
        int mutationCount = random.nextInt(maxMutations - minMutations + 1) + minMutations;
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genes.size(); i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);

        for (int i = 0; i < mutationCount; i++) {
            int geneIndex = indexes.get(i);
            int newGene;
            do {
                newGene = random.nextInt(8);
            } while (newGene == genes.get(geneIndex));
            genes.set(geneIndex, newGene);
        }
    }

    public List<Integer> getGenes() {
        return new ArrayList<>(genes);
    }
}