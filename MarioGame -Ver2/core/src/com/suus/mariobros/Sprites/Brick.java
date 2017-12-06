package com.suus.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.suus.mariobros.MarioBros;
import com.suus.mariobros.Scenes.Hud;
import com.suus.mariobros.Screens.PlayScreen;

public class Brick extends InteractiveTileObject{

    //Constructor
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);

    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()) {

            Gdx.app.log("Brick", "collision");
            setCategoryFilter(MarioBros.DESTROYED_BIT);

            getCell().setTile(null);
            Hud.addScore(200);

            MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }else{
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        }
    }
}
