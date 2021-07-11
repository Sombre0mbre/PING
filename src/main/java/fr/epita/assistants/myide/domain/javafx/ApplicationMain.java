package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ApplicationMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        SceneLoader.loadStartup(primaryStage);
    }
}
