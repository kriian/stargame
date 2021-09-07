package ru.hehnev.stargame.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.hehnev.stargame.base.BaseScreen;
import ru.hehnev.stargame.math.Rect;
import ru.hehnev.stargame.sprite.Background;
import ru.hehnev.stargame.sprite.GamingShip;
import ru.hehnev.stargame.sprite.Star;

public class GameScreen extends BaseScreen {

    public static final int STAR_COUNT = 32;

    private Texture bg;
    private TextureAtlas atlas;

    private Background background;
    private Star[] stars;
    private GamingShip gamingShip;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/space.png");
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        gamingShip = new GamingShip(atlas);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        gamingShip.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        gamingShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchUp(touch, pointer, button);
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        gamingShip.update(delta);
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        gamingShip.draw(batch);
        batch.end();
    }
}
