package org.schlunzis.kurtama.client.fx.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.client.util.I18n;

import java.util.Objects;

public class StatusView extends HBox {

    public static final String STYLE_CLASS = "status-view";
    public static final String ICON_STYLE_CLASS = "status-icon";
    private static final int PROGRESS_ICON_SIZE = 30;

    @Setter
    private I18n i18n;

    private final Region statusIcon = new Region();
    private final Label statusLabel = new Label();

    private final Rotate progressRotate;
    private final Timeline progressTimeline;

    public StatusView() {
        super();
        this.getChildren().add(statusIcon);
        this.getChildren().add(statusLabel);

        this.getStyleClass().add(STYLE_CLASS);
        statusIcon.getStyleClass().add(ICON_STYLE_CLASS);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("status-view.css")).toExternalForm());
        progressRotate = new Rotate(0, PROGRESS_ICON_SIZE / 2d, PROGRESS_ICON_SIZE / 2d);
        progressTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressRotate.angleProperty(), 0d)),
                new KeyFrame(Duration.millis(2000), new KeyValue(progressRotate.angleProperty(), 360d))
        );
        statusIcon.getTransforms().add(progressRotate);
        progressTimeline.setCycleCount(Animation.INDEFINITE);
    }

    public void setStatus(Status status) {
        if (i18n == null)
            throw new IllegalStateException("I18n must be set before setting status");

        progressTimeline.stop();
        progressRotate.setAngle(0);
        if (status.getType() == StatusType.PROGRESS)
            progressTimeline.play();
        statusLabel.textProperty().bind(i18n.createBinding(status.getI18nKey()));
        statusIcon.getStyleClass().clear();
        statusIcon.getStyleClass().addAll(ICON_STYLE_CLASS, status.getType().getStyleClass());
    }

    public interface Status {
        StatusType getType();

        String getI18nKey();
    }

    @Getter
    @RequiredArgsConstructor
    public enum StatusType {
        INFO("info"),
        PROGRESS("progress"),
        WARNING("warning"),
        ERROR("error"),
        SUCCESS("success");

        private final String styleClass;
    }

}

