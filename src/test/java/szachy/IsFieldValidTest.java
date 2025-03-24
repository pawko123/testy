package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class IsFieldValidTest {

    @Test
    public void testIsFieldValidTrueSize8() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boolean result = boardState.isFieldValid("d1");
        // then
        assertThat(result, is(true));
        // no exception is thrown
    }

    @Test
    public void testIsFieldValidTrueSize1() {
        // given
        BoardState boardState = new BoardState(1);
        // when
        boolean result = boardState.isFieldValid("a1");
        // then
        assertThat(result, is(true));
        // no exception is thrown
    }
    @ParameterizedTest
    @ValueSource(strings = {"d9", "a0"})
    public void testIsFieldValidFalse(String field) {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boolean result = boardState.isFieldValid(field);
        // then
        assertThat(result, is(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {"d1", "a1"})
    public void testIsFieldValidFalseWithObstacle(String field) {
        // given
        BoardState boardState = new BoardState(8);
        boardState.setField("p"+field);
        // when
        boolean result = boardState.isFieldValid(field);
        // then
        assertThat(result, is(false));
    }
}
