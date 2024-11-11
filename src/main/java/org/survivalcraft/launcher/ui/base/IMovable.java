package org.survivalcraft.launcher.ui.base;

import javafx.scene.Node;

public interface IMovable {

    void setRight(Node node);
    void setLeft(Node node);
    void setTop(Node node);
    void setBottom(Node node);
    void setBaseLine(Node node);
    void setCenterH(Node node);
    void setCenterV(Node node);

}