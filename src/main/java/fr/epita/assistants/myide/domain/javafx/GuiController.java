package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Project;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GuiController {

    public AnchorPane mainAnchor;
    public TreeView<String> treeView;

    Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    // the initialize method is automatically invoked by the FXMLLoader - it's magic
    public void initialize() {
        loadTreeItems("initial 1", "initial 2", "initial 3");
    }

    // loads some strings into the tree in the application UI.
    public void loadTreeItems(String... rootItems) {
        TreeItem<String> root = new TreeItem<String>("Root Node", getIcon());
        root.setExpanded(true);
        for (String itemString: rootItems) {
            root.getChildren().add(new TreeItem<String>(itemString));
        }

        treeView.setRoot(root);
    }

    private Node getIcon() {
        return null;
    }
}
