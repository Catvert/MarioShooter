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

public final class Game extends ApplicationAdapter {
    public static final Random Random = new Random();

    private static final AssetManager _assetsManager = new AssetManager();
    private static Scene _scene;

    private final ArrayList<Score> _scores = new ArrayList<>();
    private SpriteBatch _batch;
    private Stage _stage;
    private BitmapFont _mainFont;
    private Music _mainMusic;
    private boolean _useDefaultBackground = true;

    public static Scene getCurrentScene() {
        return _scene;
    }

    public final BitmapFont getMainFont() {
        return _mainFont;
    }

    public final ArrayList<Score> getScores() {
        return _scores;
    }

    public final boolean getUseDefaultBackground() { return _useDefaultBackground; }
    public final void setUseDefaultBackground(boolean value) { _useDefaultBackground = value; }

    public final void playMusic() {
        _mainMusic.play();
    }
    public final void stopMusic() {
        _mainMusic.stop();
    }

    @Override
    public void create() {
        VisUI.load();

        _batch = new SpriteBatch();

        _mainFont = new BitmapFont(Gdx.files.internal("mainFont.fnt"), false); // Charge la police principal du jeu.

        loadTextures(Gdx.files.internal("textures").file().listFiles()); // Charge toutes les textures du jeu.
        _assetsManager.load("particles/enemy_bullet.p", ParticleEffect.class); // Charge le fichier de configuration de la particule utilisé pour l'ennemi 1.
        _assetsManager.finishLoading(); // Fini de charger toutes les assets.

        loadScores();

        _mainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/mainMenuMusic.mp3"));
        _mainMusic.setLooping(true);
        _mainMusic.setVolume(0.3f);
        _mainMusic.play();

        _stage = new Stage(new ScreenViewport()); // Création d'un stage pour l'UI.
        Gdx.input.setInputProcessor(_stage);

        setScene(new MainMenuScene(this), false, false); // Chargement du menu principal.
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

    /**
     * Charge toutes les textures d'un répertoire, récursivement.
     */
    private void loadTextures(File[] files) {
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadTextures(file.listFiles());
                } else {
                    System.out.println("Chargement de l'asset : " + file.getPath());
                    _assetsManager.load(file.getPath(), Texture.class);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargements des assets ! Erreur : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde les scores dans le fichier scores.json
     */
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

    /**
     * Charge tout les scores depuis le fichier scores.json
     */
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

    /**
     * Ajoute un nouveau score au jeu.
     */
    public void addNewScore(Score score) {
        _scores.add(score);
        _scores.sort((sc1, sc2) -> Integer.compare(sc2.getScore(), sc1.getScore()));
    }

    /**
     * Charge une nouvelle scène.
     * @param scene la scène à charger.
     * @param disposeLastScene Disposer la dernière scène charger ?
     * @param clearStage Supprimer tout les contrôles du stage ?
     */
    public void setScene(Scene scene, boolean disposeLastScene, boolean clearStage) {
        if (disposeLastScene)
            _scene.dispose();
        if (clearStage)
            _stage.clear();

        _scene = scene;
        _scene.initUI(_stage);
    }

    /**
     * Cherche une texture depuis l'assetManager et retourne la texture si elle existe sinon retourne une texture 1x1.
     * @param file le nom du fichier présent dans le répertoire textures/
     */
    public static Texture getTexture(String file) {
        if (_assetsManager.isLoaded("textures/" + file))
            return _assetsManager.get("textures/" + file);
        else {
            System.err.println("La texture : " + file + " est introuvable !");
            return new Texture(1, 1, Pixmap.Format.Alpha);
        }
    }

    /**
     * Cherche le fichier de configuration d'une particule depuis l'assetManager et retourne la particule si elle existe sinon retourne une instance vide est retournée.
     * @param file le nom du fichier présent dans le répertoire textures/
     */
    public static ParticleEffect getParticleEffect(String file) {
        if(_assetsManager.isLoaded("particles/" + file))
            return _assetsManager.get("particles/" + file);
        else {
            System.err.println("Impossible de charger la particule : " + file);
            return new ParticleEffect();
        }
    }
}
