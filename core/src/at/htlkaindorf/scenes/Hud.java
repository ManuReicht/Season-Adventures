package at.htlkaindorf.scenes;

import at.htlkaindorf.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The hud of the game. It shows values like score, time, world, ... on the playscreen.
 * It implements the Diasposable class.
 * @author Reicht Manuel
 * */
public class Hud implements Disposable{

    /**
     * The stage on which the hud should appear. It is needed to show the hud on top of
     * the playscreen.
     */
    private Stage stage;
    /**
     * The viewport of the stage of the hud.
     */
    private Viewport viewport;

    /**
     * The amount of time in seconds the user have to finish a level.
     */
    private Integer worldTimer;
    /**
     * Turns true if the timeCount reaches zero.
     */
    private boolean timeUp;
    /**
     * The timer for a level. It starts at the value of worldTimer and if it reaches zero
     * timeUp turns true.
     */
    private float timeCount;
    /**
     * The score of the player. It raises if the player collects something, kills an enemy, ... .
     */
    private static Integer score;

    /**
     * The label of the countdown which contains the value of timeCount.
     */
    private Label lblCountdown;
    /**
     * The label of the score which contains the value of score.
     */
    private static Label lblScore;
    /**
     * This label is the heading of lblCountdown.
     */
    private Label lblTime;
    /**
     * This label shows the current world and level.
     */
    private Label lblLevel;
    /**
     * This label is the heading of lblLevel.
     */
    private Label lblWorld;
    /**
     * This label is the heading of lblScore.
     */
    private Label lblScoreHeading;

    /**
     * Sets the skin of the hud and adds all Labels to the hud.
     * @author Trummer Nik
     * */
    public Hud(SpriteBatch sb){
        worldTimer = 500;
        timeCount = 0;
        score = 0;

        Skin skin = new Skin(Gdx.files.internal("skins/pixthulhu/pixthulhu-ui.json"));

        viewport = new FitViewport(Game.getInstance().getV_WIDTH(), Game.getInstance().getV_HEIGHT(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        lblCountdown = new Label(String.format("%03d", worldTimer), skin);
        lblCountdown.setFontScale(0.6f);
        lblScore = new Label(String.format("%06d", score), skin);
        lblScore.setFontScale(0.6f);
        lblTime = new Label("TIME", skin);
        lblTime.setFontScale(0.6f);
        lblLevel = new Label("test", skin);
        lblLevel.setFontScale(0.6f);
        lblWorld = new Label("WORLD", skin);
        lblWorld.setFontScale(0.6f);
        lblScoreHeading = new Label("SCORE", skin);
        lblScoreHeading.setFontScale(0.6f);

        table.add(lblScoreHeading).expandX().padTop(10);
        table.add(lblWorld).expandX().padTop(10);
        table.add(lblTime).expandX().padTop(10);

        table.row();
        table.add(lblScore).expandX();
        table.add(lblLevel).expandX();
        table.add(lblCountdown).expandX();

        stage.addActor(table);

    }

    /**
     * Updates the world counter and sets timeUp true if the worldTimer reaches zero.
     * It also reloads the level if the timeUp is true.
     * @param dt delta time
     * @since 1.0
     *  */
    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            lblCountdown.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }

        lblScore.setText(String.format("%06d", score));

        if(timeUp){
            Game.getInstance().reloadGame();
        }
    }

    /**
     * Adds a value to the score and updates lblScore.
     * @param value the value which is added to the score
     * @since 1.0
     *  */
    public void addScore(int value){
        score += value;
        lblScore.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() { stage.dispose(); }

    public Stage getStage() {
        return stage;
    }

    public void setLevelName(String name){
        lblLevel.setText(name);
    }
}
