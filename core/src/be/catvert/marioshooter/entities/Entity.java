package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import be.catvert.marioshooter.IRenderer;
import be.catvert.marioshooter.IUpdateable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by arno on 6/05/17.
 */
public class Entity implements IUpdateable, IRenderer, Disposable {

    protected Texture _texture = null;
    protected Rectangle _rectangle;

    public void setTexture(Texture texture) { _texture = texture; }

    public Rectangle getRectangle() { return _rectangle; }

    public Entity(Rectangle rectangle) {
        this(null, rectangle);
    }

    public Entity(Texture texture, Vector2 position) {
        this(texture, new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight()));
    }

    public Entity(Texture texture, Rectangle rectangle) {
        _texture = texture;
        _rectangle = rectangle;
    }

    @Override
    public void update() {}

    @Override
    public void draw(SpriteBatch batch) {
        if(_texture != null)
            batch.draw(_texture, _rectangle.x, _rectangle.y, _rectangle.width, _rectangle.height);
    }

    public void destroyFromCurrentScene() {
        Game.getCurrentScene().removeEntity(this);
    }

    @Override
    public void dispose() {}
}
