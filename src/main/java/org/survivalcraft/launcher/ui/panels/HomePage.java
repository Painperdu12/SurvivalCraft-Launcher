package org.survivalcraft.launcher.ui.panels;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.forge.ForgeVersion;
import fr.flowarg.flowupdater.versions.forge.ForgeVersionBuilder;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.survivalcraft.launcher.Launcher;
import org.survivalcraft.launcher.PanelManager;
import org.survivalcraft.launcher.ui.base.ContentPanel;
import org.survivalcraft.launcher.utils.EnumConstants;
import org.survivalcraft.launcher.utils.EnumSetupStep;
import org.survivalcraft.launcher.utils.GameConstants;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class HomePage extends ContentPanel {

    private Saver saver = Launcher.getInstance().getSaver();

    private GridPane boxPane = new GridPane();
    private ProgressBar progressBar = new ProgressBar();
    private Label stepLabel = new Label();
    private Label fileLabel = new Label();

    boolean isDownloading = false;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStyleSheetPath() {
        return "css/home.css";
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setMinHeight(75);
        rowConstraints.setMaxHeight(75);
        this.layout.getRowConstraints().addAll(rowConstraints, new RowConstraints());

        boxPane.getStyleClass().add("box-pane");
        boxPane.setPadding(new Insets(20));
        this.setCanTakeAllSize(boxPane);
        this.layout.add(boxPane, 0, 0);

        this.layout.getStyleClass().add("home-layout");

        progressBar.getStyleClass().add("download-progress");
        stepLabel.getStyleClass().add("download-status");
        fileLabel.getStyleClass().add("download-status");

        progressBar.setTranslateY(-15);
        this.setCenterH(progressBar);
        this.setCanTakeAllSize(progressBar);

        stepLabel.setTranslateY(5);
        this.setCenterH(stepLabel);
        this.setCanTakeAllSize(stepLabel);

        fileLabel.setTranslateY(20);
        this.setCenterH(fileLabel);
        this.setCanTakeAllSize(fileLabel);

        showPlayButton();
    }

    private void showPlayButton() {
        boxPane.getChildren().clear();

        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.GAMEPAD);
        iconView.getStyleClass().add("play-icon");

        Button playBtn = new Button(EnumConstants.HOME_START_GAME.getText());
        playBtn.getStyleClass().add("play-btn");
        playBtn.setGraphic(iconView);
        playBtn.setOnMouseClicked(e -> {
            play();
        });

        this.setCanTakeAllSize(playBtn);
        this.setCenterH(playBtn);
        this.setCenterV(playBtn);
        boxPane.getChildren().add(playBtn);
    }

    private void play() {
        isDownloading = true;
        boxPane.getChildren().clear();
        setProgress(0, 0);
        boxPane.getChildren().addAll(progressBar, stepLabel, fileLabel);

        Thread updateThread = new Thread(this::update);
        updateThread.setName("Game Update Thread");
        updateThread.start();
        Platform.runLater(updateThread);
    }

    public void update() {
        IProgressCallback callback = new IProgressCallback() {
            private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            private String stepTxt = "";
            private String percentTxt = "0.0%";

            @Override
            public void onFileDownloaded(Path path) {
                Platform.runLater(() -> {
                    String p = path.toString();
                    fileLabel.setText("..." + p.replace(Launcher.getInstance().getLauncherDir().toFile().getAbsolutePath(), ""));
                });
            }

            @Override
            public void step(Step step) {
                Platform.runLater(() -> {
                    stepTxt = EnumSetupStep.valueOf(step.name()).getDetails();
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                });
            }

            @Override
            public void update(DownloadList.DownloadInfo info) {
                Platform.runLater(() -> {
                    percentTxt = decimalFormat.format(info.getDownloadedBytes() * 100.d / info.getTotalToDownloadBytes());
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                });
            }
        };

        try {
            final VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName(GameConstants.GAME_VERSION).build();

            final ForgeVersion forgeVersion = new ForgeVersionBuilder()
                    .withForgeVersion(GameConstants.FORGE_VERSION)
                    .withMods(GameConstants.MOD_LIST_URL)
                    .withFileDeleter(new ModFileDeleter(true, "jei.jar"))
                    .build();

            final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanillaVersion)
                    .withModLoaderVersion(forgeVersion)
                    .withLogger(Launcher.getInstance().getLogger())
                    .withProgressCallback(callback)
                    .withUpdaterOptions(new UpdaterOptions.UpdaterOptionsBuilder().build())
                    .build();

            //updater.update(Launcher.getInstance().getLauncherDir());
            startGame(updater.getVanillaVersion().getName());

        } catch(Exception e) {
            Launcher.getInstance().getLogger().err(e.getMessage());
            e.printStackTrace();
        }
    }

    public void startGame(String gameVersion) {
        try {
            NoFramework noFramework = new NoFramework(Launcher.getInstance().getLauncherDir(), Launcher.getInstance().getAuthInfos(), GameFolder.FLOW_UPDATER_1_19_SUP);
            noFramework.getAdditionalArgs().add(getRamArgs());

            Process process = noFramework.launch(gameVersion, GameConstants.FORGE_VERSION.split("-")[1], NoFramework.ModLoader.FORGE);

            Platform.runLater(() -> {
                try {
                    process.waitFor();
                    Platform.exit();

                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setStatus(String s) {
        stepLabel.setText(s);
    }

    public String getRamArgs() {
        return "-Xmx2048M";
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setProgress(double current, double max) {
        progressBar.setProgress(current / max);
    }
}