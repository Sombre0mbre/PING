package fr.epita.assistants.myide.domain.javafx.utils;

import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.ListModification;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxColor {

    public static final String[] KEYWORDS = new String[]{
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    public static final String PAREN_PATTERN = "\\(|\\)";
    public static final String BRACE_PATTERN = "\\{|\\}";
    public static final String BRACKET_PATTERN = "\\[|\\]";
    public static final String SEMICOLON_PATTERN = "\\;";
    public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    public static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
            + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String sampleCode = String.join("\n", new String[]{
            "package com.example;",
            "",
            "import java.util.*;",
            "",
            "public class Foo extends Bar implements Baz {",
            "",
            "    /*",
            "     * multi-line comment",
            "     */",
            "    public static void main(String[] args) {",
            "        // single-line comment",
            "        for(String arg: args) {",
            "            if(arg.length() != 0)",
            "                System.out.println(arg);",
            "            else",
            "                System.err.println(\"Warning: empty string as argument\");",
            "        }",
            "    }",
            "",
            "}"
    });


    /*public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CodeArea codeArea = new CodeArea();

        // add line numbers to the left of area
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setContextMenu( new DefaultContextMenu() );

        // recompute the syntax highlighting for all text, 500 ms after user stops editing area
        // Note that this shows how it can be done but is not recommended for production with
        // large files as it does a full scan of ALL the text every time there is a change !
        Subscription cleanupWhenNoLongerNeedIt = codeArea
                // plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
                // multi plain changes = save computation by not rerunning the code multiple times
                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
                .multiPlainChanges()
                // do not emit an event until 500 ms have passed since the last emission of previous stream
                .successionEnds(Duration.ofMillis(500))
                // run the following code block when previous stream emits an event
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        // when no longer need syntax highlighting and wish to clean up memory leaks
        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`

        // recompute syntax highlighting only for visible paragraph changes
        // Note that this shows how it can be done but is not recommended for production where multi-
        // line syntax requirements are needed, like comment blocks without a leading * on each line.
        codeArea.getVisibleParagraphs().addModificationObserver
                (
                        new VisibleParagraphStyler<>( codeArea, this::computeHighlighting )
                );

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, KE ->
        {
            if ( KE.getCode() == KeyCode.ENTER ) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeArea.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeArea.insertText( caretPosition, m0.group() ) );
            }
        });


        codeArea.replaceText(0, 0, sampleCode);

        Scene scene = new Scene(new StackPane(new VirtualizedScrollPane<>(codeArea)), 600, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/java-keywords.css").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java Keywords Demo");
        primaryStage.show();
    }*/

    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.singleton("code"), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.singleton("code"), text.length() - lastKwEnd);
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

    public static class DefaultContextMenu extends ContextMenu {
        public MenuItem fold, unfold, print;

        public DefaultContextMenu() {
            fold = new MenuItem("Fold selected text");
            fold.setOnAction(AE -> {
                hide();
                fold();
            });

            unfold = new MenuItem("Unfold from cursor");
            unfold.setOnAction(AE -> {
                hide();
                unfold();
            });

            print = new MenuItem("Print");
            print.setOnAction(AE -> {
                hide();
                print();
            });

            getItems().addAll(fold, unfold, print);
        }

        /**
         * Folds multiple lines of selected text, only showing the first line and hiding the rest.
         */
        public void fold() {
            ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
        }

        /**
         * Unfold the CURRENT line/paragraph if it has a fold.
         */
        public void unfold() {
            CodeArea area = (CodeArea) getOwnerNode();
            area.unfoldParagraphs(area.getCurrentParagraph());
        }

        public void print() {
            System.out.println(((CodeArea) getOwnerNode()).getText());
        }
    }
}