package org.schlunzis.kurtama.server.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.server.net.ISession;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEvent {

    private LoginRequest loginRequest;
    private ISession session;

}
