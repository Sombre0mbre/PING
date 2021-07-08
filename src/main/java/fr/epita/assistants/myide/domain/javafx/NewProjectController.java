package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementation;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class NewProjectController {
    public Button browseButton;
    public TextField projectLocationField;
    public TextField projectNameField;
    public Button createProjectButton;
    public Button cancelButton;

    public void initialize() {
        projectNameField.textProperty().addListener((obs, oldText, newText) -> updateCreateButton());
        projectLocationField.textProperty().addListener((obs, oldText, newText) -> updateCreateButton());
    }


    public void createNewProject(ActionEvent actionEvent) throws IOException {
        var service = new ProjectServiceImplementation();
        var project = service.create(new File(projectLocationField.getText()).toPath().resolve(projectNameField.getText()));

        SceneLoader.loadGui((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), project);
    }

    public void cancelAction(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadStartup((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }


    public void browseFiles(ActionEvent actionEvent) {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        var got = chooser.showDialog(window);

        if (got != null) {
            projectLocationField.setText(got.getPath());
        }
    }

    public void updateCreateButton() {
        var tmp = new File(projectLocationField.getText());
        createProjectButton.setDisable(!tmp.isDirectory() || projectNameField.getText().isBlank() || tmp.toPath().resolve(projectNameField.getText()).toFile().isDirectory());
    }
}
