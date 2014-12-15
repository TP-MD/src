package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Case;
import controler.IGameControler;
import controler.Ibonbon;

public class GameView extends JPanel {

	private static final Color BACKGROUND_COLOR = Color.white;

	private Color[] colors = {

	Color.RED, Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW };

	/**
	 * nombre de pixel pour chaque bonbon qui tombe
	 */
	private int fallPerFrame = 4;

	/**
	 * Interval des chutes de bonbon en milliseconds.
	 */
	private int fallingSpeed = 5;

	/**
	 * temps de selection deux bonbon.
	 */
	private int numberOfFlashes = 3;
	private int flashingSpeed = 50;
	private ScoreView scoreView;
	private Timer timer;
	private IGameControler iGameControler;

	private int flashingState = 0;

	private ArrayList<Case> CasesToCollapse = null;

	private boolean collapsing = false;

	private ArrayList<AnimatedCase> movingCases = null;

	private boolean filling = false;

	private ArrayList<AnimatedCase> fillingCases = null;

	private Case currentCase;
	private Case nextCase;

	public GameView(IGameControler iGameControler, ScoreView scoreView) {
		this.iGameControler = iGameControler;
		this.scoreView = scoreView;
		addMouseListener(new MyMouseListener());
		addMouseMotionListener(new MyMouseMotionListener());
		timer = new Timer(fallingSpeed, new TimerCallback());
	}

	/*
	 * Le paintComponent est invoquée par le cadre Swing chaque fois que le
	 * panneau doit être rendu à l'écran. Dans cette application, repeindre est
	 * normalement déclenchée par les appels à la méthode repaint ( ) dans le
	 * rappel de la minuterie et le gestionnaire d'événement de clavier
	 */

	public void paint(Graphics g) {
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int row = 0; row < iGameControler.getHeight(); ++row) {
			for (int col = 0; col < iGameControler.getWidth(); ++col) {
				Ibonbon t = iGameControler.getIbonbon(row, col);
				if (t != null) {
					paintOneCase(g, row, col, t);
				}
			}
		}

		if (currentCase != null) {
			highlightOneCase(g, currentCase.getRow(), currentCase.getCol());
		}
		if (nextCase != null) {
			highlightOneCase(g, nextCase.getRow(), nextCase.getCol());
		}

		if (flashingState > 0) {

			for (Case p : CasesToCollapse) {
				paintOneCase(g, p.getRow(), p.getCol(), p.getIbonbon());

				if (flashingState % 2 != 0) {
					highlightOneCase(g, p.getRow(), p.getCol());
				}
			}
		}

		if (collapsing) {

			for (AnimatedCase c : movingCases) {
				int col = c.getCol();
				int row = c.getRow();
				paintOneCase(g, row, col, BACKGROUND_COLOR);
			}

			for (AnimatedCase c : movingCases) {
				int col = c.getCol();
				int pixel = c.currentPixel;
				paintOneCaseByPixel(g, pixel, col, c.getIbonbon());
			}
		}

		if (filling) {

			for (AnimatedCase c : fillingCases) {
				int col = c.getCol();
				int row = c.getRow();
				paintOneCase(g, row, col, BACKGROUND_COLOR);
			}

			for (AnimatedCase c : fillingCases) {
				int col = c.getCol();
				int pixel = c.currentPixel;
				paintOneCaseByPixel(g, pixel, col, c.getIbonbon());
			}

		}
	}

	private void paintOneCase(Graphics g, int row, int col, Ibonbon t) {
		int x = MainPanel.SIZE * col;
		int y = MainPanel.SIZE * row;
		g.setColor(getColorForIbonbon(t));
		g.fillRect(x, y, MainPanel.SIZE, MainPanel.SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, MainPanel.SIZE - 1, MainPanel.SIZE - 1);

	}

	/**
	 * Rend une seule case de la grille.
	 *
	 */
	private void paintOneCase(Graphics g, int row, int col, Color color) {

		int x = MainPanel.SIZE * col;
		int y = MainPanel.SIZE * row;
		g.setColor(color);
		g.fillRect(x, y, MainPanel.SIZE, MainPanel.SIZE);
		g.setColor(Color.white);
		;
		g.drawRect(x, y, MainPanel.SIZE - 1, MainPanel.SIZE - 1);
	}

	/**
	 * Rend une seule case de la grille précisant sa position verticale en
	 * pixels .
	 *
	 */
	private void paintOneCaseByPixel(Graphics g, int rowPixel, int col,
			Ibonbon t) {

		int x = MainPanel.SIZE * col;
		int y = rowPixel;
		g.setColor(getColorForIbonbon(t));
		g.fillRect(x, y, MainPanel.SIZE, MainPanel.SIZE);
		g.setColor(Color.WHITE);

		g.drawRect(x, y, MainPanel.SIZE - 1, MainPanel.SIZE - 1);
	}

	/**
	 * Dessine une bordure blanche autour d'une case.
	 *
	 */
	private void highlightOneCase(Graphics g, int row, int col) {
		g.setColor(Color.WHITE);
		((Graphics2D) g).setStroke(new BasicStroke(2));
		g.drawRect(col * MainPanel.SIZE, row * MainPanel.SIZE, MainPanel.SIZE,
				MainPanel.SIZE);
		((Graphics2D) g).setStroke(new BasicStroke(1));
	}

	/**
	 * Écouteur pour les événements d'horloge . La méthode actionPerformed est
	 * appelé à chaque fois les feux de minuterie et l'appel à repaint ( ) au
	 * bas de la méthode provoque le panneau être redessiné
	 */
	private class TimerCallback implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (flashingState == 0 && !collapsing && !filling) {

				CasesToCollapse = iGameControler.findRuns(true);
				if (CasesToCollapse.size() != 0) {
					flashingState = numberOfFlashes * 2;
					timer.setDelay(flashingSpeed);
					timer.restart();

					scoreView.updateScore(iGameControler.getScore());
				} else {

					CasesToCollapse = null;
					timer.stop();
				}
			}

			if (flashingState > 0) {
				flashingState--;
				if (flashingState == 0) {

					timer.setDelay(fallingSpeed);
					timer.restart();

					ArrayList<Case> currentMovedCases = new ArrayList<Case>();
					for (int col = 0; col < iGameControler.getWidth(); ++col) {
						currentMovedCases.addAll(iGameControler
								.collapseColumn(col));
					}

					collapsing = true;
					movingCases = new ArrayList<AnimatedCase>();
					for (Case c : currentMovedCases) {
						movingCases.add(new AnimatedCase(c));
					}
				}
			}

			if (collapsing) {

				boolean found = false;
				for (AnimatedCase Case : movingCases) {
					if (!Case.done()) {
						found = true;
						Case.animate(fallPerFrame);
					}
				}
				if (!found) {

					collapsing = false;
					movingCases = null;

					initializeCasesToFill();
					filling = true;
					if (fillingCases.size() == 0) {
						System.out
								.println("WARNING: game returned collapsing Cases but failed to return Cases to fill columns");
						filling = false;
						fillingCases = null;
					}
				}
			}

			if (filling) {

				boolean found = false;
				for (AnimatedCase Case : fillingCases) {
					if (!Case.done()) {
						found = true;
						Case.animate(fallPerFrame);
					}
				}
				if (!found) {
					filling = false;
					fillingCases = null;
				}
			}

			repaint();
		}
	}

	/**
	 * Définit la liste de remplissage des cases
	 */
	private void initializeCasesToFill() {
		fillingCases = new ArrayList<AnimatedCase>();
		for (int col = 0; col < iGameControler.getWidth(); ++col) {
			ArrayList<Case> currentNewCases = iGameControler.fillColumn(col);
			for (Case c : currentNewCases) {
				fillingCases.add(new AnimatedCase(c, -1));
			}
		}
	}

	private Color getColorForIbonbon(Ibonbon Ibonbon) {
		int index = Ibonbon.getType();
		if (index < 0 || index > colors.length) {
			return Color.WHITE;
		}
		return colors[index];
	}

	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {
		}

		@Override
		public void mousePressed(MouseEvent event) {
			if (flashingState > 0 || collapsing || filling)
				return;

			int row = event.getY() / MainPanel.SIZE;
			int col = event.getX() / MainPanel.SIZE;

			currentCase = new Case(row, col,
					iGameControler.getIbonbon(row, col));
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			if (flashingState > 0 || collapsing || filling)
				return;

			if (currentCase != null) {
				if (nextCase != null) {
					boolean swapped = iGameControler.select(new Case[] {
							currentCase, nextCase });

					repaint();
					if (swapped) {

						timer.setDelay(flashingSpeed);
						timer.restart();
					}
				}
			}
			currentCase = null;
			nextCase = null;

		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	private class MyMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			int row = e.getY() / MainPanel.SIZE;
			int col = e.getX() / MainPanel.SIZE;
			if (currentCase != null) {

				if ((currentCase.getCol() == col && Math.abs(currentCase
						.getRow() - row) == 1)
						|| (currentCase.getRow() == row && Math.abs(currentCase
								.getCol() - col) == 1)) {
					nextCase = new Case(row, col, iGameControler.getIbonbon(
							row, col));
				} else {
					nextCase = null;
				}
				repaint();
			}

		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}

	private class AnimatedCase extends Case {
		public int startPixel; // pixel coordinates
		public int endPixel;
		public int currentPixel;

		public AnimatedCase(Case Case) {
			super(Case.getRow(), Case.getCol(), Case.getIbonbon());
			startPixel = Case.getPreviousRow() * MainPanel.SIZE;
			currentPixel = startPixel;
			endPixel = Case.getRow() * MainPanel.SIZE;
		}

		public AnimatedCase(Case Case, int startRow) {
			super(Case.getRow(), Case.getCol(), Case.getIbonbon());
			this.setPreviousRow(startRow);
			startPixel = startRow * MainPanel.SIZE;
			currentPixel = startPixel;
			endPixel = Case.getRow() * MainPanel.SIZE;
		}

		public boolean done() {
			return currentPixel == endPixel;
		}

		public void animate(int pixels) {
			currentPixel += pixels;
			if (currentPixel > endPixel) {
				currentPixel = endPixel;
			}
		}
	}

}
