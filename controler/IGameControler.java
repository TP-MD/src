package controler;

import java.util.ArrayList;

import model.Case;
import model.Ibonbon;

public interface IGameControler {

	Ibonbon getIbonbon(int row, int col);

	void setIbonbon(int row, int col, Ibonbon icon);

	int getWidth();

	int getHeight();

	int getScore();

	boolean select(Case[] cases);

	ArrayList<Case> findRuns(boolean doMarkAndUpdateScore);

	ArrayList<Case> collapseColumn(int col);

	ArrayList<Case> fillColumn(int col);

}
