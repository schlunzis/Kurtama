package de.schlunzis.common.messages.chat;

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
    private String sender;
    private String message;

}
