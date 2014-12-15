package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class ScoreView extends JPanel {

	private static final String scoreFormat = "Score: %1d";
	private int score;

	public void updateScore(int newScore) {
		this.score = newScore;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		Dimension d = getPreferredSize();
		((Graphics2D) g).setBackground(Color.WHITE);
		g.clearRect(0, 0, d.width, d.height);
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, MainPanel.SCORE_FONT);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = String.format(scoreFormat, score);

		int width = metrics.stringWidth(text);
		int x = (d.width - width) / 2;
		int y = (d.height) / 2;

		g.drawString(text, x, y);
	}

}
