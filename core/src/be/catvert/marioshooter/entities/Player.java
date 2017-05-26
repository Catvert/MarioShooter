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
 * Classe du joueur.
 */
public final class Player extends Entity {
    private final ArrayList<Enemy> _enemies;
    private float _moveSpeed = 10f;
    private int _lifePoint = 3;
    private int _score = 0;
    private boolean _godMode = false;
    private int _remainingBullet = 30;

    private final Sound _bulletSound;
    private final Sound _emptyBulletSound;
    private final Sound _damageSound;

    public final int getLifePoint() { return _lifePoint; }

    public final int getScore() { return _score; }
    public final void addScorePoint(int point) { _score += point; }

    public final boolean getGodMode() { return _godMode; }
    public final void setGodMode(boolean value) {
        _godMode = value;
    }

    public final int getRemainingBullet() { return _remainingBullet; }

    public Player(ArrayList<Enemy> enemies) {
        super(Game.getTexture("player.png"), new Rectangle(0, Gdx.graphics.getHeight() / 2 - 25, 40, 50));

        _enemies = enemies;

        _bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.mp3"));
        _emptyBulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/emptyBullet.mp3"));
        _damageSound = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));
    }

    @Override
    public void update() {
        super.update();

        if(Gdx.input.isKeyPressed(Input.Keys.Z) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(_rectangle.y + _moveSpeed < Gdx.graphics.getHeight() - _rectangle.height) // Vérifie si le joueur sera toujours dans l'écran après le déplacement
                _rectangle.y += _moveSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(_rectangle.y - _moveSpeed > 0)
                _rectangle.y -= _moveSpeed;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if(_remainingBullet > 0) {
                --_remainingBullet;
                _bulletSound.play();
                Game.getCurrentScene().addEntity(new Bullet(new Vector2(_rectangle.x, _rectangle.y), new Vector2(15, 0), _enemies));
            }
            else
                _emptyBulletSound.play();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        _bulletSound.dispose();
        _emptyBulletSound.dispose();
        _damageSound.dispose();
    }

    public final void removeOneLifePoint() {
        if(!_godMode) {
            _damageSound.play();
            --_lifePoint;
        }
    }

    public final void addMoveSpeed(float moveSpeed) {
        _moveSpeed += moveSpeed;
    }

    public final void addBullets(int numberBullet) {
        _remainingBullet += numberBullet;
    }
}
