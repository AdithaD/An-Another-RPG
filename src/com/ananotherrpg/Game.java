package com.ananotherrpg;

import com.ananotherrpg.entity.Player;
import com.ananotherrpg.level.Campaign;

public class Game {

	private Player player;
	private Campaign campaign;
	
	private enum State{
		MENU, GAME, COMBAT
	}
	
	private State gameState;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
