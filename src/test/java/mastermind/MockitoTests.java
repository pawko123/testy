package mastermind;

import org.example.mastermind.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.hamcrest.Matchers.*;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MockitoTests {
    // <editor-fold desc="GameState Tests">
    // <editor-fold desc="Code Service Test">
    @Test
    public void gamesWon() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess("ROPYG","ROPYG")).thenReturn(new Guess("+++++",true));

        DatabaseService databaseService = mock(DatabaseService.class);

        GameState gamestate = new GameState(5,10,databaseService,codeService);

        gamestate.makeGuess("ROPYG");

        assertTrue(gamestate.isGameWon());
        assertTrue(gamestate.isGameOver());
        assertEquals(1,gamestate.getCurrentAttempt());
    }

    @Test
    public void gameLost() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess("ROPYG","ROYYG")).thenReturn(new Guess("++-++",false));
        when(codeService.checkGuess("ROPYG","ROPYG")).thenReturn(new Guess("+++++",true));

        DatabaseService databaseService = mock(DatabaseService.class);

        GameState gamestate = new GameState(5,1,databaseService,codeService);

        gamestate.makeGuess("ROYYG");
        assertThrows(IllegalStateException.class, () -> {
            gamestate.makeGuess("ROPYG");
        });

        verify(codeService,times(1)).checkGuess(any(String.class),any(String.class));
        assertFalse(gamestate.isGameWon());
        assertTrue(gamestate.isGameOver());
        assertEquals(1,gamestate.getCurrentAttempt());
        verify(databaseService,times(0)).saveGameResult(any(GameResult.class));
    }
    @Test
    public void illegalArgumentGiven() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");

        DatabaseService databaseService = mock(DatabaseService.class);

        GameState gamestate = new GameState(5,10,databaseService,codeService);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gamestate.makeGuess("ROYYGG");
        });

        assertEquals("Guess must be 5 characters long", exception.getMessage());
    }
    // </editor-fold>

    // <editor-fold desc="Database Tests">

    @Test
    public void databeseSave() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess("ROPYG","ROPYG")).thenReturn(new Guess("+++++",true));

        DatabaseService databaseService = mock(DatabaseService.class);

        GameState gamestate = new GameState(5,10,databaseService,codeService);

        gamestate.makeGuess("ROPYG");

        verify(databaseService,times(1)).saveGameResult(any(GameResult.class));
    }

    @Test
    public void databaseLoad() throws Exception{
        CodeService codeService = mock(CodeService.class);

        DatabaseService databaseService = mock(DatabaseService.class);
        when(databaseService.getTopResults(3)).thenReturn(List.of(new GameResult(1,"ROPYG", LocalDateTime.now())));

        GameState gamestate = new GameState(5,10,databaseService,codeService);

        List<GameResult> results = gamestate.getTopResults();

        assertThat(results, Matchers.hasSize(1));
        assertThat(results.get(0).getCode(), Matchers.equalTo("ROPYG"));
    }

    @Test
    public void databaseLoadAndSave() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess("ROPYG","ROPYG")).thenReturn(new Guess("+++++",true));



        DatabaseService databaseService = mock(DatabaseService.class);
        List<GameResult> mockResults = new ArrayList<>();
        mockResults.add(new GameResult(2,"RRRRR", LocalDateTime.now()));

        doAnswer(invocation -> new ArrayList<>(mockResults))
                .when(databaseService).getTopResults(3);

        doAnswer(invocation -> {
            GameResult result = invocation.getArgument(0);
            mockResults.add(result);
            return null;
        }).when(databaseService).saveGameResult(any(GameResult.class));


        GameState gamestate = new GameState(5,10,databaseService,codeService);

        gamestate.makeGuess("ROPYG");
        //simulate game loop ending
        gamestate.getTopResults();

        verify(databaseService,times(1)).saveGameResult(any(GameResult.class));
        verify(databaseService,times(1)).getTopResults(3);

        List<GameResult> results = gamestate.getTopResults();

        assertThat(results, Matchers.hasSize(2));
        assertThat(results.get(0).getCode(), Matchers.equalTo("RRRRR"));
        assertThat(results.get(0).getAttempts(), Matchers.equalTo(2));
        assertThat(results.get(1).getCode(), Matchers.equalTo("ROPYG"));
        assertThat(results.get(1).getAttempts(), Matchers.equalTo(1));
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold desc="Exception Tests">
    @Test
    public void codeCreateExceptionTest() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenThrow(new CodeCheckException("Code generation failed"));

        Exception exception = assertThrows(CodeCheckException.class, () -> {
            codeService.generateCode(5);
        });

        assertEquals("Code generation failed", exception.getMessage());
    }

    @Test
    public void codeCheckExceptionTest() throws Exception{
        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess("ROPYG","ROPYGG")).thenThrow(new CodeCheckException("Invalid size of guess"));

        Exception exception = assertThrows(CodeCheckException.class, () -> {
            codeService.checkGuess("ROPYG","ROPYGG");
        });

        assertEquals("Invalid size of guess", exception.getMessage());
    }

    @Test
    public void databaseSaveExceptionTest() throws Exception{
        DatabaseService databaseService = mock(DatabaseService.class);
        doThrow(new DatabaseException("Database save failed"))
                .when(databaseService).saveGameResult(any(GameResult.class));

        Exception exception = assertThrows(DatabaseException.class, () -> {
            databaseService.saveGameResult(new GameResult(1,"ROPYG", LocalDateTime.now()));
        });

        assertEquals("Database save failed", exception.getMessage());
    }

    @Test
    public void databaseLoadExceptionTest() throws Exception{
        DatabaseService databaseService = mock(DatabaseService.class);
        when(databaseService.getTopResults(3)).thenThrow(new DatabaseException("Database load failed"));

        Exception exception = assertThrows(DatabaseException.class, () -> {
            databaseService.getTopResults(3);
        });

        assertEquals("Database load failed", exception.getMessage());
    }
    // </editor-fold>

    // <editor-fold desc="GameLoop Tests">
    @Test
    public void gameLoopWonTest() {
        PrintStream mockedPrintStream = mock(PrintStream.class);
        System.setOut(mockedPrintStream);
        String simulatedInput = "ROPYG\n";
        Scanner mockedScanner = mock(Scanner.class);
        when(mockedScanner.nextLine()).thenReturn(simulatedInput);


        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess(any(String.class),any(String.class))).
                thenReturn(new Guess("+++++",true));
        GameLoop gameLoop = new GameLoop(new GameState(5, 10,
                mock(DatabaseService.class), codeService), mockedScanner);

        gameLoop.startGame();
        // Verify the output
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedPrintStream, atLeastOnce()).println(captor.capture());
        List<String> capturedOutput = captor.getAllValues();

        //:TODO intercept output
        assertTrue(capturedOutput.contains("Welcome to Mastermind!"));
        assertTrue(capturedOutput.contains("Try to guess the secret code. Valid colors are: R, G, B, Y, O, P"));
        assertTrue(capturedOutput.contains("The code is 5 characters long."));
        assertTrue(capturedOutput.contains("You have 10 attempts."));

        verify(mockedScanner,times(1)).nextLine();
        assertTrue(capturedOutput.contains("Congratulations! You guessed the code!"));

        System.setOut(System.out);
    }

    @Test
    public void gameLoopLostTest() throws DatabaseException {
        PrintStream mockedPrintStream = mock(PrintStream.class);
        System.setOut(mockedPrintStream);
        String simulatedInput = "ROYYG\n";
        Scanner mockedScanner = mock(Scanner.class);
        when(mockedScanner.nextLine()).thenReturn(simulatedInput);

        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess(any(String.class),any(String.class))).
                thenReturn(new Guess("++-++",false));

        DatabaseService databaseService = mock(DatabaseService.class);


        GameLoop gameLoop = new GameLoop(new GameState(5, 2,
                databaseService, codeService),mockedScanner);

        gameLoop.startGame();

        // Verify the output
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedPrintStream, atLeastOnce()).println(captor.capture());
        List<String> capturedOutput = captor.getAllValues();

        assertTrue(capturedOutput.contains("Game over! The secret code was: ROPYG"));
        assertThat(gameLoop.getGame().isGameOver(), Matchers.equalTo(true));
        assertThat(gameLoop.getGame().isGameWon(), Matchers.equalTo(false));
        verify(mockedScanner,times(2)).nextLine();
        verify(codeService,times(1)).generateCode(5);
        verify(codeService,times(2)).checkGuess(any(String.class),any(String.class));
        verify(databaseService,times(0)).saveGameResult(any(GameResult.class));
        verify(databaseService,times(1)).getTopResults(3);

        System.setOut(System.out);

    }

    @Test
    public void gameLoopWithIllegalGuess(){
        PrintStream mockedPrintStream = mock(PrintStream.class);
        System.setOut(mockedPrintStream);
        Scanner mockedScanner = mock(Scanner.class);
        when(mockedScanner.nextLine()).thenReturn("ROYYGG","ROPYG");


        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess("ROPYG","ROPYG")).
                thenReturn(new Guess("+++++",true));
        DatabaseService databaseService = mock(DatabaseService.class);

        GameLoop gameLoop = new GameLoop(new GameState(5, 1,
                databaseService, codeService),mockedScanner);

        gameLoop.startGame();


        // Verify the output
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedPrintStream, atLeastOnce()).println(captor.capture());
        List<String> capturedOutput = captor.getAllValues();

        assertThat(gameLoop.getGame().isGameOver(), Matchers.equalTo(true));
        assertThat(gameLoop.getGame().isGameWon(), Matchers.equalTo(true));
        assertThat(gameLoop.getGame().getCurrentAttempt(), Matchers.equalTo(1));
        verify(mockedScanner,times(2)).nextLine();
        assertThat(capturedOutput.contains("Invalid input: Guess must be 5 characters long"), Matchers.equalTo(true));

        System.setOut(System.out);
    }

    @Test
    public void gameLoopWithDBExceptions() throws DatabaseException {
        PrintStream mockedPrintStream = mock(PrintStream.class);
        PrintStream outputStream = System.out;
        System.setOut(mockedPrintStream);
        String simulatedInput = "ROPYG\n";
        Scanner mockedScanner = mock(Scanner.class);
        when(mockedScanner.nextLine()).thenReturn(simulatedInput);

        DatabaseService databaseService = mock(DatabaseService.class);
        when(databaseService.getTopResults(any(Integer.class))).thenThrow(new DatabaseException("Database load failed"));
        doThrow(new DatabaseException("Database save failed"))
                .when(databaseService).saveGameResult(any(GameResult.class));


        CodeService codeService = mock(CodeService.class);
        when(codeService.generateCode(5)).thenReturn("ROPYG");
        when(codeService.checkGuess(any(String.class),any(String.class))).
                thenReturn(new Guess("+++++",true));
        GameLoop gameLoop = new GameLoop(new GameState(5, 10,
                databaseService, codeService), mockedScanner);

        gameLoop.startGame();
        // Verify the output
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedPrintStream, atLeastOnce()).println(captor.capture());
        List<String> capturedOutput = captor.getAllValues();

        System.setOut(outputStream);

        assertTrue(capturedOutput.contains("Welcome to Mastermind!"));
        assertTrue(capturedOutput.contains("Try to guess the secret code. Valid colors are: R, G, B, Y, O, P"));
        assertTrue(capturedOutput.contains("The code is 5 characters long."));
        assertTrue(capturedOutput.contains("You have 10 attempts."));

        verify(mockedScanner,times(1)).nextLine();
        assertTrue(capturedOutput.contains("Congratulations! You guessed the code!"));

        verify(databaseService,times(1)).getTopResults(3);
        verify(databaseService,times(1)).saveGameResult(any(GameResult.class));
        assertTrue(capturedOutput.contains("Failed to retrieve top results"));
        assertTrue(capturedOutput.contains("Failed to save game result"));

    }

    // </editor-fold>
}
