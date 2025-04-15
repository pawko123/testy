package org.example.mastermind;

public class CodeCreateException extends RuntimeException {
    public CodeCreateException(String message) {
        super(message);
    }

    public CodeCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
