package view;

import javax.swing.SwingUtilities;

public class CandyCruch {
	
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			public void run() {
				MainPanel.create();
			}
		};
		SwingUtilities.invokeLater(r);
	}

}
