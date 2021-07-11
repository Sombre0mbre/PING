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

import javax.annotation.RegEx;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class NewFileController {
    public Button createButton;
    public Button browseButton;
    public TextField locationField;
    public TextField nameField;
    private NodeServiceImplementation service = new NodeServiceImplementation();
    private GuiController gui;

    final static String regex = "^.+[.]java$";

    Project project;

    public void initialize() {
        nameField.textProperty().addListener((obs, oldText, newText) -> updateCreateButton());
        locationField.textProperty().addListener((obs, oldText, newText) -> updateCreateButton());
    }

    public void setup(Project project, GuiController guiController) {
        this.project = project;
        this.gui = guiController;

        locationField.setText(project.getRootNode().getPath().toString());
    }

    public void createNewFile(ActionEvent actionEvent) {
        var parent = new File(locationField.getText()).toPath();
        service.generateChildren(project.getRootNode());
        var n = service.createDirectories(project.getRootNode(), parent);

        var name = nameField.getText();
        if (!name.matches("^.+[.].+$")) {
            name = name + ".java";
        }
        var got = service.create(n, name, fr.epita.assistants.myide.domain.entity.Node.Types.FILE);
        if (name.matches(regex)) {
            service.setText(got, ("public class " + name.substring(0, (name.length() - ".java".length())) + " {\n}").getBytes(StandardCharsets.UTF_8));
            System.out.println("set");
        }
        gui.openNode(got);
        cancelAction(null);
    }

    public void cancelAction(ActionEvent actionEvent) {
        createButton.getScene().getWindow().hide();
    }


    public void browseFiles(ActionEvent actionEvent) {
        final var window = ((Node) actionEvent.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        chooser.setInitialDirectory(project.getRootNode().getPath().toFile());
        var got = chooser.showDialog(window);

        if (got != null) {
            locationField.setText(got.getPath());
        }
    }

    public void updateCreateButton() {
        var parent = new File(locationField.getText());
        var parentPath = parent.toPath();
        createButton.setDisable(
                !parent.isDirectory()
                        || nameField.getText().isBlank()
                        || !parentPath.startsWith(project.getRootNode().getPath())
                        || parentPath.resolve(nameField.getText()).toFile().isFile()
                        || (!nameField.getText().matches(regex) && parentPath.resolve(nameField.getText() + ".java").toFile().isFile())
        );
    }
}
