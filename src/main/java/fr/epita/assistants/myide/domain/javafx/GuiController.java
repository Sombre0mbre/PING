package fr.epita.assistants.myide.domain.javafx;

import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.features.any.SearchImplementation;
import fr.epita.assistants.myide.domain.javafx.utils.Icons;
import fr.epita.assistants.myide.domain.javafx.utils.SceneLoader;
import fr.epita.assistants.myide.domain.javafx.utils.SyntaxColor;
import fr.epita.assistants.myide.domain.javafx.utils.Utils;
import fr.epita.assistants.myide.domain.service.NodeServiceImplementation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public Project project;

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
        var search = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

        tabPane.getScene().getAccelerators().put(save, () -> saveFile(null));
        tabPane.getScene().getAccelerators().put(tutorial, () -> startTutorial(null));
        tabPane.getScene().getAccelerators().put(search, () -> search(null));

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

        openNode(node);
    }

    public void openNode(fr.epita.assistants.myide.domain.entity.Node node) {
        var got = tabPane.getTabs().stream().filter((e) -> node.equals(e.getUserData())).findAny();
        if (got.isPresent()) {
            tabPane.getSelectionModel().select(got.get());
            return;
        }

        CodeArea text = new CodeArea();
        var tab = new Tab(node.getPath().getFileName().toString(), new VirtualizedScrollPane<>(text));

        // add line numbers to the left of area
        text.setParagraphGraphicFactory(LineNumberFactory.get(text));
        text.setContextMenu(new SyntaxColor.DefaultContextMenu());

        text.getVisibleParagraphs().addModificationObserver
                (
                        new SyntaxColor.VisibleParagraphStyler<>(text, SyntaxColor::computeHighlighting)
                );

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        text.addEventHandler(KeyEvent.KEY_PRESSED, KE ->
        {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = text.getCaretPosition();
                int currentParagraph = text.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(text.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> text.insertText(caretPosition, m0.group()));
            }
        });

        text.replaceText(0, 0, service.getContent(node));
        text.textProperty().addListener((observable, oldValue, newValue) -> setEdited(tab, true));
        tab.setUserData(node);
        setTabBG(tab);
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
        final var text = ((CodeArea) ((VirtualizedScrollPane) tab.getContent()).getContent());

        service.setText(node, text.getText().getBytes(StandardCharsets.UTF_8));
        var feature = project.getFeature(Mandatory.Features.Any.SEARCH);
        feature.ifPresent(value ->
                CompletableFuture.supplyAsync(() ->
                        ((SearchImplementation) value).updateCache(project)
                )
        );

    }

    public void changeProject(ActionEvent actionEvent) {
        SceneLoader.loadStartup((Stage) tabPane.getScene().getWindow());
    }


    public void changeTheme(ActionEvent actionEvent) {
        Utils.setDarkMode(!Utils.isDarkMode());
        Utils.applyThemeMode(mainAnchor);
        for (var tab : tabPane.getTabs()) {
            setTabBG(tab);
        }
    }

    private void setTabBG(Tab t) {
        var tmp = (VirtualizedScrollPane) t.getContent();
        var text = (CodeArea) tmp.getContent();
        if (Utils.isDarkMode())
            text.setBackground(new Background(new BackgroundFill(Color.DIMGRAY, new CornerRadii(2), Insets.EMPTY)));
        else
            text.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(2), Insets.EMPTY)));
    }

    public void search(ActionEvent actionEvent) {
        // TODO
        SceneLoader.loadSearch((Stage) mainAnchor.getScene().getWindow(), this);
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

        var loading = Utils.newAlertWrapper(Alert.AlertType.INFORMATION, "Veuillez patienter...");
        task.setOnRunning((e) -> loading.show());
        task.setOnSucceeded((e) -> {
            loading.hide();
            Alert alert;
            if (report != null && report.isSuccess()) {
                alert = Utils.newAlertWrapper(Alert.AlertType.INFORMATION, "L'action a été exectuée avec succès!");
            } else {
                alert = Utils.newAlertWrapper(Alert.AlertType.ERROR, "Impossible d'effectuer l'action demandée !");
            }
            alert.showAndWait();
        });
        task.setOnFailed((e) -> {
            loading.hide();
            Alert alert = Utils.newAlertWrapper(Alert.AlertType.ERROR, "Impossible d'effectuer l'action demandée !");
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
