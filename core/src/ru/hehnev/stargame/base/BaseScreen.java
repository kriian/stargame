package ru.hehnev.stargame.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.hehnev.stargame.math.MatrixUtils;
import ru.hehnev.stargame.math.Rect;

public class BaseScreen implements Screen, InputProcessor {

    private Rect screenBounds;
    private Rect worldBounds;
    private Rect glBounds;

    private Matrix4 worldToGl;
    private Matrix3 screenToWorld;

    private Vector2 touch;

    protected SpriteBatch batch;

    @Override
    public void show() {
        screenBounds = new Rect();
        worldBounds = new Rect();
        glBounds = new Rect(0, 0, 1f, 1f);
        worldToGl = new Matrix4();
        screenToWorld = new Matrix3();
        touch = new Vector2();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize width = " + width + " height = " + height);
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);

        float aspect = width / (float) height;
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect);

        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        resize(worldBounds);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);
    }

    public void resize(Rect worldBounds) {
        System.out.println("resize worldBounds.width = " + worldBounds.getWidth() + " worldBounds.height = " + worldBounds.getHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // получили вектор в нашей мировой сетке координат соответсвующий worldBounds
        touch.set(screenX, screenBounds.getHeight()).mul(screenToWorld);
        touchDown(touch, pointer, button);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight()).mul(screenToWorld);
        touchUp(touch, pointer, button);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, screenBounds.getHeight()).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
