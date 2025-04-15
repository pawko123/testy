package org.example.mastermind;

import java.util.List;

public interface DatabaseService {
    void saveGameResult(GameResult result) throws DatabaseException;
    List<GameResult> getTopResults(int limit) throws DatabaseException;
}
