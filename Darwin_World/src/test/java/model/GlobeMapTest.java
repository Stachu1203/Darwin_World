package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobeMapTest {
    private SimulationConfig config;
    private GlobeMap map;

    @BeforeEach
    void setUp() {
        config = new SimulationConfig();
        config.width = 10;
        config.height = 10;
        map = new GlobeMap(config);
    }

    @Test
    void shouldWrapAroundHorizontalBorders() {
        Animal animal = new Animal(new Vector2d(9, 5), 50, 5, 0);
        Vector2d outsidePos = new Vector2d(10, 5);

        Vector2d validated = map.validatePosition(outsidePos, animal);

        assertEquals(0, validated.getX(), "Zwierzę powinno pojawić się po lewej stronie (x=0)");
        assertEquals(5, validated.getY());
    }

    @Test
    void shouldBounceFromPoleAndStayOnMap() {
        Animal animal = new Animal(new Vector2d(5, 9), 50, 5, 0);
        animal.setDirection(MapDirection.NORTH);
        Vector2d outsideNorth = new Vector2d(5, 10);

        Vector2d validated = map.validatePosition(outsideNorth, animal);

        assertEquals(9, validated.getY(), "Zwierzę nie powinno wyjść poza y=9");
        assertEquals(MapDirection.SOUTH, animal.getDirection(), "Zwierzę powinno zmienić kierunek na przeciwny");
    }

    @Test
    void strongestAnimalShouldEatPlant() {

        Vector2d pos = new Vector2d(2, 2);
        map.getPlants().add(pos);


        Animal stronger = new Animal(pos, 100, 5, 0); // 100 energii
        Animal weaker = new Animal(pos, 50, 5, 0);   // 50 energii
        map.place(stronger);
        map.place(weaker);


        map.eatPlants();


        assertEquals(105, stronger.getEnergy(), "Silniejszy powinien zjeść roślinę (+5)");
        assertEquals(50, weaker.getEnergy(), "Słabszy nie powinien nic zjeść");
        assertTrue(map.getPlants().isEmpty(), "Roślina powinna zniknąć z mapy");
    }



    @Test
    void shouldApplyLonelinessPenaltyWhenNoNeighbors() {

        Vector2d pos = new Vector2d(5, 5);
        Animal loneAnimal = new Animal(pos, 50, 5, 0);
        map.place(loneAnimal);


        map.config.heatingRadius = 1;
        map.config.penaltyForLoneliness = 5;

        int penalty = map.getEnergyPenalty(pos);


        assertTrue(penalty >= 5, "Samotne zwierzę powinno otrzymać karę za samotność");
    }

    @Test
    void shouldApplyTemperaturePenaltyInWinter() {
        Vector2d pos = new Vector2d(5, 5);
        Animal animal = new Animal(pos, 50, 5, 0);
        map.place(animal);


        ClimateManager cm = map.getClimateManager();
        map.config.minTemperature = -10;
        map.config.temperatureImpact = 2;


        for(int i = 0; i < 20; i++) cm.nextDay();

        int penalty = map.getEnergyPenalty(pos);

        if (cm.getTemperature() < 0) {
            assertTrue(penalty > 0, "Zwierzę powinno otrzymać karę za ujemną temperaturę");
        }
    }



    @Test
    void shouldReproduceWhenTwoAnimalsOnSameField() {
        Vector2d pos = new Vector2d(3, 3);
        Animal parent1 = new Animal(pos, 100, 10, 0);
        Animal parent2 = new Animal(pos, 100, 10, 0);

        map.place(parent1);
        map.place(parent2);

        map.reproduceAnimals();


        assertEquals(3, map.getAllAnimals().size(), "Po rozmnożeniu na mapie powinny być 3 zwierzęta");
    }
    @Test
    void shouldCorrectlyRemoveDeadAnimalsAndTrackThem() {

        Vector2d pos = new Vector2d(5, 5);


        Animal survivor = new Animal(pos, 50, 5, 0);


        Animal deadAnimal1 = new Animal(pos, 0, 5, 0);


        Animal deadAnimal2 = new Animal(pos, -5, 5, 0);

        map.place(survivor);
        map.place(deadAnimal1);
        map.place(deadAnimal2);


        assertEquals(3, map.objectsAt(pos).size(), "Na początku powinny być 3 zwierzęta na polu");


        List<Animal> removed = map.removeDeadAnimals();


        assertEquals(2, removed.size(), "Metoda powinna zwrócić listę z 2 martwymi zwierzętami");


        assertEquals(1, map.objectsAt(pos).size(), "Na mapie powinno zostać tylko 1 żywe zwierzę");
        assertTrue(map.objectsAt(pos).contains(survivor), "Ocalałe zwierzę to powinien być 'survivor'");


        assertFalse(map.objectsAt(pos).contains(deadAnimal1));
        assertFalse(map.objectsAt(pos).contains(deadAnimal2));


        assertEquals(2, map.getDeadAnimals().size(), "Mapa powinna przechowywać 2 martwe zwierzęta w liście deadAnimals");
    }
}
