package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by arno on 18/05/17.
 */
public final class Enemy_1 extends Enemy {
    private float _moveSpeed = 3f;

    private ParticleEffect _particle;

    public Enemy_1(Player player) {
        super(Game.getTexture("enemy_1.png"), getRandomPosition(70, 45), player, 1);

       _particle = Game.getParticleEffect("enemy_1.p");
       _particle.setPosition(_rectangle.x + _rectangle.width + 10, _rectangle.y + _rectangle.height / 2);
    }

    @Override
    public void update() {
        super.update();

        _rectangle.x -= _moveSpeed;

        _particle.setPosition(_rectangle.x + _rectangle.width + 10, _rectangle.y + _rectangle.height / 2);
        _particle.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw(SpriteBatch batch) {
        _particle.draw(batch);

        super.draw(batch);
    }
}
