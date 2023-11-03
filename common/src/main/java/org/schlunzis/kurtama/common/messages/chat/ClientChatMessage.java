package org.schlunzis.kurtama.common.messages.chat;

import lombok.*;
import org.schlunzis.kurtama.common.messages.IClientMessage;

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
