package server;

/**
 * Pre-defined constants for more efficient communicating over TCP/IP sockets.
 * @author Harry Xu
 * @version 1.0 - December 27th 2023
 */
public enum ServerCode {
    HEARTBEAT,
    NEXT_SCREEN,
    DISCONNECT,
    SUBMISSION_FINISHED,
}
