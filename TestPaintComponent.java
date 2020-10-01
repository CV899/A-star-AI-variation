
import javax.swing.*;

import org.graphstream.graph.implementations.MultiGraph;

public class TestPaintComponent extends JFrame {
	int environmentSize;
	int[] s = new int[2];
	int[] goal = new int[2];
	int height;
	int width;
	MultiGraph obstacles = new MultiGraph("obstacles");
	MultiGraph incumbent = new MultiGraph("incumbent");
		public TestPaintComponent(int environmentSize, int[] s, int[] goal, MultiGraph incumbent, int height, int width, MultiGraph obstacles) {
			this.environmentSize = environmentSize;
			this.s = s;
			this.goal = goal;
			this.incumbent = incumbent;
			this.height = height;
			this.width = width;
			this.obstacles = obstacles;
			add(new NewPanel(environmentSize, s, goal, incumbent, height, width, obstacles));
		}
}
