package org.schlunzis.kurtama.common.messages.authentication.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessfulResponse implements IServerMessage {

    private IUser user;

    private String email;

    private Collection<LobbyInfo> lobbyInfos;

}
