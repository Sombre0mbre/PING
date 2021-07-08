package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.javafx.utils.Icons;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class GuiController {

    public AnchorPane mainAnchor;
    public TreeView<String> treeView;

    public final int treeImageHeight = 20;


    Project project;

    public void setProject(Project project) {
        this.project = project;
        updateTree();
    }

    private void updateTree() {
        final var node = project.getRootNode();

        TreeItem<String> root = new TreeItem<>(node.getPath().getFileName().toString(), getIcon(node));
        root.setExpanded(true);
        updateTreeSub(node, root);

        treeView.setRoot(root);
    }

    private void updateTreeSub(fr.epita.assistants.myide.domain.entity.Node node, TreeItem<String> root) {
        for (var child : node.getChildren()) {
            final var childTree = new TreeItem<>(child.getPath().getFileName().toString(), getIcon(child));

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
}
