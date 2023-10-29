package de.schlunzis.common.messages.chat;

import de.schlunzis.common.IUser;
import de.schlunzis.common.messages.IServerMessage;
import lombok.*;

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
