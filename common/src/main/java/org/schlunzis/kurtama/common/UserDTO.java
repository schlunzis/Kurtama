package org.schlunzis.kurtama.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements IUser {

    private UUID id;

    private String username;

    private String email;

}
