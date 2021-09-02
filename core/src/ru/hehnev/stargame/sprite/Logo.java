package ru.hehnev.stargame.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.hehnev.stargame.base.Sprite;
import ru.hehnev.stargame.math.Rect;

public class Logo extends Sprite {

    private static final float V_LEN = 0.01f;

    private Vector2 v;
    private Vector2 tmp;
    private Vector2 touch;


    public Logo(Texture texture) {
        super(new TextureRegion(texture));
        v = new Vector2();
        tmp = new Vector2();
        touch = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setSize(0.1f, 0.1f);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        System.out.println("touch = " + touch);
        v.set(touch.cpy().sub(this.pos)).setLength(V_LEN);
        System.out.println("v = " + v);
        System.out.println("pos = " + this.pos);
        return false;
    }

    @Override
    public void update(float delta) {
        tmp.set(touch);
        if (tmp.sub(this.pos).len() > V_LEN) {
            this.pos.add(v);
        } else {
            this.pos.set(touch);
        }
    }
}
