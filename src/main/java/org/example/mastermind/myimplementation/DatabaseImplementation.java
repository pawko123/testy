package org.example.mastermind.myimplementation;

import org.example.mastermind.DatabaseService;
import org.example.mastermind.GameResult;

import java.util.ArrayList;
import java.util.List;

public class DatabaseImplementation implements DatabaseService {
    private List<GameResult> gameResults;

    public DatabaseImplementation() {
        this.gameResults = new ArrayList<>();
    }

    @Override
    public void saveGameResult(GameResult result) {
        gameResults.add(result);
    }

    @Override
    public List<GameResult> getTopResults(int limit) {
        gameResults.sort((r1, r2) -> Integer.compare(r1.getAttempts(), r2.getAttempts()));
        return gameResults.subList(0, Math.min(limit, gameResults.size()));
    }

    public List<GameResult> getAllResults() {
        return new ArrayList<>(gameResults);
    }
}
