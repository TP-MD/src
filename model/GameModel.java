package model;


import java.util.ArrayList;

import controler.IGameControler;

public class GameModel implements IGameControler {

	@Override
	public Ibonbon getIbonbon(int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIbonbon(int row, int col, Ibonbon icon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean select(Case[] cases) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Case> findRuns(boolean doMarkAndUpdateScore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Case> collapseColumn(int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Case> fillColumn(int col) {
		// TODO Auto-generated method stub
		return null;
	}

	
	}


