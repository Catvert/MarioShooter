package be.catvert.marioshooter;

/**
 * Created by arno on 17/05/17.
 * Cette classe permet de gérer le score de chaque partie pour être ensuite sauvegardé dans un fichier.
 */
public final class Score {
    private final String _playerName;
    private final int _score;

    public String getPlayerName() {
        return _playerName;
    }
    public int getScore() { return _score; }

    public Score(String playerName, int score) {
        _playerName = playerName;
        _score = score;
    }
}
