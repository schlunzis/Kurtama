package de.schlunzis.common.messages.chat;

import de.schlunzis.common.messages.ClientMessage;
import lombok.*;

import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ClientChatMessage implements ClientMessage, IChatMessage {

    private UUID chatID;
    private String nickname;
    private String message;

}
