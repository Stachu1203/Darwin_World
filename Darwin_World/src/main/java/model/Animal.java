package model;
import java.util.Random;

public class Animal implements Comparable<Animal> {

    private MapDirection direction;
    private Vector2d position;
    private final Genotype genotype;
    private int energy;
    private int age = 0;
    private int childrenCount = 0;
    private final int birthDay;
    private int deathDay = -1;
    private final int id;

    public Animal(Vector2d position, int startEnergy, int genomeSize, int currentDay) {
        this.position = position;
        this.energy = startEnergy;
        this.birthDay = currentDay;
        this.genotype = new Genotype(genomeSize);
        this.direction = MapDirection.getRandom();
        this.id = new Random().nextInt();
    }

    public Animal(Animal parent1, Animal parent2, int currentDay, int childEnergy) {
        this.position = parent1.position;
        this.birthDay = currentDay;
        this.energy = childEnergy;
        this.direction = MapDirection.getRandom();
        this.id = new Random().nextInt();
        this.genotype = new Genotype(parent1.genotype, parent2.genotype, parent1.energy, parent2.energy);
        parent1.childrenCount++;
        parent2.childrenCount++;
    }

    public void move(MoveValidator validator) {
        int rotation = genotype.getCurrentGene();
        this.direction = this.direction.rotate(rotation);

        Vector2d desiredPosition = this.position.add(this.direction.toUnitVector());
        this.position = validator.validatePosition(desiredPosition, this);

        genotype.advanceGene();
    }

    public void eat(int plantEnergy) {
        this.energy += plantEnergy;
    }

    public void subtractEnergy(int amount) {
        this.energy -= amount;
    }

    public void newDay(int dailyCost) {
        this.energy -= dailyCost;
        this.age++;
    }

    public boolean isDead() {
        return this.energy <= 0;
    }

    public void markAsDead(int day) {
        this.deathDay = day;
    }

    public boolean canReproduce(int requiredEnergy) {
        return this.energy >= requiredEnergy;
    }

    public Animal reproduce(Animal partner, int currentDay, int childEnergyCost) {

        int energyFromParent1 = childEnergyCost / 2;
        int energyFromParent2 = childEnergyCost / 2;


        this.subtractEnergy(energyFromParent1);
        partner.subtractEnergy(energyFromParent2);


        return new Animal(this, partner, currentDay, energyFromParent1 + energyFromParent2);
    }
    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    @Override
    public int compareTo(Animal other) {
        if (this.energy != other.energy) {
            return other.energy - this.energy;
        }
        if (this.age != other.age) {
            return other.age - this.age;
        }
        if (this.childrenCount != other.childrenCount) {
            return other.childrenCount - this.childrenCount;
        }
        return this.id - other.id;
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public int getDeathDay() {
        return deathDay;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getId() {
        return id;
    }
}