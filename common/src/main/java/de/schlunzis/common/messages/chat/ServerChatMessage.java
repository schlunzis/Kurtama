package de.schlunzis.common.messages.chat;

import de.schlunzis.common.User;
import de.schlunzis.common.messages.ServerMessage;
import lombok.*;

import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ServerChatMessage implements ServerMessage, IChatMessage {

    private UUID chatID;
    private String nickname;
    private User user;
    private String message;

}
