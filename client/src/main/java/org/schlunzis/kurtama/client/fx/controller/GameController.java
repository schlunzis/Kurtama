package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.fx.TilePane;
import org.schlunzis.kurtama.client.service.IGameService;
import org.schlunzis.kurtama.common.game.model.IGameStateDTO;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("game.fxml")
@Component
@RequiredArgsConstructor
public class GameController {

    private final IGameService gameService;

    @FXML
    private GridPane terrainGrid;

    @FXML
    private void initialize() {
        log.info("Initializing GameController");
        gameService.getGameState().addListener((observable, oldValue, newValue) -> updateTerrain(newValue));
        IGameStateDTO gameState = gameService.getGameState().get();
        if (gameState != null) {
            updateTerrain(gameState);
        }
        terrainGrid.setGridLinesVisible(true);
    }

    private void updateTerrain(IGameStateDTO gameState) {
        log.info("Updating terrain");
        final int percentHeight = 100 / gameState.terrain().height();
        final int percentWidth = 100 / gameState.terrain().width();
        terrainGrid.getRowConstraints().clear();
        terrainGrid.getColumnConstraints().clear();
        for (int i = 0; i < gameState.terrain().width(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(percentWidth);
            terrainGrid.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < gameState.terrain().height(); i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(percentHeight);
            terrainGrid.getRowConstraints().add(rowConstraints);
        }
        for (int x = 0; x < gameState.terrain().width(); x++) {
            for (int y = 0; y < gameState.terrain().height(); y++) {
                terrainGrid.add(new TilePane(gameState.terrain().tiles()[x][y]), x, y);
            }
        }
    }

}
