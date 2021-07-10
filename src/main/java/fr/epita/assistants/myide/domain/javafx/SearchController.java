package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.features.any.SearchImplementation;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class SearchController {
    public TextField searchBar;
    public Button searchButton;
    public ListView<Node> listView;
    public Button openButton;

    private GuiController guiController;

    private Node selected = null;

    private String query = "filename";

    public void setup(GuiController guiController) {
        this.guiController = guiController;
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldV, newV) -> selectFile());
        openButton.setDisable(true);
    }

    public void selectFile() {
        selected = listView.getSelectionModel().getSelectedItem();
        openButton.setDisable(false);
    }

    public void searchItem(ActionEvent actionEvent) {
        listView.getItems().clear();
        openButton.setDisable(true);

        var feature = guiController.project.getFeature(Mandatory.Features.Any.SEARCH);
        if (feature.isEmpty())
            throw new UnsupportedOperationException();
        var exec = (SearchImplementation.SearchReport) feature.get().execute(guiController.project, query, searchBar.getText());
        if (!exec.isSuccess())
            return;

        var tmp = new HashSet<Node>();
        for (var i : exec.getResult()) {
            System.out.println(i.get("path"));
            try {
                tmp.add(getNodeWithPath(new File(i.get("path")).toPath()));
            } catch (Exception ignored) {
            }
        }
        listView.getItems().addAll(tmp.stream().sorted(Comparator.comparing(Object::toString)).toList());
    }

    private Node getNodeWithPath(Path path) {
        final Node rootNode = guiController.project.getRootNode();
        var nodePath = rootNode.getPath().relativize(path).resolve(path.getFileName());
        System.out.println(nodePath + "\n");
        var parentList = new ArrayList<Path>();
        while (nodePath.getParent() != null) {
            parentList.add(0, nodePath.getParent().getFileName());
            nodePath = nodePath.getParent();
        }

        Node current = rootNode;
        for (var i : parentList) {
            current = searchChildren(current, i);
        }
        return current;
    }

    private Node searchChildren(Node current, Path i) {
        for (var childNode : current.getChildren()) {
            if (childNode.getPath().getFileName().equals(i))
                return childNode;
        }
        throw new UnsupportedOperationException();
    }

    public void openAction(ActionEvent actionEvent) {
        if (selected != null) {
            guiController.openNode(selected);
        } else {
            openButton.setDisable(true);
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
        searchButton.getScene().getWindow().hide();
    }

    public void openCheck(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                selectFile();
                openAction(null);
            }
        }
    }

    public void fileSelect(ActionEvent actionEvent) {
        query = "filename";
    }

    public void textSelect(ActionEvent actionEvent) {
        query = "contents";
    }
}
