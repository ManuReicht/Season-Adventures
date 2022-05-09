package at.htlkaindorf.scenes;

import at.htlkaindorf.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable{

    //Scene2D.ui Stage and its own Viewport for HUD
    private Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer score;

    //Scene2D widgets
    private Label lblCountdown;
    private static Label lblScore;
    private Label lblTime;
    private Label lblLevel;
    private Label lblWorld;
    private Label lblPlayer;

    public Hud(SpriteBatch sb){
        //define our tracking variables
        worldTimer = 500;
        timeCount = 0;
        score = 0;

        Skin skin = new Skin(Gdx.files.internal("skins/pixthulhu/pixthulhu-ui.json"));

        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Game.getInstance().getV_WIDTH(), Game.getInstance().getV_HEIGHT(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
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
        lblPlayer = new Label("SCORE", skin);
        lblPlayer.setFontScale(0.6f);

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(lblPlayer).expandX().padTop(10);
        table.add(lblWorld).expandX().padTop(10);
        table.add(lblTime).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(lblScore).expandX();
        table.add(lblLevel).expandX();
        table.add(lblCountdown).expandX();

        //add our table to the stage
        stage.addActor(table);

    }

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

        if(timeUp){
            Game.getInstance().reloadGame();
        }
    }

    public static void addScore(int value){
        score += value;
        lblScore.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() { return timeUp; }

    public Stage getStage() {
        return stage;
    }

    public void setLevelName(String name){
        lblLevel.setText(name);
    }
}
