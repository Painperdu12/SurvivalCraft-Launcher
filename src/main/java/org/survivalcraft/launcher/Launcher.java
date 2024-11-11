package org.survivalcraft.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.survivalcraft.launcher.ui.panels.MenuSidePanel;
import org.survivalcraft.launcher.utils.EnumConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Launcher extends Application {

    private static Launcher instance;
    private final Path launcherDir = GameDirGenerator.createGameDir("survivalcraft", true);
    private final ILogger logger;
    private final Saver saver;
    private AuthInfos authInfos = null;
    private PanelManager manager;

    public Launcher()  {
        instance = this;
        this.logger = new Logger("[SurvivalCraft/Launcher]", launcherDir.resolve("launcher.log"));

        if(!launcherDir.toFile().exists()) {
            if(!launcherDir.toFile().mkdir()) this.logger.err("Unable to create launcher folder ! (Launcher.java)");
        }

        this.saver = new Saver(launcherDir.resolve("data.properties"));
        saver.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Starting " + EnumConstants.WINDOW_TITLE.getText() + " version " + EnumConstants.VERSION.getText());

        this.manager = new PanelManager(this, primaryStage);
        manager.init();

        if(isUserAlreadyLoggedIn()) manager.showPanel(new MenuSidePanel());
        else manager.showPanel(new MenuSidePanel());

    }

    public boolean isUserAlreadyLoggedIn() {
        if(saver.get("msAccessToken") != null && saver.get("msRefreshToken") != null) {
            try {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult response = authenticator.loginWithRefreshToken(saver.get("msRefreshToken"));

                saver.set("msAccessToken", response.getAccessToken());
                saver.set("msRefreshToken", response.getRefreshToken());

                setAuthInfos(new AuthInfos(response.getProfile().getName(), response.getAccessToken(), response.getProfile().getId()));

                return true;
            } catch(MicrosoftAuthenticationException e) {
                e.printStackTrace();

                logger.err(e.getMessage());
                saver.remove("msAccessToken");
                saver.remove("msRefreshToken");
            }
        }

        return false;
    }

    @Override
    public void stop() {
        Platform.exit();
        Main.exit();
    }

    public void hideWindow() {
        manager.getStage().hide();
    }

    public static Launcher getInstance() {
        return instance;
    }

    public ILogger getLogger() {
        return this.logger;
    }

    public Saver getSaver() {
        return this.saver;
    }

    public AuthInfos getAuthInfos() {
        return this.authInfos;
    }

    public void setAuthInfos(AuthInfos authInfos) {
        this.authInfos = authInfos;
    }

    public Path getLauncherDir() {
        return this.launcherDir;
    }
}