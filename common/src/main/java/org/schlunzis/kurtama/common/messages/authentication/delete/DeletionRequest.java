package org.schlunzis.kurtama.common.messages.authentication.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IClientMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeletionRequest implements IClientMessage {

    private String password;

}
