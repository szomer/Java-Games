package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.RasmusGame;
import com.mygdx.game.Screens.PlayScreen;

public class B2WorldCreater {
    public B2WorldCreater(PlayScreen screen) {

        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //Create body and fixtures variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2) , (rect.getY() + rect.getHeight()/2));

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2) , (rect.getHeight()/2));
            fdef.shape = shape;
            body.createFixture(fdef);
        }

    }
}
