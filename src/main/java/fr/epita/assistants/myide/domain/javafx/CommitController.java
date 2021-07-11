package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.utils.Utils;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.function.Supplier;

public class CommitController {

    @FXML
    private Button validateButton;
    @FXML
    private TextField commitMessage;
    @FXML
    private AnchorPane pane;

    public Project project;

    public void setup(Project project) {
        this.project = project;
        commitMessage.setText("");
    }

    @FXML
    void Validate(ActionEvent event) {
        var got = project.getFeature(Mandatory.Features.Git.COMMIT);
        if (got.isEmpty())
            return;
        var params = commitMessage.getText();
        showResult(() -> got.get().execute(project, params));
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void DoValidate(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)  {
            validateButton.fire();
        }

    }

    public void showResult(Supplier<Feature.ExecutionReport> supplier) {
        final Feature.ExecutionReport[] report = {null};
        Task<Boolean> task = new Task<>() {
            @Override
            public Boolean call() {
                report[0] = supplier.get();
                if (report[0].isSuccess())
                    return true;
                throw new UnsupportedOperationException();
            }
        };

        var loading = Utils.newAlertWrapper(Alert.AlertType.INFORMATION, "Veuillez patienter...");
        var indicator = new ProgressIndicator();
        indicator.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        loading.setGraphic(indicator);
        task.setOnRunning((e) -> loading.show());
        task.setOnSucceeded((e) -> {
            loading.hide();
            successWindow(report[0]);
        });
        task.setOnFailed((e) -> {
            loading.hide();
            Alert alert = Utils.newAlertWrapper(Alert.AlertType.ERROR, "Impossible d'effectuer l'action demandée !");
            alert.showAndWait();
        });
        new Thread(task).start();
    }

    private void successWindow(Feature.ExecutionReport report) {
        Alert alert;
        if (report != null && report.isSuccess()) {
            alert = Utils.newAlertWrapper(Alert.AlertType.INFORMATION, "L'action a été exectuée avec succès!");
        } else {
            alert = Utils.newAlertWrapper(Alert.AlertType.ERROR, "AAAA d'effectuer l'action demandée !");
        }
        alert.showAndWait();
    }

}
