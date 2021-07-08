package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.javafx.utils.TutorialArrays;
import fr.epita.assistants.myide.domain.javafx.utils.TutorialElement;
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

    @FXML
    public void initialize() {
        tutorialText.setText("");
        flowPane.getChildren().clear();
        flowPane.setHgap(20);

    }

    public void gitButtonAction(ActionEvent actionEvent) {
        setFlowPaneFromArray(TutorialArrays.gitElements);
    }

    public void mavenButtonAction(ActionEvent actionEvent) {
        setFlowPaneFromArray(TutorialArrays.mavenElements);
    }

    public void javaButtonAction(ActionEvent actionEvent) {
        setFlowPaneFromArray(TutorialArrays.javaElements);
    }

    private void setFlowPaneFromArray(TutorialElement[] array) {
        flowPane.getChildren().clear();
        for (var i : array) {
            var button = new Button(i.title);

            button.setOnAction((event) -> tutorialText.setText(i.textData));

            flowPane.getChildren().add(button);

        }
    }

    // TODO - Add buttons dynamically to flowPane and set text accordingly

}
