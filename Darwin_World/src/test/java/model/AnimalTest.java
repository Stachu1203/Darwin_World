package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    private Animal animal;
    private final int START_ENERGY = 50;
    private final int GENOME_SIZE = 5;

    @BeforeEach
    void setUp() {
        animal = new Animal(new Vector2d(2, 2), START_ENERGY, GENOME_SIZE, 0);
    }

    @Test
    void shouldDecreaseEnergyAndIncreaseAgeEachDay() {
        int dailyCost = 5;
        animal.newDay(dailyCost);

        assertEquals(START_ENERGY - dailyCost, animal.getEnergy(), "Energia powinna spaść o koszt dzienny");
        assertEquals(1, animal.getAge(), "Wiek zwierzęcia powinien wzrosnąć o 1");
    }

    @Test
    void shouldCorrectlyIdentifyDeath() {
        assertFalse(animal.isDead(), "Zwierzę z energią startową nie powinno być martwe");

        animal.subtractEnergy(START_ENERGY);
        assertTrue(animal.isDead(), "Zwierzę z 0 energii powinno zostać uznane za martwe");

        animal.subtractEnergy(10);
        assertTrue(animal.isDead(), "Zwierzę z ujemną energią powinno być martwe");
    }

    @Test
    void shouldEatAndGainEnergy() {
        int plantEnergy = 15;
        animal.eat(plantEnergy);

        assertEquals(START_ENERGY + plantEnergy, animal.getEnergy(), "Energia powinna wzrosnąć po zjedzeniu rośliny");
    }

    @Test
    void shouldMoveAndAdvanceGenotype() {

        int currentGene = animal.getGenotype().getCurrentGene();
        MapDirection startDirection = animal.getDirection();

        MoveValidator validator = (pos, anim) -> pos;

        animal.move(validator);


        assertEquals(startDirection.rotate(currentGene), animal.getDirection(), "Zwierzę powinno się obrócić zgodnie z genem");
    }

    @Test
    void shouldCorrectlyReproduce() {
        Animal parent2 = new Animal(new Vector2d(2, 2), 60, GENOME_SIZE, 0);
        int childEnergyCost = 20;

        Animal child = animal.reproduce(parent2, 1, childEnergyCost);

        assertEquals(START_ENERGY - 10, animal.getEnergy());
        assertEquals(60 - 10, parent2.getEnergy());

        assertNotNull(child);
        assertEquals(childEnergyCost, child.getEnergy(), "Dziecko powinno otrzymać zsumowaną energię zabraną rodzicom");
        assertEquals(1, animal.getChildrenCount(), "Licznik dzieci rodzica 1 powinien wzrosnąć");
        assertEquals(1, parent2.getChildrenCount(), "Licznik dzieci rodzica 2 powinien wzrosnąć");
        assertEquals(animal.getPosition(), child.getPosition(), "Dziecko powinno urodzić się na pozycji rodziców");
    }
}