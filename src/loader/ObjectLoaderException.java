package loader;

/**
 * Indicates that submitted code cannot be compiled, loaded, or instantiated by the {@link ObjectLoader}
 * @author Harry Xu
 * @version 1.0 - January 10th 2024
 */
public class ObjectLoaderException extends Exception {
    public ObjectLoaderException() {
        super();
    }

    public ObjectLoaderException(String message) {
        super(message);
    }

    public ObjectLoaderException(Throwable cause) {
        super(cause);
    }
}
