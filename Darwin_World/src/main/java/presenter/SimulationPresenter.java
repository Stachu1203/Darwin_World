package presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import model.*;

import java.util.List;
import java.util.Objects;

public class SimulationPresenter {

    @FXML private GridPane mapGrid;

    @FXML private Label dayLabel;
    @FXML private Label animalCountLabel;
    @FXML private Label plantCountLabel;
    @FXML private Label freeFieldsLabel;
    @FXML private Label avgEnergyLabel;
    @FXML private Label avgLifespanLabel;
    @FXML private Label avgChildrenLabel;
    @FXML private Label bestGenotypeLabel;

    private WorldMap worldMap;
    private double cellSize = 25;
    private boolean isPaused = false;
    private Image cockroachImage;

    @FXML
    public void initialize() {
        try {
            cockroachImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/karaluch.png")));
        } catch (Exception e) {

        }
    }

    public void drawMap(WorldMap worldMap, SimulationStats stats) {
        this.worldMap = worldMap;
        Seasons seasonToDraw = Seasons.SUMMER;
        if (worldMap instanceof GlobeMap globeMap) {
            seasonToDraw = globeMap.getClimateManager().getCurrentSeason();
            System.out.println("DEBUG: Sezon wysłany do GUI: " + seasonToDraw);
        }

        final Seasons finalSeason = seasonToDraw;
        Platform.runLater(() -> {
            updateMapVisuals(finalSeason);
            updateStatsVisuals(stats, worldMap.getCurrentDay());
        });
    }

    private void updateStatsVisuals(SimulationStats stats, int day) {
        dayLabel.setText(String.valueOf(day));
        animalCountLabel.setText(String.valueOf(stats.getAnimalCount()));
        plantCountLabel.setText(String.valueOf(stats.getPlantCount()));
        freeFieldsLabel.setText(String.valueOf(stats.getFreeFieldsCount()));
        avgEnergyLabel.setText(String.format("%.2f", stats.getAverageEnergy()));
        avgLifespanLabel.setText(String.format("%.2f", stats.getAverageLifespan()));
        avgChildrenLabel.setText(String.format("%.2f", stats.getAverageChildrenCount()));
        bestGenotypeLabel.setText(stats.getMostPopularGenotype());
    }

    private void updateMapVisuals(Seasons currentSeason) {
        mapGrid.getChildren().clear();
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();

        int width = worldMap.getWidth();
        int height = worldMap.getHeight();

        for (int i = 0; i < width; i++) mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        for (int i = 0; i < height; i++) mapGrid.getRowConstraints().add(new RowConstraints(cellSize));


        Color stepColor = (currentSeason == Seasons.WINTER) ? Color.web("#D1D1D1") : Color.web("#F5F5DC");
        Color grassColor = (currentSeason == Seasons.WINTER) ? Color.web("#4F7942") : Color.FORESTGREEN;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Rectangle background = new Rectangle(cellSize, cellSize);
                if (y >= (int) (0.4 * height) && y <= (int) (0.6 * height)) {
                    background.setFill(Color.web("#90EE90"));
                } else {
                    background.setFill(stepColor);
                }
                mapGrid.add(background, x, y);
            }
        }

        for (Vector2d pos : worldMap.getPlants()) {
            Rectangle grass = new Rectangle(cellSize, cellSize);
            grass.setFill(grassColor);
            mapGrid.add(grass, pos.getX(), pos.getY());
        }



        List<Animal> animals = worldMap.getAllAnimals();
        for (Animal animal : animals) {
            Vector2d pos = animal.getPosition();

            if (cockroachImage != null) {
                ImageView animalView = new ImageView(cockroachImage);
                animalView.setFitWidth(cellSize);
                animalView.setFitHeight(cellSize);
                animalView.setPreserveRatio(true);
                animalView.setEffect(createColorEffect(getEnergyColor(animal.getEnergy())));
                GridPane.setHalignment(animalView, HPos.CENTER);
                GridPane.setValignment(animalView, VPos.CENTER);
                mapGrid.add(animalView, pos.getX(), pos.getY());
            } else {
                javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(cellSize/2.5);
                circle.setFill(getEnergyColor(animal.getEnergy()));
                GridPane.setHalignment(circle, HPos.CENTER);
                mapGrid.add(circle, pos.getX(), pos.getY());
            }
        }
    }

    private Lighting createColorEffect(Color color) {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setColor(color);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(0.0);
        lighting.setDiffuseConstant(1.5);
        return lighting;
    }

    private Color getEnergyColor(int energy) {
        double hue = 30; // Stały odcień brązu

        if (energy < 10) {

            return Color.hsb(hue, 0.15, 0.95);
        } else if (energy < 30) {

            return Color.hsb(hue, 0.40, 0.80);
        } else if (energy < 50) {

            return Color.hsb(hue, 0.70, 0.55);
        } else if (energy < 100) {

            return Color.hsb(hue, 0.90, 0.35);
        } else {

            return Color.hsb(hue, 1.0, 0.15);
        }
    }
    @FXML
    public void onPauseClicked() {
        isPaused = !isPaused;
    }


    public boolean isPaused() {
        return isPaused;
    }
}