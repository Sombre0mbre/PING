package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.utils.Icons;
import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.service.NodeServiceImplementation;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiController {
    public final int treeImageHeight = 20;
    final NodeServiceImplementation service = new NodeServiceImplementation();
    public AnchorPane mainAnchor;
    public TreeView<fr.epita.assistants.myide.domain.entity.Node> treeView;
    public Button tutorialButton;
    public TabPane tabPane;
    Project project;

    public void initialize() {
        tabPane.getTabs().clear();
    }

    // Project management {
    public void setup(Project project) {
        this.project = project;

        var save = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        var tutorial = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);

        Runnable saveRunnable = ()-> saveFile(null);
        Runnable tutorialRunnable = ()-> startTutorial(null);

        tabPane.getScene().getAccelerators().put(save, saveRunnable);
        tabPane.getScene().getAccelerators().put(tutorial, tutorialRunnable);
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


    public void treeSelectClickEvent(MouseEvent mouseEvent) {
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

        var tab = new Tab(node.getPath().getFileName().toString(), new TextArea(service.getContent(node)));

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
        // TODO
    }

    private void saveTab(Tab tab) {
        if (tab == null)
            return;
        System.out.println("Saving tab: " + tab.getText());
        // TODO
    }

    public void changeProject(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadStartup((Stage) tabPane.getScene().getWindow());
    }

    public void changeTheme(ActionEvent actionEvent) {
        // TODO
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
        // TODO
    }
}
