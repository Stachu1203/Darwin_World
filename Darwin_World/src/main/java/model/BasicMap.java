package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BasicMap extends AbstractWorldMap{

    public BasicMap(SimulationConfig config){
        super(config);
    }




    public void growPlants() {
        Random random = new Random();
        int amountOfPlants = random.nextInt(config.maxAmountOfPlantsPerDay - config.minAmountOfPlantsPerDay + 1) + config.minAmountOfPlantsPerDay;
        int counter = 0;
        int maxAttempts = amountOfPlants*20;
        int attempts = 0;

        while (counter < amountOfPlants && attempts<maxAttempts) {
            int xPosition = random.nextInt(config.width );
            int yPosition = random.nextInt(config.height );
            Vector2d newPosition = new Vector2d(xPosition, yPosition);
            int chances = random.nextInt(100);

            attempts+=1;

            if (plants.contains(newPosition)) continue;

            if (newPosition.getY() >= (int) (0.4 * config.height) && newPosition.getY() <= (int) (0.6 * config.height)){
                if (chances >= 0 && chances <= 79) {
                        plants.add(newPosition);
                        counter++;
                }
            }else {
                if (chances >= 80 && chances <= 99) {
                        plants.add(newPosition);
                        counter++;
                }
            }
        }
    }

    @Override
    public ClimateManager getClimateManager(){
        return null;
    }


}
