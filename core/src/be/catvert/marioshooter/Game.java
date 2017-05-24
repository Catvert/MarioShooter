package be.catvert.marioshooter;

import be.catvert.marioshooter.scenes.MainMenuScene;
import be.catvert.marioshooter.scenes.Scene;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game extends ApplicationAdapter {
    public static Random Random = new Random();
    private static AssetManager _assetsManager = new AssetManager();
    private static Scene _scene;

    private SpriteBatch _batch;
    private Stage _stage;
    private BitmapFont _mainFont;
    private ArrayList<Score> _scores = new ArrayList<>();
    public boolean UseDefaultBackground = true;
    private Music _mainMusic;

    public static Scene getCurrentScene() {
        return _scene;
    }

    public BitmapFont getMainFont() {
        return _mainFont;
    }

    public ArrayList<Score> getScores() {
        return _scores;
    }

    @Override
    public void create() {
        VisUI.load();

        _batch = new SpriteBatch();

        _mainFont = new BitmapFont(Gdx.files.internal("mainFont.fnt"), false);

        loadAssets(Gdx.files.internal("textures").file().listFiles());

        _assetsManager.load("particles/enemy_1.p", ParticleEffect.class);

        _assetsManager.finishLoading();

        loadScores();

        _mainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/mainMenuMusic.mp3"));
        _mainMusic.setLooping(true);
        _mainMusic.setVolume(0.3f);
        _mainMusic.play();

        _stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(_stage);

        setScene(new MainMenuScene(this), false, false);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _scene.update();

        _batch.begin();
        _scene.draw(_batch);
        _batch.end();

        _stage.act(Gdx.graphics.getDeltaTime());
        _stage.draw();
    }

    @Override
    public void dispose() {
        saveScores();

        _mainMusic.dispose();
        _mainFont.dispose();
        _assetsManager.dispose();
        _batch.dispose();
        _stage.dispose();
        _scene.dispose();
        VisUI.dispose(true);
    }

    private void loadAssets(File[] files) {
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadAssets(file.listFiles());
                } else {
                    System.out.println("Chargement de l'asset : " + file.getPath());
                    _assetsManager.load(file.getPath(), Texture.class);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargements des assets ! Erreur : " + e.getMessage());
        }
    }

    private void saveScores() {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(Gdx.files.internal("scores.json").path()));
            writer.setOutputType(JsonWriter.OutputType.json);
            writer.object();

            writer.array("scores");

            for (Score sc : _scores) {
                writer.object();

                writer.name("playerName");
                writer.value(sc.getPlayerName());

                writer.name("score");
                writer.value(sc.getScore());

                writer.pop();
            }

            writer.pop();
            writer.pop();

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement des scores ! Erreur : " + e.getMessage());
        }
    }

    private void loadScores() {
        try {
            JsonValue root = new JsonReader().parse(Gdx.files.internal("scores.json"));

            JsonValue scores = root.get("scores");
            scores.forEach(sc -> {
                String playerName = sc.getString("playerName");
                int score = sc.getInt("score");

                addNewScore(new Score(playerName, score));
            });

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des scores ! Erreur : " + e.getMessage());
        }
    }

    public void addNewScore(Score score) {
        _scores.add(score);
        _scores.sort((sc1, sc2) -> Integer.compare(sc2.getScore(), sc1.getScore()));
    }

    public void setScene(Scene scene, boolean disposeLastScene, boolean clearStage) {
        if (disposeLastScene)
            _scene.dispose();
        if (clearStage)
            _stage.clear();

        _scene = scene;
        _scene.initUI(_stage);
    }

    public void playMusic() {
        _mainMusic.play();
    }

    public void stopMusic() {
        _mainMusic.stop();
    }

    public static Texture getTexture(String file) {
        if (_assetsManager.isLoaded("textures/" + file))
            return _assetsManager.get("textures/" + file);
        else {
            System.err.println("La texture : " + file + " est introuvable !");
            return new Texture(1, 1, Pixmap.Format.Alpha);
        }
    }

    public static ParticleEffect getParticleEffect(String file) {
        if(_assetsManager.isLoaded("particles/" + file))
            return _assetsManager.get("particles/" + file);
        else {
            System.err.println("Impossible de charger la particule : " + file);
            return new ParticleEffect();
        }
    }
}
