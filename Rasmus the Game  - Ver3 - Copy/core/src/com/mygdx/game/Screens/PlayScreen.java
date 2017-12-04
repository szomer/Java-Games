package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.RasmusGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Tools.B2WorldCreater;
import com.mygdx.game.Tools.WorldContactListener;

public class PlayScreen implements Screen{

    private RasmusGame game;
    private TextureAtlas atlas;

    //Basic playscreen variables
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Sprites
    private Player player;


    private Music music;


    public PlayScreen(RasmusGame game) {

        this.game = game;

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(RasmusGame.V_WIDTH , RasmusGame.V_HEIGHT, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("WorldMap2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);

        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        world = new World(new Vector2(0,0 ), true);

        b2dr = new Box2DDebugRenderer();

        new B2WorldCreater(this);

        //Create mario player
        player = new Player(world);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            System.exit(0);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            game.setScreen(new PauseScreen(game));
        }

    }


    public void update(float dt){
        //Handle User input first
        handleInput(dt);
        player.handleInput(dt);

        //Takes 1 step in the physis simulation(60 fps)
        world.step(1/60f, 6, 2);

        hud.update(dt);

        //Attach our gameCam to our player.x coordinate


        //Update our gameCam with correct coordinates after changes
        gameCam.update();

        //Tell our renderer to draw only what out camera can see in our world
        renderer.setView(gameCam);
    }


    @Override
    public void render(float delta) {
        //Separate our update logic from render
        update(delta);

        gameCam.position.set(player.body.getPosition().x,player.body.getPosition().y,0);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT );

        //Render our game map
        renderer.render();

        //Re-render  our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        //Set our batch to now draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

}
