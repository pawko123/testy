package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardStateConstructorTest {
    @Test
    public void testConstructor() {
        // given
        BoardState[] boardStateContainer = new BoardState[2];
        assertDoesNotThrow(() -> boardStateContainer[0] = new BoardState(8));
        assertDoesNotThrow(() -> boardStateContainer[1] = new BoardState(1));
        BoardState boardState = boardStateContainer[0];
        BoardState boardState2 = boardStateContainer[1];

        assertThat(boardState.board.size(), is(64));
        assertThat(boardState2.board.size(), is(1));
    }

    @Test
    public void testConstructorInvalidSize() {
        // given
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new BoardState(0));
        Exception exception1 = assertThrows(IllegalArgumentException.class,
                () -> new BoardState(-1));
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> new BoardState(27));
        // then
        assertThat(exception.getMessage(), is("Size must be greater than 0"));
        assertThat(exception1.getMessage(), is("Size must be greater than 0"));
        assertThat(exception2.getMessage(), is("Size must be less than 27"));
    }
}
