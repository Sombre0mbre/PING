package fr.epita.assistants.myide.domain.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class TutorialController {
    public Button gitButton;
    public FlowPane flowPane;
    public Button javaButton;
    public Button mavenButton;
    public TextArea tutorialText;

    final String[] gitButtonsNames = {"Push", "Pull", "Add", "Commit"};
    final String[] mavenButtonsNames = {"Compile", "Clean", "Exec", "Install", "Package", "Test", "Tree"};
    final String[] javaButtonsNames = {"if", "Boucles", "Fonctions", "Classes"};

    @FXML
    public void initialize() {
        tutorialText.setText("");
    }

    public void gitButtonAction(ActionEvent actionEvent) {

    }

    public void mavenButtonAction(ActionEvent actionEvent) {
    }

    public void javaButtonAction(ActionEvent actionEvent) {
    }

    private void setFlowPaneFromArray(String[] array) {
        flowPane.getChildren().clear();
        for (var i : array) {
            var button = new Button(i);

            flowPane.getChildren().add(button);
        }
    }

    // TODO - Add buttons dynamically to flowPane and set text accordingly

}
