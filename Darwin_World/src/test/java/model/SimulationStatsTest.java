package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationStatsTest {
    private SimulationStats stats;
    private GlobeMap map;
    private SimulationConfig config;

    @BeforeEach
    void setUp() {
        config = new SimulationConfig();
        config.width = 10;
        config.height = 10;
        map = new GlobeMap(config);
        stats = new SimulationStats();
    }

    @Test
    void shouldCalculateBasicCountsCorrectly() {

        map.place(new Animal(new Vector2d(1, 1), 50, 5, 0));
        map.place(new Animal(new Vector2d(2, 2), 50, 5, 0));
        map.getPlants().add(new Vector2d(3, 3));
        map.getPlants().add(new Vector2d(4, 4));
        map.getPlants().add(new Vector2d(5, 5));

        stats.update(map, new ArrayList<>());

        assertEquals(2, stats.getAnimalCount(), "Liczba zwierząt powinna wynosić 2");
        assertEquals(3, stats.getPlantCount(), "Liczba roślin powinna wynosić 3");
    }

    @Test
    void shouldCalculateAveragesCorrectly() {

        Animal a1 = new Animal(new Vector2d(1, 1), 100, 5, 0);

        Animal a2 = new Animal(new Vector2d(1, 1), 50, 5, 0);

        map.place(a1);
        map.place(a2);

        a1.reproduce(a2, 1, 20);

        stats.update(map, new ArrayList<>());

        assertTrue(stats.getAverageEnergy() > 0);
        assertEquals(1.0, stats.getAverageChildrenCount(), "Średnia liczba dzieci powinna wynosić 1.0 (2 dzieci / 2 rodziców)");
    }

    @Test
    void shouldTrackAverageLifespanOfDeadAnimals() {
        List<Animal> deadAnimals = new ArrayList<>();

        Animal d1 = new Animal(new Vector2d(1, 1), 0, 5, 0);
        for(int i=0; i<10; i++) d1.newDay(0); // postarzamy o 10 dni
        deadAnimals.add(d1);


        Animal d2 = new Animal(new Vector2d(1, 1), 0, 5, 0);
        for(int i=0; i<20; i++) d2.newDay(0);
        deadAnimals.add(d2);

        stats.update(map, deadAnimals);

        assertEquals(15.0, stats.getAverageLifespan(), "Średnia długość życia powinna wynosić 15.0");
    }



    @Test
    void shouldHandleEmptyMapWithoutErrors() {
        assertDoesNotThrow(() -> stats.update(map, new ArrayList<>()));
        assertEquals(0, stats.getAnimalCount());
        assertEquals(0.0, stats.getAverageEnergy());
    }
}