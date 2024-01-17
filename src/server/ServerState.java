package server;

/**
 * Constants for specifying the internal state of the {@link HostServer}.
 * @author Harry Xu
 * @version 1.0 - December 27th 2023
 */
public enum ServerState {
    ACCEPTING,
    CORRESPONDING,
    CLOSED,
}