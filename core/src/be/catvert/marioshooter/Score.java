package be.catvert.marioshooter;

/**
 * Created by arno on 17/05/17.
 */
public final class Score {
    private String _playerName;
    private int _score;

    public String getPlayerName() {
        return _playerName;
    }

    public int getScore() { return _score; }

    public Score(String playerName, int score) {
        _playerName = playerName;
        _score = score;
    }
}
