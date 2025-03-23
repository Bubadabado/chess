package server;

import com.google.gson.Gson;
import model.GameData;
import service.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    //facade endpoints
    public RegisterResult register(RegisterRequest request) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }
    public LoginResult login(LoginRequest request) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class);
    }
    public String logout(LogoutRequest request) throws Exception {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, String.class, request.authToken());
    }
    public CreateGameResult createGame(CreateGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class, request.authToken());
    }
    public ListGameResult listGames(ListGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGameResult.class, request.authToken());
    }
    public String joinGame(JoinGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("PUT", path, request, String.class, request.authToken());
    }
    public GameData observeGame(JoinGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGameResult.class,
                request.authToken()).games().get(request.gameID());
    }
    public void clear() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, Object.class);
    }

    //make an http request
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw ex;
        }
    }
    //http request with auth and headers
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Authorization", auth);
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception { //ResponseException
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new Exception(respErr.toString());
                }
            }
            throw new Exception("other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
