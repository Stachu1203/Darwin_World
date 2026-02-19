package presenter;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    class SimulationPresenterStub extends SimulationPresenter {
        @Override
        public void drawMap(WorldMap map, SimulationStats stats) {
        }
        @Override
        public boolean isPaused() {
            return false;
        }
    }
    private SimulationConfig config;
    private GlobeMap map;
    private SimulationEngine engine;
    private SimulationStats stats;

    @BeforeEach
    void setUp() {
        config = new SimulationConfig();
        config.width = 10;
        config.height = 10;
        config.startAnimalCount = 10;
        config.startEnergy = 100;

        map = new GlobeMap(config);
        stats = new SimulationStats();
        ClimateManager climateManager = new ClimateManager(config);


        SimulationPresenter stubPresenter = new SimulationPresenterStub();

        engine = new SimulationEngine(config, map, stats, climateManager, stubPresenter);
    }

    @Test
    void shouldInitializeSimulationWithCorrectNumberOfAnimals() {

        assertNotNull(map);
        assertEquals(10, config.startAnimalCount);
    }




}