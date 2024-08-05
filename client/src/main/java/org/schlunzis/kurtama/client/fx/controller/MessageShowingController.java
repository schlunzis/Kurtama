package org.schlunzis.kurtama.client.fx.controller;

import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeMessage;

import java.util.List;

public interface MessageShowingController {

    void showMessages(List<SceneChangeMessage> messages);

}
