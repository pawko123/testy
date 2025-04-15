package org.example.mastermind;

public class CodeCheckException extends RuntimeException {
    public CodeCheckException(String message) {
      super(message);
    }

    public CodeCheckException(String message, Throwable cause) {
      super(message, cause);
    }
}
