package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<String, Connection> connections = new HashMap<>();

    public void add(String auth, Session session, int id) {
        System.out.println("user " + auth);
        var connection = new Connection(auth, session, id);
        connections.put(auth, connection);
    }

    public void remove(String auth) {
        connections.remove(auth);
    }

    public void removeAll() {
        connections.clear();
    }
    public boolean hasConnection(String auth) {
        System.out.println(auth);
        System.out.println(connections.get(auth).auth);
        return connections.containsKey(auth);
    }

    public void broadcast(String excludeAuth, ServerMessage notification, int id) throws IOException, NullPointerException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.auth.equals(excludeAuth) && (c.gameid == id)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.auth);
        }
    }

    public void broadcastOne(String auth, ServerMessage notification) throws IOException {
        var c = connections.get(auth);
        if(c.session.isOpen()) {
            c.send(new Gson().toJson(notification));
        } else {
            connections.remove(c.auth);
        }
    }

    public void broadcastSession(Session s, ServerMessage notification) throws IOException {
        s.getRemote().sendString(new Gson().toJson(notification));
    }
}
