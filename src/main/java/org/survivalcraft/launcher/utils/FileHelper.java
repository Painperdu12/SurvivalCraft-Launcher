package org.survivalcraft.launcher.utils;

import fr.flowarg.flowcompat.Platform;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    public static File generateGamePath(String folderName) {
        Path path = null;

        switch(Platform.getCurrentPlatform()) {
            case WINDOWS -> path = Paths.get(System.getenv("APPDATA"));
            case MAC -> path = Paths.get(System.getProperty("user.home"), "/Library/Application Support");
            case LINUX -> path = Paths.get(System.getProperty("user.home"), ".local/share");
            default -> Paths.get(System.getProperty("user.home"));
        }

        path = Paths.get(path.toString(), folderName);

        return path.toFile();
    }

}