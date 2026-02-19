package model;

import java.util.*;

public class GlobeMap extends AbstractWorldMap{

    ClimateManager climateManager;


    public GlobeMap(SimulationConfig config) {
        super(config);
        this.climateManager = new ClimateManager(config);
    }


    public int getEnergyPenalty(Vector2d position){

        int penalty = 0;
        int square = 0;

        for (int j = position.getY() - config.heatingRadius; j <= position.getY() + config.heatingRadius; j++) {
            if (j < 0 || j >= config.height) continue;
            for (int i = position.getX() - config.heatingRadius; i <= position.getX() + config.heatingRadius; i++) {

                int wrappedX = (i % config.width + config.width) % config.width;
                Vector2d newPosition = new Vector2d(wrappedX,j);

                if (animals.containsKey(newPosition)){
                    square += animals.get(newPosition).size();
                }
            }
        }

        if (square-1< 2){
            penalty+=config.penaltyForLoneliness;
        }


        if(climateManager.getTemperature()<0){
            penalty += Math.abs(climateManager.getTemperature())*config.temperatureImpact;
        }

        return penalty;
    }

    public void growPlants() {
        Random random = new Random();
        int amountOfPlants = random.nextInt(config.maxAmountOfPlantsPerDay - config.minAmountOfPlantsPerDay + 1) + config.minAmountOfPlantsPerDay;
        int counter = 0;
        int maxAttempts = amountOfPlants*20;
        int attempts = 0;

        if(climateManager.getCurrentSeason() == Seasons.WINTER){
            amountOfPlants = (int)(config.winterPlantRatio*amountOfPlants);
        }

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
    public void eatPlants() {
        Iterator<Vector2d> plantIterator = plants.iterator();

        while (plantIterator.hasNext()) {
            Vector2d plantPosition = plantIterator.next();


            if (animals.containsKey(plantPosition)) {
                Set<Animal> animalsAtPosition = animals.get(plantPosition);

                if (!animalsAtPosition.isEmpty()) {
                    Animal winner = findStrongestAnimal(animalsAtPosition);
                    if(climateManager.getCurrentSeason() == Seasons.WINTER){
                        winner.eat(config.winterPlantEnergy);
                    }else{
                        winner.eat(config.plantEnergy);
                    }


                    plantIterator.remove();
                }
            }
        }
    }

    @Override
    public ClimateManager getClimateManager(){
        return this.climateManager;
    }




}
