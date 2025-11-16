package org.example._0zo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación.
 * Combina el launcher y la configuración de JavaFX.
 */
public class MainApplication extends Application {

    /**
     * Método principal - Punto de entrada del programa.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Configura e inicia la ventana principal.
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("Menu.fxml"));

        Scene scene = new Scene(loader.load(), 860, 575);  // ← Tamaño de tu FXML
        stage.setTitle("Cincuentazo - Juego de Cartas");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}