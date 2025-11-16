package org.example._0zo.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example._0zo.MainApplication;

/**
 * Controlador del menú principal del juego.
 * Gestiona la selección del número de jugadores máquina y el inicio del juego.
 *
 * @author Sebastian-Javier-Alejandro
 * @version 1.0
 */
public class MenuController {

    @FXML
    private Button btnStart;

    @FXML
    private ComboBox<Integer> cmbNumRobots;

    private static int selectedNumMachines = 2;

    /**
     * Inicializa el controlador configurando el ComboBox y los listeners.
     */
    @FXML
    private void initialize() {
        cmbNumRobots.getItems().addAll(1, 2, 3);
        cmbNumRobots.setValue(2);

        cmbNumRobots.setOnAction(event -> {
            selectedNumMachines = cmbNumRobots.getValue();
        });

        btnStart.setOnAction(event -> startGame());
    }

    /**
     * Inicia el juego cargando la vista principal y pasando el número de máquinas seleccionado.
     */
    private void startGame() {
        try {
            Stage stage = (Stage) btnStart.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("hello-view.fxml")
            );

            Scene gameScene = new Scene(loader.load(), 609, 609);

            GameController gameController = loader.getController();
            gameController.setNumMachines(selectedNumMachines);

            stage.setScene(gameScene);
            stage.setTitle("Cincuentazo - En Juego");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del juego: " + e.getMessage());
        }
    }

    /**
     * Obtiene el número de máquinas seleccionado.
     *
     * @return Número de jugadores máquina (1-3)
     */
    public static int getSelectedNumMachines() {
        return selectedNumMachines;
    }
}