package org.schlunzis.kurtama.common;

import java.util.UUID;

public record LobbyInfo(UUID lobbyID, String lobbyName, int users) {
}
