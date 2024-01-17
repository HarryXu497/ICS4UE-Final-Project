package loader;

/**
 * Indicates that submitted code cannot be compiled, loaded, or instantiated by the {@link ObjectLoader}
 * @author Harry Xu
 * @version 1.0 - January 10th 2024
 */
public class ObjectLoaderException extends Exception {
    /**
     * Constructs an {@link ObjectLoaderException}.
     */
    public ObjectLoaderException() {
        super();
    }

    /**
     * Constructs an {@link ObjectLoaderException} with a message.
     * @param message the error message
     */
    public ObjectLoaderException(String message) {
        super(message);
    }

    /**
     * Constructs an {@link ObjectLoaderException} with a cause.
     * @param cause the exception cause
     */
    public ObjectLoaderException(Throwable cause) {
        super(cause);
    }
}
