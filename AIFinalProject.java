import java.util.Random;
import java.util.Scanner;
import javax.swing.JFrame;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;
/*
 * Author: Christian Vincent
 * Last edited: 11-20-2019
 * CAP4630 - Intro to Artificial Intelligence, Prof. Ayan Dutta
 * --This is an implementation of APTS/ANA* (algorithms 3 and 4).--
 * doi: 10.1016/j.artint.2014.05.002 
 * 
 * ***NOTE***: Program won't terminate if initial w value is too small
 */
public class AIFinalProject {

	public static void main(String[] args) {
		
		// variables
		double startTime = System.currentTimeMillis();
		final int NODE_COST = 1; // NODE_COST = g in [fwn = g + wh])
		int environmentSize;
		Scanner scanner = new Scanner(System.in);
		int[] s = new int[2];
		int[] goal = new int[2];
		int height;
		int width;
		int selection;
		int x;
		int y;
		int x1;
		int y1;
		int x2;
		int y2;
		int numObstaclesAllowed;
		int numObstaclesSoFar = 0;
		double fwn;
		double w;
		double changeW;
		double G = 9999999;
		boolean foundBestSolution = false;
		MultiGraph graph = new MultiGraph("Environment");
		MultiGraph open = new MultiGraph("open");
		MultiGraph incumbent = new MultiGraph("incumbent");
		MultiGraph newSolution = new MultiGraph("new solution");
		MultiGraph obstacles = new MultiGraph("obstacles");
		Node startNode;
		Node n;
		Node goalNode;
		Node temp;
		double h;
		Random r = new Random();

		// input:
		// get the dimensions of the environment
		System.out.print("Enter environment size: ");
		environmentSize = scanner.nextInt();
		System.out.print("Enter height: ");
		height = scanner.nextInt();
		System.out.print("Enter width: ");
		width = scanner.nextInt();
		// get starting point
		System.out.print("Enter x coordinate of start point: ");
		s[0] = scanner.nextInt();
		System.out.print("Enter y coordinate of start point: ");
		s[1] = scanner.nextInt();
		// get end point (goal)
		System.out.print("Enter x coordinate of goal point: ");
		goal[0] = scanner.nextInt();
		System.out.print("Enter y coordinate of goal point: ");
		goal[1] = scanner.nextInt();
		// get w and change in w
		System.out.println("Enter w: ");
		w = scanner.nextDouble();
		System.out.println("Enter change in w: ");
		changeW = scanner.nextDouble();
		System.out.print("1: no obstacles\n" +
						 "2: 10% obstacles\n" +
						 "3: 20% obstacles\n" +
						 "4: 30% obstacles\n");
		selection = scanner.nextInt();
		scanner.close();
		
		// create environment
		graph.setAutoCreate(true);
		graph.setStrict(false);
		for(int i = 0; i < environmentSize + 1; i++) {
			for(int j = 0; j < environmentSize + 1; j++) {
				graph.addNode("(" + i + "," + j + ")");
				n = graph.getNode("(" + i + "," + j + ")");
				n.setAttribute("x", i);
				n.setAttribute("y", j);
				
			}
		}
		
		// set goal
		goalNode = graph.getNode("(" + goal[0] + "," + goal[1] + ")");
		x2 = goalNode.getAttribute("x");
		y2 = goalNode.getAttribute("y");
		// get start node
		startNode = graph.getNode("(" + s[0] + "," + s[1] + ")");
		
		// create obstacles (if applicable)
		switch(selection) {
			
		case 1: 
			break;
		
		case 2: {
			numObstaclesAllowed = (int) Math.round(.10 * (environmentSize * environmentSize));
			while(numObstaclesSoFar != numObstaclesAllowed) {
				x = r.nextInt(environmentSize);
				y = r.nextInt(environmentSize);
				if(graph.getNode("(" + x + "," + y + ")") != null && graph.getNode("(" + x + "," + y + ")") != goalNode && graph.getNode("(" + x + "," + y + ")") != startNode) {
					graph.removeNode("(" + x + "," + y + ")");
					obstacles.addNode("(" + x + "," + y + ")");
					temp = obstacles.getNode("(" + x + "," + y + ")");
					temp.setAttribute("x", x);
					temp.setAttribute("y", y);
					
					numObstaclesSoFar++;
				}
			}
			break;
		}
		
		
		case 3: {
			numObstaclesAllowed = (int) Math.round(.20 * (environmentSize * environmentSize));
			while(numObstaclesSoFar != numObstaclesAllowed) {
				x = r.nextInt(environmentSize);
				y = r.nextInt(environmentSize);
				if(graph.getNode("(" + x + "," + y + ")") != null && graph.getNode("(" + x + "," + y + ")") != goalNode && graph.getNode("(" + x + "," + y + ")") != startNode) {
					graph.removeNode("(" + x + "," + y + ")");
					obstacles.addNode("(" + x + "," + y + ")");
					temp = obstacles.getNode("(" + x + "," + y + ")");
					temp.setAttribute("x", x);
					temp.setAttribute("y", y);
					
					numObstaclesSoFar++;
				}
			}
			break;
		}
		
		case 4: {
			numObstaclesAllowed = (int) Math.round(.30 * (environmentSize * environmentSize));
			while(numObstaclesSoFar != numObstaclesAllowed) {
				x = r.nextInt(environmentSize);
				y = r.nextInt(environmentSize);
				if(graph.getNode("(" + x + "," + y + ")") != null && graph.getNode("(" + x + "," + y + ")") != goalNode && graph.getNode("(" + x + "," + y + ")") != startNode) {
					graph.removeNode("(" + x + "," + y + ")");
					obstacles.addNode("(" + x + "," + y + ")");
					temp = obstacles.getNode("(" + x + "," + y + ")");
					temp.setAttribute("x", x);
					temp.setAttribute("y", y);
					
					numObstaclesSoFar++;
				}
			}
			
			break;
		}
		
		
		
		}
		
		// **algorithm 3 main implementation**
		// insert S into OPEN
		open.addNode("(" + s[0] + "," + s[1] + ")");
		n = open.getNode(0);
		n.setAttribute("x", s[0]);
		n.setAttribute("y", s[1]);
		
		ImproveSolution solution = new ImproveSolution();
		// set initial fwn values
		for(Node graphNode: graph) {
			x1 = graphNode.getAttribute("x");
			y1 = graphNode.getAttribute("y");
			x2 = goalNode.getAttribute("x");
			y2 = goalNode.getAttribute("y");
			h = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
			fwn = NODE_COST + (w * h);
			graphNode.setAttribute("fwn", fwn);
		}
		for(Node openNode: open) {
			x1 = openNode.getAttribute("x");
			y1 = openNode.getAttribute("y");
			x2 = goalNode.getAttribute("x");
			y2 = goalNode.getAttribute("y");
			h = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
			fwn = NODE_COST + (w * h);
			openNode.setAttribute("fwn", fwn);
		}
		
		while(open.getNodeCount() != 0) {
			
			newSolution = solution.getSolution(open, w, G, graph, goalNode);
			if(newSolution != null) {
				G = solution.cost(newSolution);
				incumbent = newSolution;
			}
			else {
				
				foundBestSolution = true;
			}
			
			if(foundBestSolution)
				break;
			
			w = w - changeW;
			
			// reset fwn values
			open = solution.getOpen();
			for(Node openNode: open) {
				x1 = openNode.getAttribute("x");
				y1 = openNode.getAttribute("y");
				x2 = goalNode.getAttribute("x");
				y2 = goalNode.getAttribute("y");
				h = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
				fwn = NODE_COST + (w * h);
				openNode.setAttribute("fwn", fwn);
				// prune if h() + g() >= G
				if(h + NODE_COST >= G)
					open.removeNode(openNode);
			}
			for(Node graphNode: graph) {
				x1 = graphNode.getAttribute("x");
				y1 = graphNode.getAttribute("y");
				x2 = goalNode.getAttribute("x");
				y2 = goalNode.getAttribute("y");
				h = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
				fwn = NODE_COST + (w * h);
				graphNode.setAttribute("fwn", fwn);
			}
			
		}
		
		// create display window (visualization)
		TestPaintComponent frame = new TestPaintComponent(environmentSize, s, goal, incumbent, height, width, obstacles);
		frame.setTitle("Visualization");
		frame.setSize(1500, 1000);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		double endTime = System.currentTimeMillis() - startTime;
		System.out.println("Runtime: " + endTime + " ms");
	
	}
	
}
