package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClimateManagerTest {


    private SimulationConfig config;
    private ClimateManager manager;

        @BeforeEach
        void setUp() {
            config = new SimulationConfig();
            config.seasonDuration = 3;       // Krótki sezon dla łatwiejszych testów
            config.startingTemperature = 10;
            config.minTemperature = 8;
            manager = new ClimateManager(config);
        }

        @Test
        void shouldIncrementDayCounterAndResetOnSeasonChange() {
            assertEquals(0, manager.getCurrentDay(), "Dzień początkowy powinien wynosić 0");

            manager.nextDay();
            assertEquals(1, manager.getCurrentDay());

            manager.nextDay();
            assertEquals(2, manager.getCurrentDay());


            manager.nextDay();
            assertEquals(0, manager.getCurrentDay(), "Licznik dni powinien się zresetować po zmianie sezonu");
        }

        @Test
        void shouldCycleBetweenSummerAndWinter() {
            assertEquals(Seasons.SUMMER, manager.getCurrentSeason());


            for (int i = 0; i < 3; i++) manager.nextDay();
            assertEquals(Seasons.WINTER, manager.getCurrentSeason(), "Po lecie powinna nastąpić zima");


            for (int i = 0; i < 3; i++) manager.nextDay();
            assertEquals(Seasons.SUMMER, manager.getCurrentSeason(), "Po zimie powinno nastąpić lato");
        }
}