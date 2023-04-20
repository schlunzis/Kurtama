package de.schlunzis.common.messages.authentication;

import de.schlunzis.common.User;
import de.schlunzis.common.messages.ServerMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessfulResponse implements ServerMessage {

    private User user;

}
