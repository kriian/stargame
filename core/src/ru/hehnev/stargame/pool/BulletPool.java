package ru.hehnev.stargame.pool;

import ru.hehnev.stargame.base.BaseSprite;
import ru.hehnev.stargame.base.SpritesPool;
import ru.hehnev.stargame.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {


    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
