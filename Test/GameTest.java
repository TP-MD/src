package model;

import org.junit.Test;

import controler.*;
import view.*;



public class GameTest {
	
	BonbonFactory bonbon = new BonbonFactory(5); 
	GameModel game = new GameModel(4, 4, bonbon); 
	
	// tester la création de la grille
	@Test
	public void testGameModel(){
	System.out.println("------------test de la classe GameModel--------");
		Assert.assertNotNull(game);
		Assert.assertNotNull(bonbon);
		System.out.println(game.toString());
		System.out.println("------------------------------------------------");
	}
	
	//tester la creation des bonbons 
	@Test
	public void testCreateBonbon(){
		Ibonbon bon ;
		bon = bonbon.createRandomfactory();
		Assert.assertNotNull(bon);
	}
	
	//tester la méthode select qui return true si deux bonbon ont été échangé 
	@Test
	public void testSelectTrue(){
		Case case1 = new Case(2, 2, game.getIbonbon(1, 1));
		Case case2 = new Case(2, 3, game.getIbonbon(3, 3));
		Boolean cases = game.select(new Case[]{case1, case2});
		Assert.assertTrue(cases);
	}
	
	//tester la méthode findsRun qui verifie s'il y a eu un echange de bonbon que ca soit 
	//vertical ou horizental et modifie la valeur de score si c'est la cas
	@Test
	public void testFindRuns(){
		System.out.println("------------test de la methode FindRuns--------");
		
		game.setIbonbon(0, 1, new BonbonModel(4));
		game.setIbonbon(0, 2, new BonbonModel(4));
		game.setIbonbon(0, 3, new BonbonModel(4));
		game.setIbonbon(3, 1, new BonbonModel(4));
		game.setIbonbon(3, 2, new BonbonModel(4));
		game.setIbonbon(3, 3, new BonbonModel(4));
		
		game.findRuns(true);
		System.out.println(game.toString());
		System.out.println("votre score est " + game.getScore()); 
		
		System.out.println("------------------------------------------------");

	}

}
