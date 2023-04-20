package de.schlunzis.common.messages.authentication;

import de.schlunzis.common.messages.ClientMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements ClientMessage {

    private String email;
    private String password;

}
