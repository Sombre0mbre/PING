package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementation;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.IOException;

public class StartupController {
    public Button newProjectButton;
    public Button openProjectButton;

    public void loadExistingProject(ActionEvent actionEvent) {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        var got = chooser.showDialog(window);

        if (got != null) {
            var project = new ProjectServiceImplementation().load(got.toPath());
            SceneLoader.loadGui((Stage) window, project);
        }
    }


    public void createNewProject(ActionEvent actionEvent) {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();

        SceneLoader.loadNewProjectScene((Stage) window);
    }
}
