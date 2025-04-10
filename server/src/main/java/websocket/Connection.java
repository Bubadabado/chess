package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String auth;
    public Session session;
    public int gameid;

    public Connection(String auth, Session session, int gameid) {
        this.auth = auth;
        this.session = session;
        this.gameid = gameid;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
