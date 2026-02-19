package presenter;

import model.*;

import java.util.Random;
import java.util.Set;

public class SimulationEngine implements Runnable{
    private GlobeMap map;
    private final SimulationConfig config;
    private final SimulationStats stats;
    private ClimateManager climateManager;
    private boolean isRunning;
    private boolean start;
    private SimulationPresenter presenter;

    public SimulationEngine(SimulationConfig config, GlobeMap map, SimulationStats stats, ClimateManager climateManager,SimulationPresenter presenter){
        this.config=config;
        this.map=map;
        this.stats=stats;
        this.isRunning =  true;
        this.start = false;
        this.climateManager= climateManager;
        this.presenter = presenter;
    }

    public void run(){
        if(!start){
            placeAnimals();
            start = true;
        }
        while(isRunning){

            if (presenter.isPaused()) {
                try {
                    Thread.sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            dayStep();
            stats.update(this.map, map.getDeadAnimals());

            presenter.drawMap(map, stats);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void placeAnimals(){
        Random random = new Random();
        for(int i = 0; i< this.config.startAnimalCount; i++){
            int x = random.nextInt(config.width);
            int y = random.nextInt(config.height);
            Vector2d newPosition = new Vector2d(x, y);

            Animal newAnimal = new Animal(newPosition, config.startEnergy, config.genomSize, climateManager.getCurrentDay());

            map.place(newAnimal);
        }
    }

//    Usunięcie martwych zwierzaków z mapy.
//    Skręt i przemieszczenie każdego zwierzaka.
//    Konsumpcja roślin, na których pola weszły zwierzaki.
//    Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
//    Wzrastanie nowych roślin na wybranych polach mapy.

    private void dayStep(){
        map.removeDeadAnimals();
        map.moveAllAnimals();
        applyEnergyPenalties();
        map.eatPlants();
        map.reproduceAnimals();
        climateManager.nextDay();
        map.growPlants();
        map.nextDay();
    }

    private void applyEnergyPenalties() {
        for (Set<Animal> animalSet : map.animals.values()) {
            for (Animal animal : animalSet) {
                int penalty = map.getEnergyPenalty(animal.getPosition());
                animal.subtractEnergy(penalty);
            }
        }
    }
}
