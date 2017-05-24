package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by arno on 17/05/17.
 */
public final class Player extends Entity {
    private final ArrayList<Enemy> _enemies;

    private float _moveSpeed = 10f;
    private int _lifePoint = 3;
    private int _score = 0;
    private boolean _godMode = false;

    private Sound _bulletSound;
    private Sound _damageSound;

    public int getLifePoint() { return _lifePoint; }

    public int getScore() { return _score; }
    public void addScorePoint(int point) { _score += point; }

    public boolean getGodMode() { return _godMode; }
    public void setGodMode(boolean value) {
        _godMode = value;
    }

    public Player(ArrayList<Enemy> enemies) {
        super(Game.getTexture("player.png"), new Rectangle(0, Gdx.graphics.getHeight() / 2 - 25, 40, 50));

        _enemies = enemies;

        _bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.mp3"));
        _damageSound = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));
    }

    @Override
    public void update() {
        super.update();

        if(Gdx.input.isKeyPressed(Input.Keys.Z) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(_rectangle.y + _moveSpeed < Gdx.graphics.getHeight() - _rectangle.height)
                _rectangle.y += _moveSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(_rectangle.y - _moveSpeed > 0)
            _rectangle.y -= _moveSpeed;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            _bulletSound.play();
            Game.getCurrentScene().addEntity(new Bullet(new Vector2(_rectangle.x, _rectangle.y), new Vector2(15, 0), _enemies));
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        _damageSound.dispose();
        _bulletSound.dispose();
    }

    public void removeOneLifePoint() {
        if(!_godMode) {
            _damageSound.play();
            --_lifePoint;
        }
    }

    public void addLifePoint(int number) {
        _lifePoint += number;
    }

    public void addMoveSpeed(float moveSpeed) {
        _moveSpeed += moveSpeed;
    }
}
