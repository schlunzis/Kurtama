package de.schlunzis.common.messages.authentication.register;

import de.schlunzis.common.messages.IClientMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest implements IClientMessage {

    private String username;
    private String email;
    private String password;

}
