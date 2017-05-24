package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by arno on 18/05/17.
 */
public abstract class Enemy extends Entity {
    private Player _player;

    private int _pointOnDie = 1;

    protected Enemy(Texture texture, Rectangle rectangle, Player player, int pointOnDie) {
        super(texture, rectangle);

        _player = player;

        _pointOnDie = pointOnDie;
    }

    @Override
    public void update() {
        super.update();

        if(_rectangle.x <= 0) {
            _player.removeOneLifePoint();
            destroyFromCurrentScene();
        }
    }

    public void kill() {
        _player.addScorePoint(_pointOnDie);
        destroyFromCurrentScene();
    }

    protected static Rectangle getRandomPosition(float width, float height) {
        return new Rectangle(Gdx.graphics.getWidth(), Game.Random.nextInt(Gdx.graphics.getHeight() - (int)height - 50) + 50, width, height);
    }
}
