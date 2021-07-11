package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.features.any.SearchImplementation;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.NodeServiceImplementation;
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
    private NodeServiceImplementation service = new NodeServiceImplementation();

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
        var exec = (SearchImplementation.SearchReport) feature.get().execute(guiController.project, query, searchBar.getText() + "*");
        if (!exec.isSuccess())
            return;

        var tmp = new HashSet<Node>();
        for (var i : exec.getResult()) {
            System.out.println(i.get("path"));
            try {
                tmp.add(service.getNodeWithPath(guiController.project.getRootNode(), new File(i.get("path")).toPath()));
            } catch (Exception ignored) {
            }
        }
        listView.getItems().addAll(tmp.stream().sorted(Comparator.comparing(Object::toString)).toList());
    }

    public void openAction(ActionEvent actionEvent) {
        if (selected != null) {
            guiController.openNode(selected);
            guiController.mainAnchor.getScene().getWindow().requestFocus();
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
