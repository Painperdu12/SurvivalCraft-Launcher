package org.survivalcraft.launcher.ui.base;

import fr.flowarg.flowlogger.ILogger;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.survivalcraft.launcher.Launcher;
import org.survivalcraft.launcher.PanelManager;

public abstract class Panel implements IPanel, IMovable, ITakePlace {

    protected final ILogger logger;
    protected GridPane layout = new GridPane();
    protected PanelManager manager;

    public Panel() {
        this.logger = Launcher.getInstance().getLogger();
    }

    @Override
    public void init(PanelManager manager) {
        this.manager = manager;
        setCanTakeAllSize(layout);
    }

    @Override
    public GridPane getLayout() {
        return this.layout;
    }

    @Override
    public abstract String getName();

    @Override
    public String getStyleSheetPath() {
        return null;
    }

    @Override
    public void onShow() {
        FadeTransition transition = new FadeTransition(Duration.seconds(1), layout);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setAutoReverse(true);
        transition.play();
    }

    @Override
    public void setRight(Node node) {
        GridPane.setHalignment(node, HPos.RIGHT);
    }

    @Override
    public void setLeft(Node node) {
        GridPane.setHalignment(node, HPos.LEFT);
    }

    @Override
    public void setTop(Node node) {
        GridPane.setValignment(node, VPos.TOP);
    }

    @Override
    public void setBottom(Node node) {
        GridPane.setValignment(node, VPos.BOTTOM);
    }

    @Override
    public void setBaseLine(Node node) {
        GridPane.setValignment(node, VPos.BASELINE);
    }

    @Override
    public void setCenterH(Node node) {
        GridPane.setHalignment(node, HPos.CENTER);
    }

    @Override
    public void setCenterV(Node node) {
        GridPane.setValignment(node, VPos.CENTER);
    }
}