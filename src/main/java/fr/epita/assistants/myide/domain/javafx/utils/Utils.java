package fr.epita.assistants.myide.domain.javafx.utils;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utils {
    private static boolean darkMode = false;
    private static final String style = "-fx-base: rgba(60, 63, 65, 255);";

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        Utils.darkMode = darkMode;
        if (SceneLoader.tutorialWindow.getScene() != null)
            applyThemeMode(SceneLoader.tutorialWindow.getScene().getRoot());
    }

    public static void applyThemeMode(Parent parent) {
        if (darkMode)
            parent.setStyle(style);
        else
            parent.getStyleClass().clear();
    }

    public static Scene newSceneWrapper(Parent parent) {
        final var res = new Scene(parent);
        parent.setStyle(style);
        return res;
    }

    public static Alert newAlertWrapper(Alert.AlertType alertType, String content, ButtonType... buttonTypes) {
        var res = new Alert(alertType, content, buttonTypes);
        res.setHeaderText(null);
        applyThemeMode(res.getDialogPane());
        return res;
    }
}
