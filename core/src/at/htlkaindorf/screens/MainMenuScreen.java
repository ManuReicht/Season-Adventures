package at.htlkaindorf.screens;

import at.htlkaindorf.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Used to create the main menu screen of the game.
 * It implements the Screen class.
 * @author Reicht Manuel
 * */
public class MainMenuScreen implements Screen {
    /**
     * The stage on which the menu screen should appear. It is needed to show the screen on top of
     * the playscreen.
     */
    private final Stage stage;

    public MainMenuScreen() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Sets the skin of the menu screen.
     * It also adds the buttons and their action listeners to the screen.
     * @author Reicht Manuel
     * */
    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skins/pixthulhu/pixthulhu-ui.json"));

        //create buttons
        TextButton newGame = new TextButton("New Game", skin);
        TextButton loadGame = new TextButton("Load Game", skin);
        TextButton exit = new TextButton("Exit", skin);
        Label title = new Label("Season-Adventures", skin);
        title.setAlignment(Align.center);
        title.setFontScale(2);
        //add buttons to table

        table.add(title).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        //table.add(loadGame).fillX().uniformX();
        //table.row().pad(10, 0, 0, 0);
        table.add(exit).fillX().uniformX();

        //stage.getViewport().update(width, height, true);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.getInstance().loadMap("1-1");
            }
        });
    }

    /**
     * Renders the menu screen.
     * @param dt delta time
     * @since 1.0
     *  */
    @Override
    public void render(float dt) {

        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
