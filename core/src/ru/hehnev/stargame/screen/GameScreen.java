package ru.hehnev.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.hehnev.stargame.base.BaseScreen;
import ru.hehnev.stargame.math.Rect;
import ru.hehnev.stargame.pool.BulletPool;
import ru.hehnev.stargame.pool.EnemyPool;
import ru.hehnev.stargame.sprite.Background;
import ru.hehnev.stargame.sprite.GamingShip;
import ru.hehnev.stargame.sprite.Star;
import ru.hehnev.stargame.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    public static final int STAR_COUNT = 32;

    private Texture bg;
    private TextureAtlas atlas;

    private Background background;
    private Star[] stars;
    private GamingShip gamingShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private EnemyEmitter enemyEmitter;
    private Sound bulletSound;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/space.png");
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));

        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        gamingShip = new GamingShip(atlas, bulletPool);

        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
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
        freeAllDestroyed();
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        bulletSound.dispose();
        gamingShip.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        gamingShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        gamingShip.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        gamingShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        gamingShip.keyUp(keycode);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        gamingShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemyEmitter.generate(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        gamingShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }
}
