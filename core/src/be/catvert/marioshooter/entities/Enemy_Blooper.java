package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by arno on 20/05/17.
 * Troisième ennemi du jeu, le blooper.
 */
public final class Enemy_Blooper extends Enemy {
    private boolean _visible = true; // Donne au blooper une capacité d'invisibilité.
    private boolean _goDown = Game.Random.nextBoolean(); // Permet de savoir si le blooper va vers le bas ou le haut.

    private float _moveSpeedX = 5f, _moveSpeedY = 5f;

    public Enemy_Blooper(Player player) {
        super(Game.getTexture("enemy_blooper.png"), getRandomPosition(25, 50), player, 20, 10);

        // Timer démarrant après 2 sec avec une interval de 1 sec permettant d'inverser la visibilité du blooper.
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

        _moveSpeedX = Game.Random.nextInt(5) + 1; // Déplacement aléatoire du blooper.
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
