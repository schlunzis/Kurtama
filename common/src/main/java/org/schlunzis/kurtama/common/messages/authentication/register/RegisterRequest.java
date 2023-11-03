package org.schlunzis.kurtama.common.messages.authentication.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IClientMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest implements IClientMessage {

    private String username;
    private String email;
    private String password;

}
