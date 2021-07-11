package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ApplicationMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.getIcons().add(
                new Image(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icons/icon.png"))));
        SceneLoader.loadStartup(primaryStage);

    }
}
