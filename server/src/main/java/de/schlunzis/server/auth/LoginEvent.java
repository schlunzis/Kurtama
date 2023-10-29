package de.schlunzis.server.auth;

import de.schlunzis.common.messages.authentication.LoginRequest;
import de.schlunzis.server.net.ISession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEvent {

    private LoginRequest loginRequest;
    private ISession session;

}
