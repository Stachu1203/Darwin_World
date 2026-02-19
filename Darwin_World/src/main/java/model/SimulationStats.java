package model;

import java.util.*;

public class SimulationStats {



    private int animalCount;
    private int plantCount;
    private int freeFieldsCount;
    private double averageEnergy;
    private double averageChildrenCount;
    private double averageLifespan;
    private String mostPopularGenotype;
    private SimulationConfig config;

    private int deadAnimalsCount = 0;
    private long totalLifespanSum = 0;




    public void update(WorldMap map, List<Animal> newlyDeadAnimals) {
        List<Animal> allAnimals = map.getAllAnimals();

        this.animalCount = allAnimals.size();
        this.plantCount = map.getPlants().size();

        if (animalCount > 0) {
            this.averageEnergy = allAnimals.stream()
                    .mapToInt(Animal::getEnergy)
                    .average().orElse(0.0);

            this.averageChildrenCount = allAnimals.stream()
                    .mapToInt(Animal::getChildrenCount)
                    .average().orElse(0.0);
        } else {
            this.averageEnergy = 0.0;
            this.averageChildrenCount = 0.0;
        }

        Set<Vector2d> occupiedPositions = new HashSet<>();
        occupiedPositions.addAll(map.getPlants());
        for (Animal a : allAnimals) {
            occupiedPositions.add(a.getPosition());
        }

        int totalFields = map.getWidth() * map.getHeight();
        this.freeFieldsCount = totalFields - occupiedPositions.size();

        if (newlyDeadAnimals != null && !newlyDeadAnimals.isEmpty()) {
            for (Animal dead : newlyDeadAnimals) {
                deadAnimalsCount++;
                totalLifespanSum += dead.getAge();
            }
        }

        if (deadAnimalsCount > 0) {
            this.averageLifespan = (double) totalLifespanSum / deadAnimalsCount;
        } else {
            this.averageLifespan = 0.0;
        }

        this.mostPopularGenotype = findMostPopularGenotype(allAnimals);
    }

    private String findMostPopularGenotype(List<Animal> animals) {
        if (animals.isEmpty()) return "-";

        Map<String, Integer> counts = new HashMap<>();
        for (Animal animal : animals) {
            String g = animal.getGenotype().getGenes().toString();
            counts.put(g, counts.getOrDefault(g, 0) + 1);
        }

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");
    }

    public int getAnimalCount() { return animalCount; }
    public int getPlantCount() { return plantCount; }
    public int getFreeFieldsCount() { return freeFieldsCount; }
    public double getAverageEnergy() { return averageEnergy; }
    public double getAverageChildrenCount() { return averageChildrenCount; }
    public double getAverageLifespan() { return averageLifespan; }
    public String getMostPopularGenotype() { return mostPopularGenotype; }
}