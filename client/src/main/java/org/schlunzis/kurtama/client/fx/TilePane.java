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

    public TilePane(ITileDTO tileDTO, EdgeDTO[] edges, IGameService gameService) {
        Label label = new Label();
        label.setText(tileDTO.id() + " " + Arrays.stream(edges).reduce("", (acc, edge) -> acc + edge.secondTileIndex() + ", ", String::concat));
        getChildren().add(label);
        StringBuilder styleBuilder = new StringBuilder();
        if (!tileDTO.figures().isEmpty()) {
            styleBuilder.append("-fx-background-color: ").append(tileDTO.figures().getFirst().color().name().toLowerCase()).append(";");
        }
        styleBuilder.append("-fx-border-color: black;");
        setStyle(styleBuilder.toString());

        this.setOnMouseClicked(event -> {
            log.info("Tile {} clicked", tileDTO.id());
            gameService.sendMoveRequest(tileDTO.id());
        });
    }

}
