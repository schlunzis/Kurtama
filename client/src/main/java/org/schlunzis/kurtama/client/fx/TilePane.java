package org.schlunzis.kurtama.client.fx;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.service.IGameService;
import org.schlunzis.kurtama.common.game.model.ITileDTO;

@Slf4j
public class TilePane extends AnchorPane {

    private final IGameService gameService;

    private final Label label = new Label();
    private ITileDTO tileDTO;

    public TilePane(ITileDTO tileDTO, IGameService gameService) {
        this.tileDTO = tileDTO;
        this.gameService = gameService;
        label.setText(String.valueOf(tileDTO.id()));
        getChildren().add(label);
        if (!tileDTO.figures().isEmpty())
            this.setStyle("-fx-background-color: red;");
        this.setOnMouseClicked(event -> {
            log.info("Tile " + tileDTO.id() + " clicked");
            gameService.sendMoveRequest(tileDTO.id());
        });
    }

}
