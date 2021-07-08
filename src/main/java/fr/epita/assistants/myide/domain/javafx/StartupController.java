package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;

public class StartupController {
    public Button newProjectButton;
    public Button openProjectButton;

    public void loadExistingProject(ActionEvent actionEvent) throws IOException {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        var got = chooser.showDialog(window);

        if (got != null) {
            var project = new ProjectServiceImplementation().load(got.toPath());
            SceneLoader.loadGui((Stage) window, project);
        }
    }





    public void createNewProject(ActionEvent actionEvent) throws IOException {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();

        SceneLoader.loadNewProjectScene((Stage) window);
    }
}
