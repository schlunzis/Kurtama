package org.schlunzis.kurtama.client.fx;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.schlunzis.kurtama.common.game.model.ITileDTO;


public class TilePane extends AnchorPane {

    private final Label label = new Label();
    private ITileDTO tileDTO;

    public TilePane(ITileDTO tileDTO) {
        this.tileDTO = tileDTO;
        label.setText(String.valueOf(tileDTO.id()));
        getChildren().add(label);
    }

}
