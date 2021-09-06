package ru.hehnev.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.hehnev.stargame.base.Sprite;
import ru.hehnev.stargame.math.Rect;

public class GamingShip extends Sprite {

    private static final float HEIGHT = 0.1f;
    private static final float V_LEN = 0.01f;

    private final Vector2 v;
    private final Vector2 touch;


    public GamingShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship").split(390 / 2, 287)[0][0]);
        v = new Vector2();
        touch = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        v.set(touch.cpy().sub(pos).setLength(V_LEN));
        return false;
    }



    @Override
    public void update(float delta) {
        super.update(delta);
        if (touch.dst(pos) > V_LEN) {
            pos.add(v);
        } else {
            pos.set(touch);
        }
    }
}
