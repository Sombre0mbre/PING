package fr.epita.assistants.myide.domain.javafx.utils;

import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.GuiController;
import fr.epita.assistants.myide.domain.javafx.NewFileController;
import fr.epita.assistants.myide.domain.javafx.SearchController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URL;

public class SceneLoader {

    public static Stage tutorialWindow = new Stage();
    public static Stage searchWindow = new Stage();

    private static LoadReport loadFXML(URL path) {
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
        return new LoadReport(res, fxmlLoader);
    }

    public static void loadGui(@NotNull Stage stage, @Nullable Project project) {
        var loadReport = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/gui.fxml"));

        Utils.applyThemeMode(loadReport.gui);
        Scene scene = new Scene(loadReport.gui);

        stage.setScene(scene);
        assert project != null;
        ((GuiController) loadReport.loader.getController()).setup(project);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void loadNewProjectScene(@NotNull Stage stage) {
        Parent gui = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/newProject.fxml")).gui;

        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void loadStartup(@NotNull Stage stage) {
        Parent gui = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/startup.fxml")).gui;

        Scene scene = new Scene(gui);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void loadSearch(@NotNull Stage stage, @NotNull GuiController controller) {
        var loadReport = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/search.fxml"));

        Scene scene = new Scene(loadReport.gui);


        searchWindow.setTitle("Rechercher...");
        searchWindow.setScene(scene);

        ((SearchController) loadReport.loader.getController()).setup(controller);

        // Set position of second window, related to primary window.
        searchWindow.centerOnScreen();
        searchWindow.setResizable(false);
        searchWindow.show();
    }

    public static void loadTutorial() {
        Parent gui = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/tutorial.fxml")).gui;

        Scene scene = new Scene(gui);

        tutorialWindow.setTitle("Tutoriel");
        tutorialWindow.setScene(scene);

        // Set position of second window, related to primary window.
        tutorialWindow.centerOnScreen();
        tutorialWindow.setResizable(false);
        tutorialWindow.show();
    }

    public static void loadNewFile(Project project, GuiController gui) {
        var loadReport = loadFXML(SceneLoader.class.getClassLoader().getResource("fxml/newFile.fxml"));
        ((NewFileController) loadReport.loader.getController()).setup(project, gui);

        Scene scene = new Scene(loadReport.gui);
        var window = new Stage();

        window.setTitle("Nouveau fichier");
        window.setScene(scene);

        // Set position of second window, related to primary window.
        window.centerOnScreen();
        window.setResizable(false);
        window.showAndWait();
    }

    private record LoadReport(Parent gui, FXMLLoader loader) {
    }
}
