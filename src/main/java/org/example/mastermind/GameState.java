package org.example.mastermind;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    private String secretCode;
    private int maxAttempts;
    private int currentAttempt;
    private boolean isGameOver;
    private boolean isGameWon;
    private DatabaseService databaseService;
    private CodeService codeChecker;

    public GameState(int codeLength, int maxAttempts, DatabaseService databaseService, CodeService codeChecker) {
        this.secretCode = codeChecker.generateCode(codeLength);
        this.maxAttempts = maxAttempts;
        this.currentAttempt = 0;
        this.isGameOver = false;
        this.isGameWon = false;
        this.databaseService = databaseService;
        this.codeChecker = codeChecker;
    }

    public Guess makeGuess(String guess){
        if (isGameOver) {
            throw new IllegalStateException("Game is already over");
        }

        if (guess.length() != secretCode.length()) {
            throw new IllegalArgumentException("Guess must be " + secretCode.length() + " characters long");
        }

        currentAttempt++;

        try {
            Guess result = codeChecker.checkGuess(secretCode, guess);

            if (result.isCorrect()) {
                isGameWon = true;
                isGameOver = true;
                saveGameResult(currentAttempt);
            } else if (currentAttempt >= maxAttempts) {
                isGameOver = true;
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error checking guess", e);
        }
    }

    private void saveGameResult(int attempts) {
        try {
            LocalDateTime date = LocalDateTime.now();
            GameResult result = new GameResult(attempts, secretCode, date);
            databaseService.saveGameResult(result);
        } catch (Exception e) {
            System.err.println("Failed to save game result: " + e.getMessage());
        }
    }

    public List<GameResult> getTopResults() {
        try {
            return databaseService.getTopResults(3);
        } catch (Exception e) {
            System.err.println("Failed to retrieve top results: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String getSecretCode() {
        return secretCode;
    }

    public int getCurrentAttempt() {
        return currentAttempt;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return isGameWon;
    }
}
