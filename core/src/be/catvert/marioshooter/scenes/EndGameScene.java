package be.catvert.marioshooter.scenes;

import be.catvert.marioshooter.Game;
import be.catvert.marioshooter.Score;
import be.catvert.marioshooter.entities.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by arno on 17/05/17.
 * Scène de fin de partie pour enregistrer son score.
 */
public final class EndGameScene extends Scene {
    private int _score;
    private final GlyphLayout _scoreLayout = new GlyphLayout();
    private final boolean _godModeUsed;

    public EndGameScene(Game game, int score, boolean godModeUsed) {
        super(game);

        _score = score;
        _godModeUsed = godModeUsed;

        Gdx.audio.newSound(Gdx.files.internal("sounds/gameOver.mp3")).play();

        Texture gameOverTexture = Game.getTexture("gameOver.png");
        addEntity(new Entity(gameOverTexture, new Rectangle(Gdx.graphics.getWidth() / 2 - gameOverTexture.getWidth() / 2, Gdx.graphics.getHeight() - gameOverTexture.getHeight(), gameOverTexture.getWidth(), gameOverTexture.getHeight())));
    }
    @Override
    public void initUI(Stage stage) {
        VisWindow window = new VisWindow("Enregistrer votre score");
        window.setSize(200, 150);

        VerticalGroup group = new VerticalGroup();

        VisTextField playerNameField = new VisTextField();
        playerNameField.setMessageText("Entrer votre nom");

        VisTextButton saveButton = new VisTextButton(_godModeUsed ? "GodMode utilisé" :"Sauvegarder");
        saveButton.setTouchable(_godModeUsed ? Touchable.disabled : Touchable.enabled); // Rend le button impossible à cliquer si le mode dieu à été utilisé en pendant la partie.
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.addNewScore(new Score(playerNameField.isEmpty() ? "Anonymous" : playerNameField.getText(), _score));
                _game.setScene(new MainMenuScene(_game), true, true);
            }
        });

        VisTextButton dontSaveButton = new VisTextButton("Ne pas sauvegarder");
        dontSaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScene(new MainMenuScene(_game), true, true);
            }
        });

        group.addActor(playerNameField);
        group.addActor(saveButton);
        group.addActor(dontSaveButton);

        group.space(10f);
        window.add(group);

        window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);

        stage.addActor(window);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        String scoreText = "Score : " + _score;
        _scoreLayout.setText(_game.getMainFont(), scoreText);
        _game.getMainFont().draw(batch, scoreText, Gdx.graphics.getWidth() / 2 - _scoreLayout.width / 2, Gdx.graphics.getHeight() - _scoreLayout.height);
    }
}
