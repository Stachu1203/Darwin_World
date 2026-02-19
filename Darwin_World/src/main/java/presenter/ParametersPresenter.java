package presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class ParametersPresenter {

    @FXML private TextField widthField;
    @FXML private TextField heightField;
    @FXML private TextField startEnergyField;
    @FXML private TextField moveEnergyField;
    @FXML private TextField plantEnergyField;
    @FXML private TextField reproductionCostField;
    @FXML private TextField genomeSizeField;
    @FXML private TextField minMutationsField;
    @FXML private TextField maxMutationsField;
    @FXML private TextField startAnimalCountField;
    @FXML private TextField plantsPerDayField;

    @FXML
    public void onSimulationStartClicked() throws IOException {

        SimulationConfig config = new SimulationConfig();

        try {
            config.width = Integer.parseInt(widthField.getText());
            config.height = Integer.parseInt(heightField.getText());
            config.startEnergy = Integer.parseInt(startEnergyField.getText());
            config.moveEnergy = Integer.parseInt(moveEnergyField.getText());
            config.plantEnergy = Integer.parseInt(plantEnergyField.getText());
            config.childEnergyCost = Integer.parseInt(reproductionCostField.getText());
            config.genomSize = Integer.parseInt(genomeSizeField.getText());



            config.startAnimalCount = Integer.parseInt(startAnimalCountField.getText());

            int plants = Integer.parseInt(plantsPerDayField.getText());
            config.minAmountOfPlantsPerDay = plants;
            config.maxAmountOfPlantsPerDay = plants;


            config.winterPlantRatio = 0.2f;
            config.startingTemperature = 10;
            config.seasonDuration = 15;
            config.heatingRadius = 5;
            config.penaltyForLoneliness = 2;

        } catch (NumberFormatException e) {
            System.out.println("Błąd: Wpisz poprawne liczby całkowite w formularzu!");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();


        SimulationPresenter simulationPresenter = loader.getController();


        GlobeMap map = new GlobeMap(config);
        SimulationStats stats = new SimulationStats();
        ClimateManager manager = map.getClimateManager();


        SimulationEngine engine = new SimulationEngine(config, map, stats, manager, simulationPresenter);
        Thread engineThread = new Thread(engine);
        engineThread.start();


        Stage stage = new Stage();
        configureStage(stage, viewRoot);
        stage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulacja Darwin World");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}