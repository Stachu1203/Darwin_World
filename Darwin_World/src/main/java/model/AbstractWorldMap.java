package model;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap, MoveValidator{
    public Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    protected  List<Vector2d> plants = new ArrayList<>();
    protected int dayCounter = 0;
    public List<Animal> deadAnimals = new ArrayList<>();
    public SimulationConfig config;

    public AbstractWorldMap(SimulationConfig config){
        this.config = config;
    }

    @Override
    public boolean place(Animal animal){
        animals.computeIfAbsent(animal.getPosition(), k -> new HashSet<>()).add(animal);
        return true;
    }

    @Override
    public void move(Animal animal, MapDirection direction){
        Vector2d oldPosition = animal.getPosition();

        Set<Animal> oldSet = animals.get(oldPosition);
        if (oldSet != null) {
            oldSet.remove(animal);
            if (oldSet.isEmpty()) {
                animals.remove(oldPosition);
            }
        }

        animal.move(this);
        animal.newDay(config.moveEnergy);
        Vector2d newPosition = animal.getPosition();


        animals.computeIfAbsent(newPosition, k -> new HashSet<>()).add(animal);

    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public Set<Animal> objectsAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return animals.get(position);
        }
        return null;
    }



    @Override
    public List<Animal> removeDeadAnimals() {
        for (Set<Animal> animalsAtPosition : animals.values()) {
            Iterator<Animal> iterator = animalsAtPosition.iterator();
            while (iterator.hasNext()) {
                Animal animal = iterator.next();
                if (animal.isDead()) {
                    iterator.remove();
                    deadAnimals.add(animal);
                }
            }
        }
        return deadAnimals;
    }

        @Override
        public void reproduceAnimals(){
            for (Set<Animal> animalsAtPosition : animals.values()) {
                if (animalsAtPosition.size() >= 2) {
                    List<Animal> animalList = new ArrayList<>(animalsAtPosition);
                    animalList.sort((a1, a2) -> Integer.compare(a2.getEnergy(), a1.getEnergy()));

                    Animal child = animalList.get(0).reproduce(animalList.get(1), this.dayCounter, config.childEnergyCost);
                    child.getGenotype().mutate(1,7);

                    place(child);

                }
            }
        }

    @Override
    public Vector2d validatePosition(Vector2d position, Animal animal) {
        int x = position.getX();
        int y = position.getY();

        int wrappedX = (x % config.width + config.width) % config.width;

        if (y < 0 || y >= config.height) {
            animal.setDirection(animal.getDirection().opposite());
            return new Vector2d(wrappedX, animal.getPosition().getY());
        }
        return new Vector2d(wrappedX, y);
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

                    winner.eat(config.plantEnergy);

                    plantIterator.remove();
                }
            }
        }
    }


    @Override
    public void moveAllAnimals() {
        List<Animal> allAnimals = new ArrayList<>();
        for (Set<Animal> setAtPosition : animals.values()) {
            allAnimals.addAll(setAtPosition);
        }

        for (Animal animal : allAnimals) {
            move(animal, animal.getDirection());
        }
    }




    public Animal findStrongestAnimal(Set<Animal> animalsAtPosition) {

            List<Animal> animalList = new ArrayList<>(animalsAtPosition);


            animalList.sort((a1, a2) -> {
                    return Integer.compare(a2.getEnergy(), a1.getEnergy());});

            return animalList.getFirst();
    }

    public void nextDay(){
        dayCounter+=1;
    }

    public int getCurrentDay(){
        return dayCounter;
    }

    public List<Animal> getAllAnimals(){
        List<Animal> allAnimals = new ArrayList<>();
        for(Set<Animal> animalsAtPosition: animals.values()){
            for(Animal animal : animalsAtPosition){
                if(!animal.isDead()) {
                    allAnimals.add(animal);
                }
            }
        }
        return allAnimals;
    }

    public List<Vector2d> getPlants(){
        return plants;
    }

    public List<Animal> getDeadAnimals(){
        return deadAnimals;
    }

    public int getWidth(){
        return config.width;
    }

    public int getHeight(){
        return config.height;
    }



}