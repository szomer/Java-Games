package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.RasmusGame;
import com.mygdx.game.RasmusGame;

public class GameOver implements Screen {

    private RasmusGame game;
    private Stage stage;
    private Music music;

    private Texture backGround;
    private Image BGI;
    public GameOver(final RasmusGame game) {
        this.game = game;
        backGround = new Texture("GameOver.png");
        BGI = new Image(backGround);
        // BGI.setPosition(Gdx.graphics.getWidth()/8 - BGI.getWidth()/8,
        //       Gdx.graphics.getHeight()/8 - BGI.getHeight()/8);
        BGI.setSize(backGround.getWidth() / 2, backGround.getHeight() / 2);
        BGI.setPosition(350, 0);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        Skin MenuButtons = new Skin(Gdx.files.internal("ButtonsMenu.json"));
        Button StartGame = new TextButton("Start Game", MenuButtons);
        StartGame.setSize(150, 50);
        StartGame.setPosition(150, 305);

        Button loadGame = new TextButton("Load Game", MenuButtons);
        loadGame.setSize(150, 50);
        loadGame.setPosition(150, 205);


        Button exitGame = new TextButton("Exit Game", MenuButtons);
        exitGame.setSize(150, 50);
        exitGame.setPosition(150, 105);

        StartGame.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PlayScreen(game));
                music.dispose();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(BGI);
        stage.addActor(StartGame);
        stage.addActor(loadGame);
        stage.addActor(exitGame);
        //stage.addActor(BGI);
        music = Gdx.audio.newMusic(Gdx.files.internal("Greta_Sting.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw( );

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
