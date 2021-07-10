package fr.epita.assistants.myide.domain.javafx.utils;

import javafx.application.Platform;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.ListModification;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxColor {

    static final String[] keyWords = new String[]{
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "var"
    };

    static final String keywordRegex = "\\b(" + String.join("|", keyWords) + ")\\b";
    static final String parenRegex = "[()]";
    static final String braceRegex = "[{}]";
    static final String bracketRegex = "[\\[\\]]";
    static final String semiColRegex = ";";
    static final String stringRegex = "\"([^\"\\\\]|\\\\.)*\"";
    static final String commentRegex = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/" + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";

    static final Pattern pattern = Pattern.compile(
            "(?<KEYWORD>" + keywordRegex + ")"
                    + "|(?<PAREN>" + parenRegex + ")"
                    + "|(?<BRACE>" + braceRegex + ")"
                    + "|(?<BRACKET>" + bracketRegex + ")"
                    + "|(?<SEMICOLON>" + semiColRegex + ")"
                    + "|(?<STRING>" + stringRegex + ")"
                    + "|(?<COMMENT>" + commentRegex + ")"
    );

    static String[] patternNames = new String[]{"KEYWORD", "PAREN", "BRACE", "BRACKET", "SEMICOLON", "STRING", "COMMENT"};

    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = pattern.matcher(text);
        var spansBuilder = new StyleSpansBuilder<Collection<String>>();
        int lastPosition = 0;
        while (matcher.find()) {
            String styleClass = null;
            for (var i : patternNames) {
                if (matcher.group(i) != null) {
                    styleClass = i.toLowerCase(Locale.ROOT);
                }
            }

            spansBuilder.add(Collections.singleton("code"), matcher.start() - lastPosition);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastPosition = matcher.end();
        }
        spansBuilder.add(Collections.singleton("code"), text.length() - lastPosition);
        return spansBuilder.create();
    }

    public static class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
        public final GenericStyledArea<PS, SEG, S> area;
        public final Function<String, StyleSpans<S>> computeStyles;
        public int prevParagraph, prevTextLength;

        public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String, StyleSpans<S>> computeStyles) {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm) {
            if (lm.getAddedSize() > 0) {
                int paragraph = Math.min(area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size() - 1);
                String text = area.getText(paragraph, 0, paragraph, area.getParagraphLength(paragraph));

                if (paragraph != prevParagraph || text.length() != prevTextLength) {
                    int startPos = area.getAbsolutePosition(paragraph, 0);
                    Platform.runLater(() -> area.setStyleSpans(startPos, computeStyles.apply(text)));
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            }
        }
    }
}
