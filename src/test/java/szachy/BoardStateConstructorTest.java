package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardStateConstructorTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 26})
    public void testConstructorValidSize(int size) {
        // given
        BoardState[] boardState = new BoardState[1];
        // when
        assertDoesNotThrow(() -> boardState[0] =  new BoardState(size));
        // then
        assertThat(boardState[0].board.size(), is(size * size));
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

    @ParameterizedTest
    @ValueSource(ints = {-20,-1,0})
    public void testConstructorInvalidSizeParametrizedUnder1(int size) {
        // given
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new BoardState(size));
        // then
        assertThat(exception.getMessage(), is("Size must be greater than 0"));
    }

    @ParameterizedTest
    @ValueSource(ints = {27, 30, 100})
    public void testConstructorInvalidSizeParametrizedOver26(int size) {
        // given
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new BoardState(size));
        // then
        assertThat(exception.getMessage(), is("Size must be less than 27"));
    }
}
