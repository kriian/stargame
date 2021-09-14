package ru.hehnev.stargame.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.hehnev.stargame.math.Rect;
import ru.hehnev.stargame.pool.BulletPool;
import ru.hehnev.stargame.pool.ExplosionPool;
import ru.hehnev.stargame.sprite.Bullet;
import ru.hehnev.stargame.sprite.Explosion;

public class Ship extends BaseSprite {

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected final Vector2 v0 = new Vector2();
    protected final Vector2 v = new Vector2();

    protected Sound bulletSound;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int bulletDamage;

    protected float reloadTimer;
    protected float reloadInterval;
    protected float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    protected Rect worldBounds;

    protected int hp;

    protected ExplosionPool explosionPool;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);

        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }

        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public void damage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
        frame = 1;
        damageAnimateTimer = 0f;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        bulletSound.play(0.5f);
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(pos, getHeight());
    }

    public boolean isCollision(Rect rect) {
        return !(rect.getRight() < getLeft()
                || rect.getLeft() > getRight()
                || rect.getBottom() > getTop()
                || rect.getTop() < pos.y);
    }
}
