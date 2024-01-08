package client;

import java.net.Socket;

public class ClientConnection {
    private final Socket client;
    private String name;
    private String code;

    public ClientConnection(Socket client) {
        this.client = client;
    }

    public Socket getSocket() {
        return this.client;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
