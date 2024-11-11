package org.survivalcraft.launcher.ui.base;

import javafx.scene.layout.GridPane;
import org.survivalcraft.launcher.PanelManager;

public interface IPanel {

    void init(PanelManager manager);
    void onShow();
    GridPane getLayout();
    String getName();
    String getStyleSheetPath();

}