package fr.epita.assistants;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import fr.epita.assistants.myide.domain.gui.MyIdeGui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        var mainFrame = new JFrame("myIDE");
        mainFrame.setContentPane(new MyIdeGui().mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Continuing with default value");
        }
         */
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
