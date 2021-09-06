package ru.hehnev.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.hehnev.stargame.base.Sprite;
import ru.hehnev.stargame.math.Rect;

public class GamingShip extends Sprite {

    private static final float HEIGHT = 0.2f;

    private final Vector2 v;



    public GamingShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        v = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }
}
