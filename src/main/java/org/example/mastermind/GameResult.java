package org.example.mastermind;

import java.time.LocalDateTime;

public class GameResult {
    private final int attempts;
    private final String code;
    private final LocalDateTime timestamp;

    public GameResult(int attempts, String code, LocalDateTime timestamp) {
        this.attempts = attempts;
        this.code = code;
        this.timestamp = timestamp;
    }

    public String getCode(){
        return code;
    }

    public int getAttempts() {
        return attempts;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
