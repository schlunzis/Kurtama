package de.schlunzis.common.messages.authentication.login;

import de.schlunzis.common.messages.IClientMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements IClientMessage {

    private String email;
    private String password;

}