package org.schlunzis.kurtama.client.fx;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.service.IGameService;
import org.schlunzis.kurtama.common.game.model.EdgeDTO;
import org.schlunzis.kurtama.common.game.model.ITileDTO;

import java.util.Arrays;

@Slf4j
public class TilePane extends AnchorPane {

    private final IGameService gameService;

    private final Label label = new Label();
    private ITileDTO tileDTO;
    private EdgeDTO[] edges;

    public TilePane(ITileDTO tileDTO, EdgeDTO[] edges, IGameService gameService) {
        this.tileDTO = tileDTO;
        this.edges = edges;
        this.gameService = gameService;
        label.setText(tileDTO.id() + " " + Arrays.stream(edges).reduce("", (acc, edge) -> acc + edge.secondTileIndex() + ", ", String::concat));
        getChildren().add(label);
        StringBuilder styleBuilder = new StringBuilder();
        if (!tileDTO.figures().isEmpty()) {
            styleBuilder.append("-fx-background-color: red;");
        }
        styleBuilder.append("-fx-border-color: black;");
        setStyle(styleBuilder.toString());

        this.setOnMouseClicked(event -> {
            log.info("Tile " + tileDTO.id() + " clicked");
            gameService.sendMoveRequest(tileDTO.id());
        });
    }

}
