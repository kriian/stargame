package ru.hehnev.stargame;


import com.badlogic.gdx.Game;

import ru.hehnev.stargame.screen.MenuScreen;

public class StarGame extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}
}
