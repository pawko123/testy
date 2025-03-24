package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetFieldTest {

    @ParameterizedTest
    @ValueSource(strings = {"d1", "d8", "a1", "h8", "e4", "f6", "c3", "g7"})
    public void testSetFieldValidFields(String field) {
        // given
        BoardState boardState = new BoardState(8);
        // when
        assertDoesNotThrow(() -> boardState.setField("k"+field));
        // then
        assertThat(boardState.board, hasEntry(field, "k"));
        // no exception is thrown
    }

    @Test
    public void testSetFieldSize1(){
        // given
        BoardState boardState = new BoardState(1);
        // when
        assertDoesNotThrow(() -> boardState.setField("ka1"));
        // then
        assertThat(boardState.board, hasEntry("a1", "k"));
    }
    @Test
    public void testSetFieldPieceNotGiven(){
        // given
        BoardState boardState = new BoardState(8);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField("d1"));
        // then
        assertThat(exception.getMessage(), is("Piece not allowed"));
    }

    @Test
    public void testSetFieldPieceNotGivenButValidPieceLetter(){
        // given
        BoardState boardState = new BoardState(8);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField("k1"));
        // then
        assertThat(exception.getMessage(), is("Fieldname does not exist"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "i", "l", "m", "n", "o"})
    public void testSetFieldInvalidPiece(String piece){
        // given
        BoardState boardState = new BoardState(1);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField(piece+"a1"));
        // then
        assertThat(exception.getMessage(), is("Piece not allowed"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a9", "i1", "a0", "i9", "j1", "a11", "j11", "k0"})
    public void testSetFieldInvalidField(String field){
        // given
        BoardState boardState = new BoardState(8);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boardState.setField("k"+field));
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
