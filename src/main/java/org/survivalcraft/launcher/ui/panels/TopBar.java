package org.survivalcraft.launcher.ui.panels;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.survivalcraft.launcher.Main;
import org.survivalcraft.launcher.PanelManager;
import org.survivalcraft.launcher.ui.base.Panel;
import org.survivalcraft.launcher.utils.EnumConstants;


public class TopBar extends Panel {

    private GridPane topbar;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStyleSheetPath() {
        return null;
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);
        this.topbar = this.layout;

        this.layout.setStyle("-fx-background-color: rgb(40, 116, 166);");
        this.setCanTakeAllWidth(topbar);

        //GAUCHE
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("assets/icon.png"));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(40);
        this.setLeft(imageView);
        this.layout.getChildren().add(imageView);

        //CENTRE
        Label title = new Label(EnumConstants.WINDOW_TITLE.getText());
        title.setFont(Font.font(title.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, 20f));
        title.setStyle("-fx-text-fill: white;");
        this.setCenterH(title);
        this.layout.getChildren().add(title);

        //DROITE
        GridPane buttons = new GridPane();
        buttons.setMinWidth(100d);
        buttons.setMaxWidth(100d);
        this.setCanTakeAllSize(buttons);
        this.setRight(buttons);
        this.layout.getChildren().add(buttons);

        //CONFIG BOUTONS
        FontAwesomeIconView closeBtn = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_CLOSE);
        FontAwesomeIconView maximizeBtn = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MAXIMIZE);
        FontAwesomeIconView minimizeBtn = new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MINIMIZE);

        this.setCanTakeAllWidth(closeBtn, maximizeBtn, minimizeBtn);

        closeBtn.setFill(Color.WHITE);
        closeBtn.setOpacity(0.7f);
        closeBtn.setSize("18px");
        closeBtn.setOnMouseEntered(e -> closeBtn.setOpacity(1.0f));
        closeBtn.setOnMouseExited(e -> closeBtn.setOpacity(0.7f));
        closeBtn.setOnMouseClicked(e -> Main.exit());
        closeBtn.setTranslateX(70f);

        maximizeBtn.setFill(Color.WHITE);
        maximizeBtn.setOpacity(0.7f);
        maximizeBtn.setSize("14px");
        maximizeBtn.setOnMouseEntered(e -> maximizeBtn.setOpacity(1.0f));
        maximizeBtn.setOnMouseExited(e -> maximizeBtn.setOpacity(0.7f));
        maximizeBtn.setOnMouseClicked(e -> this.manager.getStage().setMaximized(!this.manager.getStage().isMaximized()));
        maximizeBtn.setTranslateX(50d);

        minimizeBtn.setFill(Color.WHITE);
        minimizeBtn.setOpacity(0.7f);
        minimizeBtn.setSize("18px");
        minimizeBtn.setOnMouseEntered(e -> minimizeBtn.setOpacity(1.0f));
        minimizeBtn.setOnMouseExited(e -> minimizeBtn.setOpacity(0.7f));
        minimizeBtn.setOnMouseClicked(e ->  this.manager.getStage().setIconified(true));
        minimizeBtn.setTranslateX(26.0f);
        minimizeBtn.setTranslateY(-2d);

        buttons.getChildren().addAll(closeBtn, maximizeBtn, minimizeBtn);
    }
}