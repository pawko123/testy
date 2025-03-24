package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AvailableMovesTest {
    @Test
    public void testAvailableMoves() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boardState.setField("kd1");

        Map<String, List<String>> moves = boardState.availableMoves();
        // then
        assertThat(moves.size(), is(1));
        assertThat(moves.get("d1"), containsInAnyOrder("b2", "f2", "c3", "e3"));
        assertThat(moves.get("d1"), hasSize(4));
    }

    @Test
    public void testAvailableMovesNoKnight() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                boardState::availableMoves);
        // then
        assertThat(exception.getMessage(), is("There is no knight on the board"));
    }

    @Test
    public void testAvailableMovesSize1() {
        // given
        BoardState boardState = new BoardState(1);
        // when
        boardState.setField("ka1");

        Map<String, List<String>> moves = boardState.availableMoves();
        // then
        assertThat(moves.size(), is(1));
        assertThat(moves.get("a1"),hasSize(0));
    }

    @Test
    public void testAvailableMovesWithObstacleSize8() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boardState.setField("kd1");
        boardState.setField("pc3");
        boardState.setField("pe3");

        Map<String, List<String>> moves = boardState.availableMoves();
        // then
        assertThat(moves.size(), is(1));
        assertThat(moves.get("d1"), containsInAnyOrder("b2", "f2"));
        assertThat(moves.get("d1"), hasSize(2));
    }

    @Test
    public void testAvailableMovesManyKnights() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boardState.setField("kd1");
        boardState.setField("kd2");

        Map<String, List<String>> moves = boardState.availableMoves();
        // then
        assertThat(moves.size(), is(2));
        assertThat(moves.get("d1"), containsInAnyOrder("b2", "f2", "c3", "e3"));
        assertThat(moves.get("d1"), hasSize(4));
        assertThat(moves.get("d2"), containsInAnyOrder("b3", "f3", "c4", "e4","f1","b1"));
        assertThat(moves.get("d2"), hasSize(6));
    }
}
