package be.catvert.marioshooter.scenes;

import be.catvert.marioshooter.Game;
import be.catvert.marioshooter.Score;
import be.catvert.marioshooter.entities.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by arno on 6/05/17.
 */
public class MainMenuScene extends Scene {
    private final float _posBestScore;

    public MainMenuScene(Game game) {
        super(game);

        _posBestScore = Gdx.graphics.getHeight() - _game.getMainFont().getLineHeight() - 100;

        Texture logoTexture = Game.getTexture("logo.png");
        addEntity(new Entity(logoTexture, new Rectangle(Gdx.graphics.getWidth() / 2 - logoTexture.getWidth() / 2, Gdx.graphics.getHeight() - logoTexture.getHeight(), logoTexture.getWidth(), logoTexture.getHeight())));
    }

    @Override
    public void initUI(Stage stage) {
        VisWindow window = new VisWindow("Menu principal");

        VerticalGroup group = new VerticalGroup();

        VisTextButton playButton = new VisTextButton("Jouer !");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScene(new GameScene(_game), true, true);
            }
        });

        VisTextButton exitButton = new VisTextButton("Quitter");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        group.addActor(playButton);
        group.addActor(exitButton);
        group.space(10f);

        window.add(group);
        window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);

        stage.addActor(window);


        VisCheckBox useOtherBackgroundCB = new VisCheckBox("Utiliser le fond d'écran alternatif",false);
        useOtherBackgroundCB.setPosition(0, useOtherBackgroundCB.getHeight());
        useOtherBackgroundCB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                _game.UseDefaultBackground = !useOtherBackgroundCB.isChecked();
                updateBackground();
            }
        });

        VisCheckBox playMusicCB = new VisCheckBox("Jouer la musique",true);
        playMusicCB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(playMusicCB.isChecked())
                    _game.playMusic();
                else
                    _game.stopMusic();
            }
        });

        stage.addActor(useOtherBackgroundCB);
        stage.addActor(playMusicCB);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        _game.getMainFont().draw(batch, "-- Meilleur scores --", 0, _posBestScore);
        for(int i = 0; i < 5; ++i) {
            String playerName = "N/A";
            int score = 0;

            if(i < _game.getScores().size()) {
                Score sc = _game.getScores().get(i);
                playerName = sc.getPlayerName();
                score = sc.getScore();
            }
            _game.getMainFont().draw(batch, String.format("%d. %s : %d", i + 1, playerName, score), 0, _posBestScore - _game.getMainFont().getLineHeight() * (i + 1));
        }

    }
}
