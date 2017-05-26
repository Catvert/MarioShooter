package be.catvert.marioshooter.scenes;

import be.catvert.marioshooter.Game;
import be.catvert.marioshooter.IRenderer;
import be.catvert.marioshooter.IUpdateable;
import be.catvert.marioshooter.entities.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 * Created by arno on 6/05/17.
 * Classe abstraite permettant d'implémenter une scène.
 */
public abstract class Scene implements IRenderer, IUpdateable, Disposable {
    private final ArrayList<Entity> _entities = new ArrayList<>();
    protected final Game _game;
    private final Entity _backgroundEntity;

    protected Scene(Game game){
        _game = game;

        _backgroundEntity = addEntity(new Entity(new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        updateBackground();
    }

    /**
     * Permet la création de l'UI nécessaire pour la scène.
     */
    public abstract void initUI(Stage stage);

    @Override
    public void draw(SpriteBatch batch) {
        for(int i = 0; i < _entities.size(); ++i)
            _entities.get(i).draw(batch);
    }

    @Override
    public void update() {
        for(int i = 0; i < _entities.size(); ++i)
            _entities.get(i).update();
    }

    public Entity addEntity(Entity e) {
        _entities.add(e);
        return e;
    }

    public void removeEntity(Entity e) {
        e.dispose();
        _entities.remove(e);
    }

    /**
     * Permet de mettre à jour le fond d'écran.
     */
    protected void updateBackground() {
        _backgroundEntity.setTexture(_game.getUseDefaultBackground() ? Game.getTexture("background_1.png") : Game.getTexture("background_2.png"));
    }

    @Override
    public void dispose() {
        for(int i = 0; i < _entities.size(); ++i)
            removeEntity(_entities.get(i));
    }
}
