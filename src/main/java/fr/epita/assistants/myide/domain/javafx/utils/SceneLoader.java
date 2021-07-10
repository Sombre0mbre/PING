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
import java.net.URL;
import java.util.Objects;

public class SceneLoader {

    private static Parent loadFXML(URL path) {
        if (path == null)
            throw new IllegalArgumentException("path is null");
        var fxmlLoader = new FXMLLoader(path);
        Parent res;
        try {
            res = fxmlLoader.load();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        Utils.applyThemeMode(res);
        return res;
    }



    public static void loadGui(@NotNull Stage stage, @Nullable Project project) {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/gui.fxml")));
        Parent gui;
        try {
            gui = fxmlLoader.load();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        Utils.applyThemeMode(gui);
        Scene scene = new Scene(gui);

        stage.setScene(scene);
        assert project != null;
        ((GuiController) fxmlLoader.getController()).setup(project);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void loadNewProjectScene(@NotNull Stage stage) {
        Parent gui = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/newProject.fxml")) ;

        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void loadStartup(@NotNull Stage stage) {
        Parent gui = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/startup.fxml")) ;

        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }


    public static Stage tutorialWindow = new Stage();
    public static void loadTutorial() {
        Parent gui = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/tutorial.fxml")) ;

        Scene scene = new Scene(gui);

        tutorialWindow.setTitle("Tutorial");
        tutorialWindow.setScene(scene);

        // Set position of second window, related to primary window.
        tutorialWindow.centerOnScreen();
        tutorialWindow.setResizable(false);
        tutorialWindow.show();
    }
}
