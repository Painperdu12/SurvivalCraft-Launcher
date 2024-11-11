package org.survivalcraft.launcher.ui.base;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class ContentPanel extends Panel {

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void onShow() {
        FadeTransition transition = new FadeTransition(Duration.millis(500), this.layout);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setAutoReverse(true);
        transition.play();
    }
}