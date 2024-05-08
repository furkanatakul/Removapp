package Helpers;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class StarRating2 extends JPanel {
	private static final long serialVersionUID = 1L;
	private int starCount = 5;
    private int starSize = 20;
    private int starSpacing = 14;

    private int filledStars = 0;
    
    public int getStarCount() {
		return starCount;
	}

	public int getStarSize() {
		return starSize;
	}

	public int getStarSpacing() {
		return starSpacing;
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);

        for (int i = 0; i < starCount; i++) {
            int x = i * (starSize + starSpacing) + 5;
            int y = 5;
            int[] xPoints = {x, x + 10, x + 15, x + 20, x + 30, x + 22, x + 25, x + 15, x + 5, x + 8};
            int[] yPoints = {y, y, y - 10, y, y, y + 5, y + 15, y + 10, y + 15, y + 5};

            if (filledStars > i) {
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(Color.BLACK);
            }
            g2d.fillPolygon(xPoints, yPoints, 10);
        }
    }



	public void setfilledStars(int filledStars ) {
       this.filledStars = filledStars;
    }
}
