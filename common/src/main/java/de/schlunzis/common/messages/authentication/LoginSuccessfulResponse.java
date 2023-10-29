package de.schlunzis.common.messages.authentication;

import de.schlunzis.common.IUser;
import de.schlunzis.common.messages.IServerMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessfulResponse implements IServerMessage {

    private IUser user;

}
