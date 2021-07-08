package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.utils.Icons;
import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class GuiController {
    public final int treeImageHeight = 20;

    public AnchorPane mainAnchor;
    public TreeView<fr.epita.assistants.myide.domain.entity.Node> treeView;
    public Button tutorialButton;

    Project project;


    // Project management {
    public void setProject(Project project) {
        this.project = project;
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
        var node = treeView.getSelectionModel().getSelectedItem().getValue();
        if (node.isFolder())
            return;

        // TODO - Load node into the textView
        System.err.println("Not implemented: open " + node);
    }

    public void startTutorial(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadTutorial();
    }
}
