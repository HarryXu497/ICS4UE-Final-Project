package function;

import java.io.IOException;

/**
 * Represents a callback function to connect to a server socket.
 * This is a {@link FunctionalInterface} whose
 * functional method is {@link #connect(String, int, String)}.
 * @author Harry Xu
 * @version 1.0 - December 28th 2023
 */
@FunctionalInterface
public interface ConnectConsumer {
    /**
     * connect
     * Attempts to connect to the socket.
     * @param ip the ip address of the host socket
     * @param port the port of the host socket
     * @param name the name of the client socket
     * @throws IOException if an exception occurs whilst trying to connect to the socket
     */
    void connect(String ip, int port, String name) throws IOException;
}
