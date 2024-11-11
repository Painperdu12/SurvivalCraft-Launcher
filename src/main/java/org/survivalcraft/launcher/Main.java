package org.survivalcraft.launcher;

import javafx.application.Application;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        verifyClassPresence("javafx.application.Application");
        Application.launch(Launcher.class, args);
    }

    public static void verifyClassPresence(String className) {
        try {
            Class.forName(className);
        } catch(ClassNotFoundException exception) {
            JOptionPane.showMessageDialog(null, "Error:\n" + exception.getMessage() + " not found! (Error Code 1)\nPlease go to discord.survivalcraft.org", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void exit() {
        Launcher.getInstance().getLogger().info("Shutting down SurvivalCraft Launcher...");
        System.exit(0);
    }

}