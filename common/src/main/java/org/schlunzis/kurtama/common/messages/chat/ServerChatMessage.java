package org.schlunzis.kurtama.common.messages.chat;

import lombok.*;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ServerChatMessage implements IServerMessage, IChatMessage {

    private UUID chatID;
    private String nickname;
    private IUser user;
    private String message;

}
