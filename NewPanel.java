import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.*;

class NewPanel extends JPanel {
		
		int environmentSize;
		int rowHeight;
		int rowWidth;
		int x1;
		int x2;
		int y1;
		int y2;
		int height;
		int width;
		int[] s = new int[2];
		int[] goal = new int[2];
		MultiGraph incumbent = new MultiGraph("incumbent");
		MultiGraph obstacles = new MultiGraph("obstacles");
		
		// constructor
		public NewPanel(int environmentSize, int[] s, int[] goal, MultiGraph incumbent, int height, int width, MultiGraph obstacles) {
			this.s = s;
			this.goal = goal;
			this.environmentSize = environmentSize;
			this.incumbent = incumbent;
			this.height = height;
			this.width = width;
			this.obstacles = obstacles;
			rowHeight = height / environmentSize;
			rowWidth = width / environmentSize;
		}
		
		// draws the environment, starting point (red), goal point (green), path (black), and obstacles (blue)
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			// draw the rows
			for(int i = 0; i < environmentSize + 1; i++) {
				g2.drawLine(0, i * rowHeight, width, i * rowHeight);  
			}
			
			// draw the columns
			for(int i = 0; i < environmentSize + 1; i++) {
				g2.drawLine(i * rowWidth, 0, i * rowWidth, height);
			}
			
			// draw start and goal point
			g2.setStroke(new BasicStroke(10));
			g2.setColor(Color.RED);
			g2.drawLine(s[0] * rowWidth, s[1] * rowHeight, s[0] * rowWidth, s[1] * rowHeight);
			g2.setColor(Color.GREEN);
			g2.drawLine(goal[0] * rowWidth, goal[1] * rowHeight, goal[0] * rowWidth, goal[1] * rowHeight);
			
			// draw solution path
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(2));
			x1 = incumbent.getNode(0).getAttribute("x");
			y1 = incumbent.getNode(0).getAttribute("y");
			for(Node incumbentNode: incumbent) {
				x2 = incumbentNode.getAttribute("x");
				y2 = incumbentNode.getAttribute("y");
				g2.drawLine(x1 * rowWidth, y1 * rowHeight, x2 * rowWidth, y2 * rowHeight);
				x1 = x2;
				y1 = y2;
			}
			// draw line from last node in incumbent to goal node
			g2.drawLine(x1 * rowWidth, y1 * rowHeight, goal[0] * rowWidth, goal[1] * rowHeight); 
			
			// draw obstacles
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(4));
			for(Node obstacleNode: obstacles) {
				x1 = obstacleNode.getAttribute("x");
				y1 = obstacleNode.getAttribute("y");
				g2.drawLine(x1 * rowWidth, y1 * rowHeight, x1 * rowWidth, y1 *rowHeight);
			}
			
			
		}
	}

