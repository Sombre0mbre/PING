package fr.epita.assistants.myide.domain.javafx.utils;

import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.GuiController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;

public class SceneLoader {

    public static void loadGui(@NotNull Stage stage, @Nullable Project project) {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/gui.fxml")));
        Parent gui;
        try {
            gui = fxmlLoader.load();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        Scene scene = new Scene(gui);
        stage.setScene(scene);
        ((GuiController) fxmlLoader.getController()).setup(project);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void loadNewProjectScene(@NotNull Stage stage) {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/newProject.fxml")));
        Parent gui;
        try {
            gui = fxmlLoader.load();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void loadStartup(@NotNull Stage stage) {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/startup.fxml")));
        Parent gui;
        try {
            gui = fxmlLoader.load();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }


    private static Stage window = new Stage();
    public static void loadTutorial() {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/tutorial.fxml")));
        Parent gui = null;
        try {
            gui = fxmlLoader.load();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        Scene scene = new Scene(gui);

        window.setTitle("Tutorial");
        window.setScene(scene);

        // Set position of second window, related to primary window.
        window.centerOnScreen();
        window.setResizable(false);
        window.show();
    }
}
