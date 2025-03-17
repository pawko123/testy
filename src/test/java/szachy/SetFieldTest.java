package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetFieldTest {
    @Test
    public void testSetField() {
        // given
        BoardState boardState = new BoardState(8);
        BoardState boardState2 = new BoardState(1);
        // when
        assertDoesNotThrow(() -> boardState.setField("kd1"));
        assertDoesNotThrow(() -> boardState2.setField("ka1"));
        // then
        assertThat(boardState.board, hasEntry("d1", "k"));
        assertThat(boardState2.board, hasEntry("a1", "k"));
        // no exception is thrown
    }

    @Test
    public void testSetFieldInvalidPiece() {
        // given
        BoardState boardState = new BoardState(8);
        BoardState boardState2 = new BoardState(26);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField("ua1"));

        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField("a1"));

        Exception exception3 = assertThrows(IllegalArgumentException.class,
                () -> boardState2.setField("k1"));
        // then
        assertThat(exception.getMessage(), is("Piece not allowed"));
        assertThat(exception2.getMessage(), is("Piece not allowed"));
        assertThat(exception3.getMessage(), is("Fieldname does not exist"));

    }

    @Test
    public void testSetFieldInvalidField() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField("kd9"));
        // then
        assertThat(exception.getMessage(), is("Fieldname does not exist"));
    }

    @Test
    public void testSetFieldOverwrite() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boardState.setField("kd1");

        assertThat(boardState.board, hasEntry("d1", "k"));

        boardState.setField("pd1");
        // then
        assertThat(boardState.board, hasEntry("d1", "p"));
    }
}
