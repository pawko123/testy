package mastermind;

import org.example.mastermind.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MockitoTests {
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
        verify(databaseService,times(1)).saveGameResult(any(GameResult.class));
    }
}
