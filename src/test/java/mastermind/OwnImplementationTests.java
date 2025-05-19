package mastermind;


import org.example.mastermind.GameLoop;
import org.example.mastermind.GameResult;
import org.example.mastermind.GameState;
import org.example.mastermind.Guess;
import org.example.mastermind.myimplementation.CodeImplementation;
import org.example.mastermind.myimplementation.DatabaseImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OwnImplementationTests {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void testCodeGeneration(int length) {
        CodeImplementation codeService = new CodeImplementation();
        String code = codeService.generateCode(length);
        assertEquals(length, code.length());
        assertTrue(code.matches("[RGBYOP]{"+length+"}"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"RGGY", "YOPG", "ROPY"})
    public void testIncorrectCheckGuess(String guess) {
        CodeImplementation codeService = new CodeImplementation();
        String code = "RGBY";
        Guess result = codeService.checkGuess(code, guess);

        // Check the length of the result
        assertEquals(code.length(), result.getAnswer().length());

        // Check if the guess is incorrect
        assertFalse(result.isCorrect());

        // Check if the result contains only valid characters
        assertTrue(result.getAnswer().matches("[+-_]{"+code.length()+"}"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"RGBY", "YOPG", "ROPY"})
    public void testCorrectCheckGuess(String guess) {
        CodeImplementation codeService = new CodeImplementation();
        Guess result = codeService.checkGuess(guess, guess);

        // Check the length of the result
        assertEquals(guess.length(), result.getAnswer().length());

        // Check if the guess is correct
        assertTrue(result.isCorrect());

        // Check if the result contains only valid characters
        assertTrue(result.getAnswer().matches("[+]{"+guess.length()+"}"));
    }

    @Test
    public void testDatabaseSave(){
        DatabaseImplementation databaseService = new DatabaseImplementation();
        GameResult result = new GameResult(3,"RGBY", LocalDateTime.now());
        databaseService.saveGameResult(result);

        assertEquals(1, databaseService.getAllResults().size());
        assertEquals(result, databaseService.getAllResults().getFirst());
    }

    @Test
    public void testDatabaseLoadLessThanLimit(){
        DatabaseImplementation databaseService = new DatabaseImplementation();
        GameResult result1 = new GameResult(3,"RGBY", LocalDateTime.now());
        GameResult result2 = new GameResult(2,"YOPG", LocalDateTime.now());
        databaseService.saveGameResult(result1);
        databaseService.saveGameResult(result2);

        List<GameResult> results = databaseService.getTopResults(3);

        assertEquals(2, results.size());
        assertEquals(result2, results.getFirst());
        assertEquals(result1, results.getLast());
    }

    @Test
    public void testDatabaseLoadMoreThanLimit(){
        DatabaseImplementation databaseService = new DatabaseImplementation();
        GameResult result1 = new GameResult(3,"RGBY", LocalDateTime.now());
        GameResult result2 = new GameResult(2,"YOPG", LocalDateTime.now());
        GameResult result3 = new GameResult(1,"ROPY", LocalDateTime.now());
        databaseService.saveGameResult(result1);
        databaseService.saveGameResult(result2);
        databaseService.saveGameResult(result3);

        List<GameResult> results = databaseService.getTopResults(2);

        assertEquals(2, results.size());
        assertEquals(result3, results.getFirst());
        assertEquals(result2, results.getLast());
    }

    @Test
    public void testInsideGameLoopWin(){
        CodeImplementation codeService = new CodeImplementation();
        DatabaseImplementation databaseService = new DatabaseImplementation();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));



        GameLoop gameLoop = new GameLoop(new GameState(5,10,databaseService,codeService));

        String secret = gameLoop.getGame().getSecretCode();
        String simulatedInput = secret + "\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        gameLoop.startGame();


        List<GameResult> results = databaseService.getTopResults(2);

        assertTrue(gameLoop.getGame().isGameOver());
        assertTrue(gameLoop.getGame().isGameWon());
        assertTrue(outputStream.toString().contains("Congratulations! You guessed the code!"));

        assertEquals(1, results.size());

        assertTrue(outputStream.toString().contains("1. Won in 1 attempts, Code: "+secret));


        System.setIn(System.in);
        System.setOut(System.out);

    }

    @Test
    public void testInsideGameLoopLose(){
        CodeImplementation codeService = new CodeImplementation();
        DatabaseImplementation databaseService = new DatabaseImplementation();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        GameLoop gameLoop = new GameLoop(new GameState(5,1,databaseService,codeService));
        String secret = gameLoop.getGame().getSecretCode();
        String simulatedInput =   "RRRRR\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        gameLoop.startGame();

        List<GameResult> results = databaseService.getTopResults(2);

        assertTrue(gameLoop.getGame().isGameOver());
        assertFalse(gameLoop.getGame().isGameWon());
        assertTrue(outputStream.toString().contains("Game over! The secret code was: "+secret));
        assertFalse(outputStream.toString().contains("1. Won in 1 attempts, Code: "+secret));

        assertEquals(0, results.size());

        System.setIn(System.in);
        System.setOut(System.out);
    }
}
