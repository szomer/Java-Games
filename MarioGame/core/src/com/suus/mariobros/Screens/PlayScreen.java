package com.suus.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.suus.mariobros.MarioBros;
import com.suus.mariobros.Scenes.Hud;
import com.suus.mariobros.Sprites.Mario;
import com.suus.mariobros.Tools.B2WorldCreater;
import com.suus.mariobros.Tools.WorldContactListener;

public class PlayScreen implements Screen{
   //Reference to our game, used to set screen
    private MarioBros game;
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
    private Mario player;


    //CONSTRUCTOR
    public PlayScreen(MarioBros game){
        atlas = new TextureAtlas("Mario_And_Enemies.pack");

        this.game = game;

        //Create cam used to follow mario through cam world
        gameCam = new OrthographicCamera();

        //create a FitViewport to maintain cirtual aspect ratio despite screen size
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);

        //Create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("MarioMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        //Initially set our gameCam to be centered correctly at the start of the map
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        //Create out box2d world, setting no gravity at x, and -10 in y
        world = new World(new Vector2(0,-10 ), true);

        //Allows for debug lines of out box2d world
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreater(world, map);

        //Create mario player
        player = new Mario(world, this);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }


    public void handleInput(float dt){
        //If our user is holding down mouse move out camera through the game world
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(),true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (player.b2body.getLinearVelocity().x <= 2)){
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (player.b2body.getLinearVelocity().x >= -2)){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }

    }

    public void update(float dt){
        //Handle User input first
        handleInput(dt);

        //Takes 1 step in the physis simulation(60 fps)
        world.step(1/60f, 6, 2);

        player.update(dt);

        //Attach our gameCam to our player.x coordinate
        gameCam.position.x = player.b2body.getPosition().x;

        //Update our gameCam with correct coordinates after changes
        gameCam.update();

        //Tell our renderer to draw only what out camera can see in our world
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        //Separate our update logic from render
        update(delta);

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

    //Adjustment of screen size
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
