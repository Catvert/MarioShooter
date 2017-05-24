package be.catvert.marioshooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by arno on 18/05/17.
 */
public class Bullet extends Entity {
    private Vector2 _moveSpeed;

    private Rectangle _screenRect = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private final ArrayList<Enemy> _enemies;

    private TextureAtlas _textureAtlas;
    private Animation<TextureAtlas.AtlasRegion> _animation;
    private float _elapsedTime = 0f;

    public Bullet(Vector2 startPosition, Vector2 moveSpeed, ArrayList<Enemy> enemies) {
        super(null, new Rectangle(startPosition.x, startPosition.y, 50, 20));

        _moveSpeed = moveSpeed;

        _enemies = enemies;

        _textureAtlas = new TextureAtlas(Gdx.files.internal("anim/fireball.atlas"));
        _animation = new Animation<>(1f/15f, _textureAtlas.getRegions());

    }

    @Override
    public void update() {
        super.update();

        _rectangle.setPosition(_rectangle.x + _moveSpeed.x, _rectangle.y + _moveSpeed.y);

        if(!_rectangle.overlaps(_screenRect))
            destroyFromCurrentScene();

        for(int i = 0; i < _enemies.size(); ++i) {
            if(_enemies.get(i).getRectangle().overlaps(_rectangle)) {
                _enemies.get(i).kill();
                this.destroyFromCurrentScene();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        _elapsedTime += Gdx.graphics.getDeltaTime();

        batch.draw(_animation.getKeyFrame(_elapsedTime,true),_rectangle.x,_rectangle.y, _rectangle.width, _rectangle.height);
    }

    @Override
    public void dispose() {
        super.dispose();

        _textureAtlas.dispose();
    }
}
