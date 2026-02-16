package com.jodexindustries.jguiwrapper.paper.exception;

/**
 * Exception thrown when there is a version incompatibility or unsupported version detected in JGuiWrapper.
 * <p>
 * This exception is typically used to indicate that the current environment or dependency version does not meet
 * the requirements for running JGuiWrapper, or when a version conflict is encountered during runtime.
 */
@SuppressWarnings({"unused"})
public class JGuiWrapperVersionException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public JGuiWrapperVersionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public JGuiWrapperVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
