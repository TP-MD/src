package model;

import java.util.ArrayList;

import controler.IGameControler;
import controler.Ibonbon;

public class GameModel implements IGameControler {

	private static final int scoreBase = 10;
	private IBonbonFactory ibf;
	private int score;
	private Ibonbon[][] grid;

	public GameModel(int width, int height, IBonbonFactory bonbonfactory) {
		grid = new Ibonbon[height][width];
		bonbonfactory.initialize(grid);
		ibf = bonbonfactory;
		score = 0;
	}

	@Override
	public Ibonbon getIbonbon(int row, int col) {
		return grid[row][col];

	}

	@Override
	public void setIbonbon(int row, int col, Ibonbon ibonbon) {
		grid[row][col] = ibonbon;
	}

	@Override
	public int getWidth() {
		return grid[0].length;
	}

	@Override
	public int getHeight() {
		return grid.length;
	}

	@Override
	public int getScore() {
		return score;
	}

	/* Les mouvements sont autorisés par 
	 * l'intermédiaire de la méthode select ( )
	 *  pour des paires de cellules adjacentes seulement. 
	 *  Les cellules doivent avoir des types différents. */
	@Override
	public boolean select(Case[] cases) {
		if (cases.length == 2) { 
			int row1 = cases[0].getRow();
			int col1 = cases[0].getCol();
			int row2 = cases[1].getRow();
			int col2 = cases[1].getCol();
			// vérification de la proximité
			if ((row1 == row2 && (col1 + 1 == col2 || col1 - 1 == col2))
					|| (col1 == col2 && (row1 + 1 == row2 || row1 - 1 == row2))) {
				// Vérifie si deux bonbons sont différents
				if (cases[0].getIbonbon() != cases[1].getIbonbon()) {
					// Echanger 2 bonbons
					grid[cases[0].getRow()][cases[0].getCol()] = cases[1]
							.getIbonbon();
					grid[cases[1].getRow()][cases[1].getCol()] = cases[0]
							.getIbonbon();
					if (findRuns(false).isEmpty()) {
						// Echanger 2 bonbons
						grid[cases[0].getRow()][cases[0].getCol()] = cases[0]
								.getIbonbon();
						grid[cases[1].getRow()][cases[1].getCol()] = cases[1]
								.getIbonbon();
						//ne peut pas echanger
						return false;
					}
					// tout va bien, acceptation pour l'échange
					return true;
				}
			}
		}
		return false;
	}

	/*Un run est définie comme étant trois 
	 * ou plus de bonbons adjacents 
	 * horizontalement ou verticalement.  
	*/
	@Override
	public ArrayList<Case> findRuns(boolean doMarkAndUpdateScore) {
		ArrayList<Case> aListTemp = new ArrayList<Case>(); 
		int counter = 0; 

		// Chercher les bonbons verticaux
	    for(int i = 0; i < grid[0].length; i++){
	    	for(int j = 0; j < grid.length; j++){ 
	    		
	    		if(j < grid.length - 2 && (grid[j][i].equals(grid[j+1][i])) && (grid[j][i].equals(grid[j+2][i]))){ 

	    			aListTemp.add(new Case(j, i, grid[j][i])); 

	    			counter++; 
	    		}

	    		else if(j > 0 && j < grid.length - 1 && grid[j-1][i].equals(grid[j][i]) && grid[j+1][i].equals(grid[j][i])){ 

	    			aListTemp.add(new Case(j, i, grid[j][i])); 
	    			counter++;
	    		}

	    		else if(j > 1 && grid[j][i].equals(grid[j-1][i]) && grid[j-2][i].equals(grid[j][i])){ 

	    			aListTemp.add(new Case(j, i, grid[j][i])); 
	    			counter++;
	    		}
	    	}
	    	if(counter == 3 && doMarkAndUpdateScore){ 
	    		score = score + scoreBase; 
	    	}
	    	else if(counter > 3 && doMarkAndUpdateScore){
	    		score = score + (scoreBase * (int)Math.pow((double)2, (double)(counter - 3))); 
	    		
	    	}
	    	counter = 0;
	    }
	    counter = 0;
	 // Chercher les bonbons horizantaux
	    for(int i = 0; i < grid.length; i++){
	    	for(int j = 0; j < grid[0].length; j++){
	    		if(j < grid[j].length - 2 && grid[i][j].equals(grid[i][j+1]) && grid[i][j].equals(grid[i][j+2])){ 
	    			aListTemp.add(new Case(i, j, grid[i][j])); 
	    			counter++;
	    		}
	    		else if(j > 0 && j < grid[j].length - 1 && grid[i][j-1].equals(grid[i][j]) && grid[i][j+1].equals(grid[i][j])){ 
	    			aListTemp.add(new Case(i, j, grid[i][j])); 
	    			counter++;
	    		}
	    		else if(j > 1 && grid[i][j].equals(grid[i][j-1]) && grid[i][j-2].equals(grid[i][j])){ 
	    			aListTemp.add(new Case(i, j, grid[i][j]));
	    			counter++;
	    		}
	    	}
	    	if(counter == 3 && doMarkAndUpdateScore){
	    		score = score + scoreBase;
	    	}
	    	else if(counter > 3 && doMarkAndUpdateScore){
	    		score = score + (scoreBase * (int)Math.pow((double)2, (double)(counter - 3)));		
	    	}
	    	counter = 0;
	    }
	    
	    if(doMarkAndUpdateScore){
	    	for(int i = 0; i < aListTemp.size(); i++){
	    		setIbonbon(aListTemp.get(i).getRow(), aListTemp.get(i).getCol(), null);
	    	}
	    }
	    return aListTemp;
	}

	@Override
	public ArrayList<Case> collapseColumn(int col) {

		ArrayList<Case> aList = new ArrayList<Case>();

		for (int i = grid.length - 1; i > 0; i--) {

			int switchRow = i;

			while (grid[switchRow][col] == null && switchRow > 0) {
				switchRow--;
			}

			if (switchRow != i
					&& !(grid[i][col] == null && grid[switchRow][col] == null)) {

				grid[i][col] = grid[switchRow][col];

				grid[switchRow][col] = null;

				Case temp = new Case(i, col, grid[i][col]);
				temp.setPreviousRow(switchRow);
				aList.add(temp);
			}
		}
		return aList;
	}

	@Override
	public ArrayList<Case> fillColumn(int col) {

		ArrayList<Case> aList = new ArrayList<Case>();
		  
	    for(int i = 0; i < grid.length; i++){ 

	    	if(grid[i][col] != null){ 
	    		break;
	    	}

	    	else if(grid[i][col] == null){

	    		setIbonbon(i, col, ibf.createRandomfactory());

	    		aList.add(new Case(i, col, grid[i][col])); 
	    	}
	    }
	    return aList;
	}
	
	@Override
	  public String toString()
	  {
	    StringBuilder sb = new StringBuilder();
	    for (int row = 0; row < grid.length; ++row)
	    {
	      for (int col = 0; col < grid[0].length; ++col)
	      {
	        Ibonbon ibonbon = grid[row][col];
	        String s = String.format("%3s", (ibonbon == null ? "*" : "" + ibonbon.getType()));
	        sb.append(s);
	      }
	      sb.append("\n");
	    }
	    return sb.toString();
	  }

}
