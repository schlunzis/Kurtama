package de.schlunzis.server.auth;


import de.schlunzis.server.net.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutEvent {

    private Session session;

}
