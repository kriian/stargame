package ru.hehnev.stargame.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.hehnev.stargame.base.BaseButton;
import ru.hehnev.stargame.math.Rect;

public class ExitButton extends BaseButton {

    public static final float HEIGHT = 0.2f;
    public static final float PADDING = 0.01f;

    public ExitButton(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setRight(worldBounds.getRight() - PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
