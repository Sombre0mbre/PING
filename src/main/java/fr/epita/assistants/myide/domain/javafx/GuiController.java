package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Project;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class GuiController {

    public AnchorPane mainAnchor;
    public TreeView<String> treeView;

    Project project;

    public void setProject(Project project) {
        this.project = project;
        updateTree();
    }

    private void updateTree() {
        TreeItem<String> root = new TreeItem<>(project.getRootNode().getPath().getFileName().toString(), getIcon());
        root.setExpanded(true);
        updateTreeSub(project.getRootNode(), root);

        treeView.setRoot(root);
    }

    private void updateTreeSub(fr.epita.assistants.myide.domain.entity.Node node, TreeItem<String> root) {
        for (var child : node.getChildren()) {
            final var childTree = new TreeItem<>(child.getPath().getFileName().toString());
            root.getChildren().add(childTree);
            if (child.isFolder()) {
                updateTreeSub(child, childTree);
            }

        }
    }

    private Node getIcon() {
        return null;
    }
}
