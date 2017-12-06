package com.suus.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.suus.mariobros.MarioBros;
import com.suus.mariobros.Sprites.Enemy;
import com.suus.mariobros.Sprites.InteractiveTileObject;
import com.suus.mariobros.Sprites.Items.Item;
import com.suus.mariobros.Sprites.Mario;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch(cDef){
            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
                if((fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT)) {
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                }else{
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                }
                break;
            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
                if((fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT)) {
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                }else{
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                }
                break;
                //If an enemy head and mario collide
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                break;
            //If an enemy and an object collide
            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).revercevelocity(true,false);
                }else {
                    ((Enemy) fixB.getUserData()).revercevelocity(true,false);
                }
                break;
            //If mario and an enemy collide
            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                if((fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)) {
                    ((Mario) fixA.getUserData()).hit();
                }else{
                    ((Mario) fixB.getUserData()).hit();                }
            //If an enemy and enemy collide
            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).revercevelocity(true,false);
                ((Enemy)fixB.getUserData()).revercevelocity(true,false);
                break;
            //If an item and object collide
            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
                    ((Item)fixA.getUserData()).reverceVelocity(true,false);
                }else {
                    ((Item) fixB.getUserData()).reverceVelocity(true,false);
                }
                break;
            //If an item and mario collide
            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
