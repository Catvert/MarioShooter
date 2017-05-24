package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.Gdx;

/**
 * Created by arno on 20/05/17.
 */
public class Enemy_2 extends Enemy {
    private float _moveSpeed = 5;

    private boolean _goDown = Game.Random.nextBoolean();

    public Enemy_2(Player player) {
        super(Game.getTexture("enemy_2.png"), getRandomPosition(50, 50), player, 5);
    }

    @Override
    public void update() {
        super.update();

        _rectangle.x -= _moveSpeed;

        if(_rectangle.y <= 0)
            _goDown = false;
        else if(_rectangle.y + _rectangle.height > Gdx.graphics.getHeight())
            _goDown = true;

        if(_goDown)
            _rectangle.y -= _moveSpeed;
        else
            _rectangle.y += _moveSpeed;

    }
}
