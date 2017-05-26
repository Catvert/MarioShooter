package be.catvert.marioshooter.scenes;

import be.catvert.marioshooter.Game;
import be.catvert.marioshooter.entities.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

/**
 * Created by arno on 17/05/17.
 * Scène du jeu.
 */
public final class GameScene extends Scene {
    private final Player _player;
    private final HeartUI[] _heartsUI = new HeartUI[3];
    private final ArrayList<Enemy> _enemies = new ArrayList<>();
    private int _timerSeconds = 0;
    private int _elapsedTime_Enemy_2_Spawn = 3, _elapsedTime_Enemy_3_Spawn = 5;
    private boolean _godModeUsed = false;

    private int getTotalScore() {
        return _player.getScore() + _timerSeconds;
    }

    public GameScene(Game game) {
        super(game);

        _player = (Player)addEntity(new Player(_enemies));

        for(int i = 0; i < _heartsUI.length; ++i)
            _heartsUI[i] = (HeartUI)addEntity(new HeartUI(new Vector2(Gdx.graphics.getWidth() / 2 + (i - 1) * HeartUI.SIZE + (i * 5), Gdx.graphics.getHeight() - HeartUI.SIZE)));
        /**
         * Timeur permettant le spawn de chaque ennemi.
         * Les ennemi spawn selon le temps écoulé.
         * Le timeur à une interval de 1.5 sec, ce qui veut dire que chaque 1.5 sec un ou plusieurs ennemi apparait à l'écran.
         * Le joueur gagne aussi 0.5f de vitesse déplacement toute les 1.5 sec.
         */
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                ++_timerSeconds;

                _player.addMoveSpeed(0.5f);

                addEnemy(new Enemy_Bullet(_player));

                if(_timerSeconds > 10) {
                    ++_elapsedTime_Enemy_2_Spawn;

                    if(_elapsedTime_Enemy_2_Spawn >= 3) {
                        addEnemy(new Enemy_FlyGuy(_player));
                        _elapsedTime_Enemy_2_Spawn = 0;
                    }
                }

                if(_timerSeconds > 20) {
                    ++_elapsedTime_Enemy_3_Spawn;

                    if(_elapsedTime_Enemy_3_Spawn >= 5) {
                        addEnemy(new Enemy_Blooper(_player));
                        _elapsedTime_Enemy_3_Spawn = 0;
                    }
                }
            }
        }, 0, 1.5f);
    }

    @Override
    public void initUI(Stage stage) {}

    @Override
    public void update() {
        super.update();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // Retourne au menu principal
            _game.setScene(new MainMenuScene(_game), true, true);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) { // Active ou désactive le mode dieu.
            _godModeUsed = true;
            _player.setGodMode(!_player.getGodMode());
        }

        if(_player.getLifePoint() <= 0) {
            _game.setScene(new EndGameScene(_game, getTotalScore(), _godModeUsed), true, true);
        }

        for(int i = 0; i < _heartsUI.length; ++i) {
            _heartsUI[i].setIsEmpty(_player.getLifePoint() < i + 1);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        _game.getMainFont().draw(batch, "Score : " + getTotalScore(), 0, Gdx.graphics.getHeight() - _game.getMainFont().getLineHeight());
        _game.getMainFont().draw(batch, "Munitions restant : " + _player.getRemainingBullet(), 0, Gdx.graphics.getHeight() - _game.getMainFont().getLineHeight() * 3);

        if(_player.getGodMode())
            _game.getMainFont().draw(batch, "GODMODE ACTIF", 0, Gdx.graphics.getHeight() - _game.getMainFont().getLineHeight() * 5);
    }

    private void addEnemy(Enemy enemy) {
        _enemies.add((Enemy)addEntity(enemy));
    }

    @Override
    public void removeEntity(Entity e) {
        super.removeEntity(e);

        if(e instanceof Enemy)
            _enemies.remove(e);
    }
}
