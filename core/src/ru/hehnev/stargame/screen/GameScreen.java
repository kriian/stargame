package ru.hehnev.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.hehnev.stargame.base.BaseScreen;
import ru.hehnev.stargame.base.Font;
import ru.hehnev.stargame.math.Rect;
import ru.hehnev.stargame.pool.BulletPool;
import ru.hehnev.stargame.pool.EnemyPool;
import ru.hehnev.stargame.pool.ExplosionPool;
import ru.hehnev.stargame.sprite.Background;
import ru.hehnev.stargame.sprite.Bullet;
import ru.hehnev.stargame.sprite.EnemyShip;
import ru.hehnev.stargame.sprite.Explosion;
import ru.hehnev.stargame.sprite.GameOver;
import ru.hehnev.stargame.sprite.GamingShip;
import ru.hehnev.stargame.sprite.NewGameButton;
import ru.hehnev.stargame.sprite.Star;
import ru.hehnev.stargame.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private static final int STAR_COUNT = 32;
    private static final float MARGIN = 0.01f;

    private Texture bg;
    private TextureAtlas atlas;

    private Background background;
    private Star[] stars;
    private GamingShip gamingShip;

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;

    private EnemyEmitter enemyEmitter;

    private GameOver gameOver;
    private NewGameButton newGameButton;

    private Font font;
    private StringBuffer sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private Sound bulletSound;
    private Sound explosionSound;

    private int frags;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/space.png");
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        gamingShip = new GamingShip(atlas, bulletPool, explosionPool);


        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);

        gameOver = new GameOver(atlas);
        newGameButton = new NewGameButton(atlas, this);

        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(0.02f);
        sbFrags = new StringBuffer();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
    }

    public void startNewGame() {
        frags = 0;

        gamingShip.startNewGame();

        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        gamingShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGameButton.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        gamingShip.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (!gamingShip.isDestroyed()) {
            gamingShip.touchDown(touch, pointer, button);
        } else {
            newGameButton.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (!gamingShip.isDestroyed()) {
            gamingShip.touchUp(touch, pointer, button);
        } else {
            newGameButton.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!gamingShip.isDestroyed()) {
            gamingShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!gamingShip.isDestroyed()) {
            gamingShip.keyUp(keycode);
        }
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        if (!gamingShip.isDestroyed()) {
            gamingShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        }
        explosionPool.updateActiveSprites(delta);
    }

    private void checkCollision() {
        if (gamingShip.isDestroyed()) return;
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList) {
            float minDst = enemyShip.getHalfWidth() + gamingShip.getHalfWidth();
            if (gamingShip.pos.dst(enemyShip.pos) < minDst) {
                enemyShip.destroy();
                gamingShip.damage(enemyShip.getBulletDamage() * 2);
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() != gamingShip) {
                if (gamingShip.isCollision(bullet)) {
                    gamingShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            } else {
                for (EnemyShip  enemyShip : enemyShipList) {
                    if (enemyShip.isCollision(bullet)) {
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroy();
                        if (enemyShip.isDestroyed()) {
                            frags++;
                        }
                    }
                }

            }
        }


    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        if (!gamingShip.isDestroyed()) {
            gamingShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else {
            gameOver.draw(batch);
            newGameButton.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        sbHP.setLength(0);
        font.draw(batch, sbHP.append(HP).append(gamingShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - MARGIN, worldBounds.getTop() - MARGIN, Align.right);
    }
}
