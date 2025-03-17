package server;

import com.google.gson.Gson;
import service.LoginRequest;
import service.LoginResult;
import service.RegisterRequest;
import service.RegisterResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }
    public RegisterResult register(RegisterRequest request) {
        return null;//TODO
    }
    public LoginResult login(LoginRequest request) {
        return null;//TODO
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {//throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            //writeBody(request, http); //TODO
            http.connect();
            //throwIfNotSuccessful(http);
        // Output the response body
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
            }
//            return readBody(http, responseClass);
//        } catch (ResponseException ex) {
//            throw ex;
        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
        }
        return null; //TODO
    }
}
