package ru.hehnev.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.hehnev.stargame.base.BaseButton;
import ru.hehnev.stargame.math.Rect;
import ru.hehnev.stargame.screen.GameScreen;

public class NewGameButton extends BaseButton {

    private static final float HEIGHT = 0.05f;
    private static final float TOP_MARGIN = -0.012f;

    private final GameScreen gameScreen;

    public NewGameButton(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setTop(TOP_MARGIN);
    }

    @Override
    public void action() {
        gameScreen.startNewGame();
    }
}
