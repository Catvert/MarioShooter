package be.catvert.marioshooter.entities;

import be.catvert.marioshooter.Game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by arno on 17/05/17.
 */
public class HeartUI extends Entity {
    public static final int SIZE = 35;

    private boolean _isEmpty = false;

    public HeartUI(Vector2 position) {
        super(Game.getTexture("heart.png"), new Rectangle(position.x, position.y, SIZE, SIZE));
    }

    public void setIsEmpty(boolean empty) {
        if(_isEmpty != empty) {
            _isEmpty = empty;

            setTexture(_isEmpty ? Game.getTexture("heartBroken.png") : Game.getTexture("heart.png"));
        }
    }
}
