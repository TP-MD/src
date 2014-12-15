package model;

import java.util.Random;

import controler.Ibonbon;


// cette classe qui va nous permettre de génerer des bonbons
public class BonbonFactory implements IBonbonFactory{
	
	private Random iRand;
	private int type;
	
	
	public BonbonFactory(int type) {
		this.type = type;
		iRand = new Random();
	}
	

	public BonbonFactory(Random iRand, int type) {
		this.iRand = iRand;
		this.type = type;
	}

	
	// méthode de géneration de bonbon a l'aide de Random
	@Override
	public Ibonbon createRandomfactory() {
		Ibonbon newIbonbon = new BonbonModel(iRand.nextInt(type-1));
		return newIbonbon;	
		}

	/*methode d'initialisation d'un tableau 2D de bonbon 
	 * qui va nous permettre d'etre sure que deux cases adjacentes 
	 * n'ont pas le meme type*/
	
	@Override
	public void initialize(Ibonbon[][] grid) {
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if ((i + j) % 2 == 0) {
					grid[i][j] = new BonbonModel(0);
				} else {
					grid[i][j] = new BonbonModel(1);
				}

			}
		}	
	}
}
