package client;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

public class ClientConnection {
    private final Socket client;
    private LocalTime submissionTime;
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

    public LocalTime getSubmissionTime() {
        return this.submissionTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
        this.submissionTime = LocalTime.now();
    }

    public boolean hasSubmitted() {
        return this.code != null;
    }

    public void close() throws IOException {
        this.client.close();
    }
}
