package fr.epita.assistants.myide.domain.javafx.utils;

public class TutorialElement {
    public final String title;
    public final String textData;

    public TutorialElement(String title, String textData) {
        this.title = title;
        this.textData = textData;
    }

    @Override
    public String toString() {
        return title;
    }
}
