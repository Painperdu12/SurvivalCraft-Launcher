package org.survivalcraft.launcher.utils;

public enum EnumSetupStep {

    INTEGRATION("Integration des fichiers..."),
    MOD_PACK("Configuration des mods..."),
    MOD_LOADER("Configuration du Forge Mod Loader (Patientez)..."),
    READ("Lecture du fichier json..."),
    DL_LIBS("Téléchargement des librairies nécessaires..."),
    DL_RESOURCES("Téléchargement des resources nécessaires..."),
    DL_ASSETS("Téléchargement des resources nécessaires..."),
    MODS("Téléchargement des mods..."),
    EXTERNAL_FILES("Téléchargement des fichiers externes..."),
    EXTRACT_NATIVES("Extraction des natives..."),
    FORGE("Initialisation de Forge..."),
    CHECK_CHEAT("Vérification de la validité du client..."),
    POST_EXECUTIONS("Exécution post-initialisation..."),
    END("Fin du processus d'installation (Le jeu se lance).");

    private String details;
    EnumSetupStep(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}