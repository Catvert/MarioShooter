package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by arno on 20/05/17.
 */
public class Enemy_3 extends Enemy {

    private boolean _visible = true;
    private boolean _goDown = Game.Random.nextBoolean();

    private float _moveSpeedX = 5f, _moveSpeedY = 5f;

    public Enemy_3(Player player) {
        super(Game.getTexture("enemy_3.png"), getRandomPosition(25, 50), player, 20);

        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                _visible = !_visible;
            }
        }, 2, 1);
    }

    @Override
    public void update() {
        super.update();

        _moveSpeedX = Game.Random.nextInt(3) + 1;
        _moveSpeedY = Game.Random.nextInt(10) + 1;

        _rectangle.x -= _moveSpeedX;

        if(_rectangle.y <= 0)
            _goDown = false;
        else if(_rectangle.y + _rectangle.height > Gdx.graphics.getHeight())
            _goDown = true;

        if(_goDown)
            _rectangle.y -= _moveSpeedY;
        else
            _rectangle.y += _moveSpeedY;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(_visible)
            super.draw(batch);
    }
}
