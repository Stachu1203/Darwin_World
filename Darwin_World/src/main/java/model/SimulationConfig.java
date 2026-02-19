package model;

public class SimulationConfig {
    public int width;
    public int height;
    public int startEnergy = 50;
    public int genomSize = 7;
    public int childEnergyCost = 2;
    public int moveEnergy = 1;
    public int plantEnergy = 5;
    public int winterPlantEnergy = 2;
    public int startAnimalCount = 30;
    public int seasonDuration = 15;
    public int minTemperature = -7;
    public int startingTemperature = 0;
    public int temperatureImpact = 2;
    public int minAmountOfPlantsPerDay = 5;
    public int maxAmountOfPlantsPerDay = 10;
    public float winterPlantRatio = 0.3F;
    public int heatingRadius = 5;
    public int penaltyForLoneliness = 2;
}
