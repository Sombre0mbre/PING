package fr.epita.assistants.myide.domain.javafx;

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

    @FXML
    public void initialize() {
        tutorialText.setText("");
    }

    // TODO - Add buttons dynamically to flowPane and set text accordingly

}
