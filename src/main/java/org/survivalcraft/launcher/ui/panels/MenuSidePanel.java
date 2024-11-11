package org.survivalcraft.launcher.ui.panels;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.survivalcraft.launcher.Launcher;
import org.survivalcraft.launcher.PanelManager;
import org.survivalcraft.launcher.ui.base.ContentPanel;
import org.survivalcraft.launcher.ui.base.Panel;
import org.survivalcraft.launcher.utils.EnumConstants;

public class MenuSidePanel extends Panel {

    private GridPane loginCard = new GridPane();
    private Saver saver = Launcher.getInstance().getSaver();

    private Label userErrorLabel = new Label();
    private Button msLoginBtn = new Button();
    private Button homeBtn = new Button();

    private Node activeLink = null;
    private ContentPanel currentPage = null;
    private GridPane navContent = new GridPane();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStyleSheetPath() {
        return "css/main.css";
    }

    @Override
    public void onShow() {
        super.onShow();
        setPage(new HomePage(), homeBtn);
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        //BACKGROUND
        this.layout.getStyleClass().add("login-layout");

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.RIGHT);
        columnConstraints.setMinWidth(350);
        columnConstraints.setMaxWidth(350);

        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());
        this.layout.add(loginCard, 0, 0);

        //BG IMAGE
        GridPane bgImage = new GridPane();
        this.setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 1, 0);

        //LOGIN CARD
        this.setCanTakeAllSize(this.layout);
        this.setLeft(loginCard);
        this.setCenterH(loginCard);
        this.setCenterV(loginCard);
        loginCard.getStyleClass().add("login-card");

        //SIDEBAR DE LOGIN MS
        Label title = new Label(EnumConstants.LOGIN_TITLE.getText());
        title.setFont(Font.font(title.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, 30f));
        title.getStyleClass().add("login-title");
        title.setTextAlignment(TextAlignment.CENTER);
        this.setCenterV(title);
        this.setCanTakeAllSize(title);
        this.setTop(title);
        loginCard.getChildren().add(title);

        Label infos = new Label(EnumConstants.LOGIN_INFOS_1.getText());
        infos.setFont(Font.font(infos.getFont().getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, 10f));
        infos.getStyleClass().add("login-infos");
        this.setCanTakeAllSize(infos);
        this.setBottom(infos);
        this.setCenterH(infos);
        loginCard.getChildren().add(infos);

        //ERREUR UTILISATEUR
        this.setCanTakeAllSize(userErrorLabel);
        this.setCenterV(userErrorLabel);
        userErrorLabel.getStyleClass().add("login-error");
        userErrorLabel.setTranslateY(10d);
        userErrorLabel.setMaxWidth(280d);
        userErrorLabel.setTextAlignment(TextAlignment.LEFT);
        loginCard.getChildren().add(userErrorLabel);

        //CONNEXION MS
        Label loginMs = new Label(EnumConstants.LOGIN_MS.getText());
        loginMs.setFont(Font.font(loginMs.getFont().getFamily(), FontWeight.SEMI_BOLD, FontPosture.REGULAR, 25f));
        loginMs.getStyleClass().add("login-label");
        loginMs.setTextAlignment(TextAlignment.CENTER);
        loginMs.setTranslateY(-80d);
        this.setCanTakeAllSize(loginMs);
        this.setCenterH(loginMs);
        loginCard.getChildren().add(loginMs);

        ImageView msImageBtn = new ImageView(new Image("assets/microsoft.png"));
        msImageBtn.setPreserveRatio(true);
        msImageBtn.setFitHeight(30d);
        msImageBtn.getStyleClass().add("login-ms-button");

        msLoginBtn.setMaxWidth(300);
        msLoginBtn.setGraphic(msImageBtn);
        msLoginBtn.setOnMouseClicked(e -> authenticateMS());
        this.setCanTakeAllSize(msLoginBtn);
        this.setCenterV(msLoginBtn);
        this.setCenterH(msLoginBtn);
        loginCard.getChildren().add(msLoginBtn);

        //Nav content
        this.layout.add(navContent, 1, 0);
        navContent.getStyleClass().add("nav-content");
        this.setLeft(navContent);
        this.setCenterH(navContent);
        this.setCenterV(navContent);

        //Home
        homeBtn.setText(EnumConstants.HOME_BTN.name());
        homeBtn.getStyleClass().add("sidemenu-nav-btn");
        homeBtn.setTranslateY(90d);
        homeBtn.setOnMouseClicked(e -> setPage(new HomePage(), homeBtn));

    }

    public void authenticateMS() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
            if(error != null) {
                Launcher.getInstance().getLogger().err(error.getMessage());
                error.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(EnumConstants.LOGIN_ERROR.getText());
                alert.setContentText(error.getMessage() + " (Error Code 2) \nPlease go to discord.survivalcraft.org");
                alert.show();

                return;
            }

            saver.set("msAccessToken", response.getAccessToken());
            saver.set("msRefreshToken", response.getRefreshToken());
            saver.save();

            Launcher.getInstance().setAuthInfos(new AuthInfos(response.getProfile().getName(), response.getAccessToken(), response.getProfile().getId()));
            this.logger.info("Connected with " + response.getProfile().getName());
         });
    }

    public void setPage(ContentPanel panel, Node navButton) {
        if(currentPage instanceof HomePage && ((HomePage)currentPage).isDownloading()) {
            return;
        }

        if(activeLink != null) activeLink.getStyleClass().remove("active");

        activeLink = navButton;
        activeLink.getStyleClass().add("active");

        this.navContent.getChildren().clear();
        if(panel != null) {

            this.navContent.getChildren().add(panel.getLayout());
            currentPage = panel;

            if(panel.getStyleSheetPath() != null) {

                this.manager.getStage().getScene().getStylesheets().clear();
                this.manager.getStage().getScene().getStylesheets().addAll(
                        this.getStyleSheetPath(),
                        panel.getStyleSheetPath()
                );
            }

            panel.init(this.manager);
            panel.onShow();
        }
    }
}