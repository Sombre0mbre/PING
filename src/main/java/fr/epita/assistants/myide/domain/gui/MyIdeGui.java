package fr.epita.assistants.myide.domain.gui;

import javax.swing.*;

public class MyIdeGui {
    public JPanel mainPanel;
    private JButton ouvrirButton;
    private JButton tutorielButton;
    private JButton gitButton;
    private JTree tree1;
    private JTextArea textArea1;
    private JButton mavenButton;
    private JButton autresButton;
    private JButton enregistrerButton;

    public MyIdeGui() {
        ouvrirButton.addActionListener(e -> {
            JFileChooser j = new JFileChooser();
            j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            j.showOpenDialog(ouvrirButton);
            j.setVisible(true);
        });
        enregistrerButton.addActionListener(e -> {
            JFileChooser j = new JFileChooser();
            j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            j.showSaveDialog(enregistrerButton);
            j.setVisible(true);
        });
    }
}
