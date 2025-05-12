package org.example.mastermind;

import java.util.List;
import java.util.Scanner;

public class GameLoop {
    private final GameState game;
    private Scanner scanner;

    public GameLoop(GameState game){
        this.game = game;
    }

    public GameLoop(GameState game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }

    public void startGame() {
        if(scanner == null) {
            this.scanner = new Scanner(System.in);
        }
        System.out.println("Welcome to Mastermind!");
        System.out.println("Try to guess the secret code. Valid colors are: R, G, B, Y, O, P");
        System.out.println("The code is " + game.getSecretCode().length() + " characters long.");
        System.out.println("You have " + game.getMaxAttempts() + " attempts.");

        while (!game.isGameOver()) {
            System.out.println("\nAttempt " + (game.getCurrentAttempt() + 1) + " of " + game.getMaxAttempts());
            System.out.print("Enter your guess: ");

            String guess = scanner.nextLine().toUpperCase().trim();

            try {
                Guess result = game.makeGuess(guess);

                System.out.println("Result: " + result.getAnswer() + " _ means not correct, + means correct, - means correct in wrong place");

                if (result.isCorrect()) {
                    System.out.println("Congratulations! You guessed the code!");
                } else if (game.isGameOver()) {
                    System.out.println("Game over! The secret code was: " + game.getSecretCode());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error processing guess: " + e.getMessage());
            }
        }

        displayTopResults();
    }

    private void displayTopResults() {
        System.out.println("\nTop 3 results:");
        List<GameResult> topResults = game.getTopResults();

        if (topResults.isEmpty()) {
            System.out.println("No results available");
        } else {
            for (int i = 0; i < topResults.size(); i++) {
                GameResult result = topResults.get(i);
                System.out.println((i + 1) + ". " +
                        "Won in " + result.getAttempts() + " attempts, " +
                        "Code: " + result.getCode());
            }
        }
    }

    public GameState getGame() {
        return game;
    }
}
