package client;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

/**
 * Wraps a client socket without other client information.
 * @author Harry Xu
 * @version 1.0 - January 7th 2024
 */
public class ClientConnection {
    private final Socket client;
    private LocalTime submissionTime;
    private String name;
    private String code;

    /**
     * Creates a connection wrapping a client socket.
     * @param client the client socket
     */
    public ClientConnection(Socket client) {
        this.client = client;
    }

    /**
     * getSocket
     * Gets the socket wrapped by this connection.
     * @return the client socket
     */
    public Socket getSocket() {
        return this.client;
    }

    /**
     * getName
     * Gets the client's name.
     * @return the name of the client
     */
    public String getName() {
        return this.name;
    }

    /**
     * getCode
     * Gets the client's submitted code or
     * null if the client has not submitted.
     * @return the code of the client
     */
    public String getCode() {
        return this.code;
    }

    /**
     * getSubmissionTime
     * Gets the time of submission or
     * null if the client has not submitted.
     * @return the submission time of the client
     */
    public LocalTime getSubmissionTime() {
        return this.submissionTime;
    }

    /**
     * setName
     * Sets the name of the player.
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setCode
     * Sets the submitted code of the player.
     * @param code the new code of the player
     */
    public void setCode(String code) {
        this.code = code;
        this.submissionTime = LocalTime.now();
    }

    /**
     * deleteCode
     * Deletes the user submitted code.
     */
    public void deleteCode() {
        this.code = null;
        this.submissionTime = null;
    }

    /**
     * hasSubmitted
     * Returns if the client has submitted their code.
     * @return if the client has submitted
     */
    public boolean hasSubmitted() {
        return this.code != null;
    }

    /**
     * close
     * Closes the socket associated with this connection.
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void close() throws IOException {
        this.client.close();
    }
}
