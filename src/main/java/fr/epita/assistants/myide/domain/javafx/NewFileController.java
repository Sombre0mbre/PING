package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.NodeServiceImplementation;
import fr.epita.assistants.myide.domain.service.ProjectService;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementation;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class NewFileController {
    public Button createButton;
    public Button browseButton;
    public TextField locationField;
    public TextField nameField;

    Project project;

    public void initialize() {
        nameField.textProperty().addListener((obs, oldText, newText) -> updateCreateButton());
        locationField.textProperty().addListener((obs, oldText, newText) -> updateCreateButton());
    }

    public void setup(Project project) {
        this.project = project;
        locationField.textProperty().set("src/main/java/");
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        var service = new ProjectServiceImplementation();
    }

    public void cancelAction(ActionEvent actionEvent) {
        createButton.getScene().getWindow().hide();
    }


    public void browseFiles(ActionEvent actionEvent) {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        var got = chooser.showDialog(window);

        if (got != null) {
            locationField.setText(got.getPath());
        }
    }

    public void updateCreateButton() {
        var tmp = new File(locationField.getText());
        createButton.setDisable(!tmp.isDirectory() || nameField.getText().isBlank() || tmp.toPath().resolve(nameField.getText()).toFile().isDirectory());
    }
}
