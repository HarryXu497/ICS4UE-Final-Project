package function;

/**
 * Represents a function which takes no arguments and returns no results
 * This is a {@link FunctionalInterface} whose
 * functional method is {@link #execute()}.
 * @author Harry Xu
 * @version 1.0 - December 28th 2023
 */
@FunctionalInterface
public interface Procedure {
    /**
     * execute
     * runs the procedure
     */
    void execute();
}
