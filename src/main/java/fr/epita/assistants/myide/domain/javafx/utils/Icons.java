package fr.epita.assistants.myide.domain.javafx.utils;

import javafx.scene.image.Image;

import java.util.Objects;

public class Icons {
    public static final Image fileIcon = new Image(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("icons/file.png")).toString());
    public static final Image folderIcon = new Image(Objects.requireNonNull(SceneLoader.class.getClassLoader().getResource("icons/folder.png")).toString());
}
