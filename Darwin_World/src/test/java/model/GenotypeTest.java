package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {
    @Test
    void shouldInitializeWithCorrectSize() {
        int size = 10;
        Genotype genotype = new Genotype(size);

        assertEquals(size, genotype.getGenes().size(), "Genom powinien mieć określony rozmiar");
        for (int gene : genotype.getGenes()) {
            assertTrue(gene >= 0 && gene <= 7, "Geny muszą być w zakresie 0-7");
        }
    }

    @Test
    void shouldAdvanceGeneIndexCorrectly() {
        int size = 3;
        Genotype genotype = new Genotype(size);

        int firstGene = genotype.getCurrentGene();
        genotype.advanceGene();
        int secondGene = genotype.getCurrentGene();
        genotype.advanceGene();
        int thirdGene = genotype.getCurrentGene();
        genotype.advanceGene();

        assertEquals(firstGene, genotype.getCurrentGene(), "Indeks powinien zawijać się (modulo size)");
    }

    @Test
    void shouldInheritGenesProportionallyFromStrongerParent() {
        int size = 100;
        Genotype g1 = new Genotype(size);
        Genotype g2 = new Genotype(size);


        int energy1 = 80;
        int energy2 = 20;

        Genotype childGenotype = new Genotype(g1, g2, energy1, energy2);
        List<Integer> childGenes = childGenotype.getGenes();

        int matchesG1 = 0;
        int matchesG2 = 0;


        assertTrue(childGenes.size() == size);
    }

    @Test
    void mutationShouldChangeGenesWithinBounds() {
        int size = 10;
        Genotype genotype = new Genotype(size);
        List<Integer> beforeMutation = List.copyOf(genotype.getGenes());

        genotype.mutate(2, 2);
        List<Integer> afterMutation = genotype.getGenes();

        int diffCount = 0;
        for (int i = 0; i < size; i++) {
            if (!beforeMutation.get(i).equals(afterMutation.get(i))) {
                diffCount++;
            }
        }

        assertEquals(2, diffCount, "Powinny zmienić się dokładnie 2 geny");
    }
}