package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class IsFieldValidTest {

    @Test
    public void testIsFieldValidTrue() {
        // given
        BoardState boardState = new BoardState(8);
        BoardState boardState2 = new BoardState(1);
        // when
        boolean result = boardState.isFieldValid("d1");
        boolean result2 = boardState2.isFieldValid("a1");
        // then
        assertThat(result, is(true));
        assertThat(result2, is(true));
        // no exception is thrown
    }
    @Test
    public void testIsFieldValidFalse() {
        // given
        BoardState boardState = new BoardState(8);
        BoardState boardState2 = new BoardState(1);

        boardState.setField("pd4");
        // when
        boolean result = boardState.isFieldValid("d9");
        boolean result2 = boardState2.isFieldValid("a0");
        boolean result3 = boardState.isFieldValid("d4");
        // then
        assertThat(result, is(false));
        assertThat(result2, is(false));
        assertThat(result3, is(false));
        // no exception is thrown
    }
}
