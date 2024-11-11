package org.survivalcraft.launcher;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import fr.flowarg.flowcompat.Platform;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.survivalcraft.launcher.ui.base.IPanel;
import org.survivalcraft.launcher.ui.panels.TopBar;
import org.survivalcraft.launcher.utils.EnumConstants;

public class PanelManager {

    private final Launcher launcher;
    private final Stage stage;
    private GridPane layout;
    private final TopBar topBar = new TopBar();
    private final GridPane contentPane = new GridPane();

    public PanelManager(Launcher launcher, Stage stage) {
        this.launcher = launcher;
        this.stage = stage;
    }

    public void init() {
        stage.setTitle(EnumConstants.WINDOW_TITLE.getText());
        stage.setWidth(840);
        stage.setHeight(560);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("assets/icon.png"));

        layout = new GridPane();

        if(Platform.isOnLinux()) {
            Scene scene = new Scene(layout);
            stage.setScene(scene);
        } else {
            stage.initStyle(StageStyle.UNDECORATED);

            BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, layout);
            scene.setResizable(false);
            scene.setMoveControl(topBar.getLayout());
            scene.removeDefaultCSS();

            stage.setScene(scene);
            stage.show();
            layout.add(contentPane, 0, 1);

            RowConstraints topBarConstraints = new RowConstraints();
            topBarConstraints.setValignment(VPos.TOP);
            topBarConstraints.setMinHeight(25);
            topBarConstraints.setMaxHeight(40);

            layout.getRowConstraints().addAll(topBarConstraints, new RowConstraints());

            topBar.init(this);
        }

        layout.add(topBar.getLayout(), 0, 0);
        GridPane.setVgrow(contentPane, Priority.ALWAYS);
        GridPane.setHgrow(contentPane, Priority.ALWAYS);

        stage.show();
    }

    public void showPanel(IPanel panel) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(panel.getLayout());

        if(panel.getStyleSheetPath() != null) {
            stage.getScene().getStylesheets().clear();
            stage.getScene().getStylesheets().add(panel.getStyleSheetPath());
        }

        panel.init(this);
        panel.onShow();
    }

    public Stage getStage() {
        return this.stage;
    }
}