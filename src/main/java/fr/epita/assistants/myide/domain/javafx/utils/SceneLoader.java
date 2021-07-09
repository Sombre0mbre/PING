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

    public static void loadGui(@NotNull Stage stage, @Nullable Project project) throws IOException {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/gui.fxml")));
        Parent gui = fxmlLoader.load();
        Scene scene = new Scene(gui);
        ((GuiController) fxmlLoader.getController()).setProject(project);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void loadNewProjectScene(@NotNull Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/newProject.fxml")));
        Parent gui = fxmlLoader.load();
        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void loadStartup(@NotNull Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/startup.fxml")));
        Parent gui = fxmlLoader.load();
        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void loadTutorial() throws IOException {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("fxml/tutorial.fxml")));
        Parent gui = fxmlLoader.load();
        Scene scene = new Scene(gui);

        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(scene);

        // Set position of second window, related to primary window.
        newWindow.centerOnScreen();

        newWindow.show();

        newWindow.setResizable(false);
        newWindow.show();
    }
}
