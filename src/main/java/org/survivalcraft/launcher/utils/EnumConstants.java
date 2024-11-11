package org.survivalcraft.launcher.utils;

public enum EnumConstants {

    LOGIN_TITLE("Connexion"),
    LOGIN_INFOS_1("Vos identifiants ne transitent pas par le réseau de SurvivalCraft.org"),
    LOGIN_MS("Connexion avec Microsoft"),
    LOGIN_USER("Utilisateur connecté: "),
    LOGIN_ERROR("Erreur de Connexion"),
    HOME_START_GAME("Jouer"),
    HOME_BTN("Accueil"),
    WINDOW_TITLE("SurvivalCraft - Launcher"),
    VERSION("1.0");

    private String text;
    EnumConstants(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}