package szachy;

import org.example.szachy.BoardState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class AllAvailableMovesTest {
    @Test
    public void testAllAvailableMoves() {
        // given
        BoardState boardState = new BoardState(8);
        // when
        boardState.setField("kd1");
        boardState.setField("kf1");

        Map<String, List<String>> moves = boardState.availableMoves();
        List<String> allMoves = boardState.allUniqueAvailableMoves(moves);

        assertThat(allMoves, containsInAnyOrder("h2","d2","g3","e3","f2","b2","c3"));
        assertThat(allMoves, hasSize(7));
        // then
    }
}
