package function;

import java.io.IOException;

/**
 * Represents a callback function to send code to the host socket.
 * This is a {@link FunctionalInterface} whose
 * functional method is {@link #submit(String)}.
 * @author Harry Xu
 * @version 1.0 - January 12th 2024
 */
public interface CodeSubmissionConsumer {
    /**
     * submit
     * Submits code to the host socket.
     * @param code the program source code.
     * @throws IOException if an exception occurs whilst trying to write to the socket
     */
    void submit(String code) throws IOException;
}
