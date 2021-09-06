package ru.hehnev.stargame.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.hehnev.stargame.base.BaseButton;
import ru.hehnev.stargame.math.Rect;
import ru.hehnev.stargame.screen.GameScreen;

public class PlayButton extends BaseButton {

    public static final float HEIGHT = 0.25f;
    public static final float PADDING = 0.01f;

    private Game game;

    public PlayButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setLeft(worldBounds.getLeft() + PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
