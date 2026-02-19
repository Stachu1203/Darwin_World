package model;

public class ClimateManager {
    private Seasons currentSeason;
    private int dayCounter;
    private final SimulationConfig config;
    private int temperature;

    public ClimateManager(SimulationConfig config){
        this.config = config;
        this.temperature = config.startingTemperature;
        this.currentSeason = Seasons.SUMMER;
        this.dayCounter = 0;
    }

    public void updateSeason(){
        if(this.currentSeason == Seasons.SUMMER) {
            this.currentSeason = Seasons.WINTER;
            this.dayCounter=0;
        }
        else{
            this.currentSeason = Seasons.SUMMER;
            this.dayCounter=0;
        }
    }

    public void nextDay(){
        dayCounter+=1;

        if (dayCounter >= config.seasonDuration) {
            updateSeason();
        }

        if (currentSeason == Seasons.WINTER && temperature>config.minTemperature){
            temperature-=1;
        }else{
            if(temperature< config.startingTemperature){
                temperature+=1;
            }
        }
    }

    public Seasons getCurrentSeason(){
        return this.currentSeason;
    }

    public int getCurrentDay(){
        return this.dayCounter;
    }
    public int getTemperature(){
        return this.temperature;
    }

}
