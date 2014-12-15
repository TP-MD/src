package view;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.BonbonFactory;
import model.GameModel;
import controler.IGameControler;

public class MainPanel {

	public static final int SIZE = 40;
	public static final int SCORE_FONT = 25;
	private static final MainPanel MainPanel = null;

	public static void create() {
	
		IGameControler game = new GameModel(8, 8, new BonbonFactory(5));

		// creation des deux panels
		ScoreView scoreView = new ScoreView();
		GameView gameView = new GameView(game, scoreView);

		// associer les deux panels
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.add(scoreView);
		mainPanel.add(gameView);


		// mettre le panel dans une fenetre
		JFrame frame = new JFrame("Miam, des bonbons !");
		frame.getContentPane().add(mainPanel);
		
		// redimensionné les panels
		Dimension d = new Dimension(game.getWidth() * MainPanel.SIZE, game.getHeight() * MainPanel.SIZE);
		gameView.setPreferredSize(d);
		scoreView.setPreferredSize(d);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}


}
