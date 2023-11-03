package org.schlunzis.kurtama.common.messages.authentication.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IClientMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements IClientMessage {

    private String email;
    private String password;

}
