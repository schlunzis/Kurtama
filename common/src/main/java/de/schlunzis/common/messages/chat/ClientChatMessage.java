package de.schlunzis.common.messages.chat;

import de.schlunzis.common.messages.IClientMessage;
import lombok.*;

import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ClientChatMessage implements IClientMessage, IChatMessage {

    private UUID chatID;
    private String nickname;
    private String message;

}
