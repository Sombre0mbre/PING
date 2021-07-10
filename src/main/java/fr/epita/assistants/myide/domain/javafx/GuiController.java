package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.javafx.utils.Icons;
import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.javafx.utils.Utils;
import fr.epita.assistants.myide.domain.service.NodeServiceImplementation;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GuiController {
    public final int treeImageHeight = 20;
    final NodeServiceImplementation service = new NodeServiceImplementation();
    public AnchorPane mainAnchor;
    public TreeView<fr.epita.assistants.myide.domain.entity.Node> treeView;
    public Button tutorialButton;
    public TabPane tabPane;
    public MenuButton gitButton;
    public MenuButton mavenButton;
    public MenuButton otherButton;
    Project project;

    public void initialize() {
        tabPane.getTabs().clear();
    }

    // Project management {
    public void setup(Project project) {
        this.project = project;

        // Enabling features
        final var aspects = project.getAspects().stream().map(Aspect::getType).collect(Collectors.toSet());
        if (!aspects.contains(Mandatory.Aspects.GIT))
            gitButton.setDisable(true);
        if (!aspects.contains(Mandatory.Aspects.MAVEN))
            mavenButton.setDisable(true);

        // Shortcuts
        var save = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        var tutorial = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);

        Runnable saveRunnable = () -> saveFile(null);
        Runnable tutorialRunnable = () -> startTutorial(null);

        tabPane.getScene().getAccelerators().put(save, saveRunnable);
        tabPane.getScene().getAccelerators().put(tutorial, tutorialRunnable);

        // Tree generation from nodes
        updateTree();
    }

    public void updateTree() {
        final var node = project.getRootNode();

        var root = new TreeItem<>(node);
        root.setExpanded(true);
        updateTreeSub(node, root);

        treeView.setRoot(root);
    }

    private void updateTreeSub(fr.epita.assistants.myide.domain.entity.Node node, TreeItem<fr.epita.assistants.myide.domain.entity.Node> root) {
        for (var child : node.getChildren()) {
            final var childTree = new TreeItem<>(child, getIcon(child));

            root.getChildren().add(childTree);
            if (child.isFolder()) {
                updateTreeSub(child, childTree);
            }
        }
    }

    private Node getIcon(fr.epita.assistants.myide.domain.entity.Node node) {
        ImageView imageView;
        if (node.isFolder()) {
            imageView = new ImageView(Icons.folderIcon);
        } else {
            imageView = new ImageView(Icons.fileIcon);
        }

        imageView.setFitHeight(treeImageHeight);
        imageView.setPreserveRatio(true);

        return imageView;
    }
    // } End of project management


    public void treeOpenEvent(MouseEvent mouseEvent) {
        var selected = treeView.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        var node = selected.getValue();
        if (node.isFolder())
            return;

        var got = tabPane.getTabs().stream().filter((e) -> node.equals(e.getUserData())).findAny();
        if (got.isPresent()) {
            tabPane.getSelectionModel().select(got.get());
            return;
        }

        var text = new TextArea(service.getContent(node));
        var tab = new Tab(node.getPath().getFileName().toString(), text);

        text.textProperty().addListener((observable, oldValue, newValue) -> setEdited(tab, true));

        tab.setUserData(node);

        tabPane.getTabs().add(0, tab);
        tabPane.getSelectionModel().select(0);

    }

    public void startTutorial(ActionEvent actionEvent) {
        System.out.println("Opening tutorial");
        SceneLoader.loadTutorial();
    }

    public void newFile(ActionEvent actionEvent) {
        // TODO - new window to choose where to create file
        // Then get parent node
        // Then
        // service.create(parentNode, filename, fr.epita.assistants.myide.domain.entity.Node.Types.FILE);
        //
        // Add to file:
        // public class <filename> {
        //
        // }
    }

    public void saveFile(ActionEvent actionEvent) {
        var selected = tabPane.getSelectionModel().getSelectedItem();
        saveTab(selected);
        setEdited(selected, false);
    }

    public void saveAllFiles(ActionEvent actionEvent) {
        for (var tab : tabPane.getTabs()) {
            saveTab(tab);
            setEdited(tab, false);
        }
    }

    private void setEdited(Tab tab, boolean edited) {
        final var node = (NodeImplementation) tab.getUserData();

        tab.setText(edited ? "⚫ " + node.toString() : node.toString());
    }

    private void saveTab(Tab tab) {
        if (tab == null)
            return;
        System.out.println("Saving tab: " + tab.getText());

        var node = (NodeImplementation) tab.getUserData();
        service.setText(node, ((TextArea) tab.getContent()).getText().getBytes(StandardCharsets.UTF_8));
    }

    public void changeProject(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadStartup((Stage) tabPane.getScene().getWindow());
    }


    public void changeTheme(ActionEvent actionEvent) {
        Utils.setDarkMode(!Utils.isDarkMode());
        Utils.applyThemeMode(mainAnchor);
    }

    public void search(ActionEvent actionEvent) {
        // TODO
    }

    public void cleanup(ActionEvent actionEvent) {
        var got = project.getFeature(Mandatory.Features.Any.CLEANUP);
        if (got.isEmpty())
            return;
        var report = got.get().execute(project);

        showResult(report);
    }

    public void dist(ActionEvent actionEvent) {
        var got = project.getFeature(Mandatory.Features.Any.DIST);
        if (got.isEmpty())
            return;
        var report = got.get().execute(project);

        showResult(report);
    }

    private void showResult(Feature.ExecutionReport report) {
        Task<Boolean> task = new Task<>() {
            @Override
            public Boolean call() {
                return report.isSuccess();
            }
        };

        var loading = new Alert(Alert.AlertType.INFORMATION, "Veuillez patienter...");
        loading.setTitle(null);
        task.setOnRunning((e) -> loading.show());
        task.setOnSucceeded((e) -> {
            loading.hide();
            Alert alert;
            if (report != null && report.isSuccess()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "L'action a été exectuée avec succès!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Impossible d'effectuer l'action demandée !");
            }
            alert.setHeaderText(null);
            alert.showAndWait();
        });
        task.setOnFailed((e) -> {
            loading.hide();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible d'effectuer l'action demandée !");
            alert.showAndWait();
        });
        new Thread(task).start();
    }


    public void gitAddEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void gitCommitEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void gitPushEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void gitPullEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void mvnPackageEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void mvnInstallEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void mvnExecEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void mvnCleanEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void mvnTestEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }

    public void mvnTreeEvent(ActionEvent actionEvent) {
        // TODO
        // showResult(report);
    }
}
