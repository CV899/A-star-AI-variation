import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;

public class ImproveSolution {
	
	// variables
	MultiGraph neighbors;
	MultiGraph path = new MultiGraph("path");
	MultiGraph open = new MultiGraph("open");
	final int NODE_COST = 1; // NODE_COST = g in [fwn = g + wh]
	int x;
	int y;
	int x1;
	int x2;
	int y1;
	int y2;
	final double C_N_to_N_PRIME = 1;
	final double G_N = 1;
	double g_n_prime = 1;
	double currentFWN;
	double tempFWN;
	double cost;
	double fwn;
	double h_n_prime;
	boolean hasNode = false;
	Node tempNode;
	Node currentNode;
	Node goalNode;

	// **algorithm 4 main implementation**
	public MultiGraph getSolution(MultiGraph open, double w, double G, MultiGraph graph, Node goalNode) {
		
		this.open = open;
		
		while(open.getNodeCount() != 0) {
			
			// pop the node with the lowest g + wh
			currentNode = open.getNode(0);
			currentFWN = currentNode.getAttribute("fwn");
			
			for(Node openNode: open) {
				tempFWN = openNode.getAttribute("fwn");
				if(tempFWN < currentFWN) {
					currentFWN = tempFWN;
					currentNode = openNode;
				}
			}
			
			open.removeNode(currentNode);
			
			// check if G is w-admissible
			if(G <= currentFWN)
				return null;
			
			// add popped node to path
			x = currentNode.getAttribute("x");
			y = currentNode.getAttribute("y");
			for(Node pathNode: path) {
				if(pathNode.getId().equals(currentNode.getId())) { 
					hasNode = true;
				}
			}
			if(!hasNode) {
				path.addNode(currentNode.getId());
				tempNode = path.getNode(currentNode.getId());
				tempNode.addAttribute("x", x);
				tempNode.addAttribute("y", y);
			}
			hasNode = false;
			
			// get neighbors of popped node
			neighbors = new MultiGraph("neighbors");
			tempNode = graph.getNode("(" + (x + 1) + "," + y + ")"); // up
			if(tempNode != null) {
				tempFWN = tempNode.getAttribute("fwn");
				neighbors.addNode(tempNode.getId());
				tempNode = neighbors.getNode(tempNode.getId());
				tempNode.setAttribute("x", (x + 1));
				tempNode.setAttribute("y", y);
				tempNode.setAttribute("fwn", tempFWN);
			}
			
			tempNode = graph.getNode("(" + (x - 1) + "," + y + ")"); // down
			if(tempNode != null) {
				tempFWN = tempNode.getAttribute("fwn");
				neighbors.addNode(tempNode.getId());
				tempNode = neighbors.getNode(tempNode.getId());
				tempNode.setAttribute("x", (x - 1));
				tempNode.setAttribute("y", y);
				tempNode.setAttribute("fwn", tempFWN);
			}
			
			tempNode = graph.getNode(("(" + x + "," + (y + 1) + ")")); // right
			if(tempNode != null) {
				tempFWN = tempNode.getAttribute("fwn");
				neighbors.addNode(tempNode.getId());
				tempNode = neighbors.getNode(tempNode.getId());
				tempNode.setAttribute("x", x);
				tempNode.setAttribute("y", (y + 1));
				tempNode.setAttribute("fwn", tempFWN);
			}
			
			tempNode = graph.getNode("(" + x + "," + (y - 1) + ")"); // left
			if(tempNode != null) {
				tempFWN = tempNode.getAttribute("fwn");
				neighbors.addNode(tempNode.getId());
				tempNode = neighbors.getNode(tempNode.getId());
				tempNode.setAttribute("x", x);
				tempNode.setAttribute("y", (y - 1));
				tempNode.setAttribute("fwn", tempFWN);
			}
			
			// foreach successor n prime of n do:
			for(Node neighborNode: neighbors) {
				
				// check if neighborNode is an element of open
				for(Node openNode: open) {
					if(neighborNode.getId().equals(openNode.getId())) {
						hasNode = true;
					}
				}
				
				if(!hasNode || NODE_COST + C_N_to_N_PRIME < g_n_prime) {
					
					g_n_prime = G_N + C_N_to_N_PRIME;
					// calculate h(n')
					x1 = neighborNode.getAttribute("x");
					y1 = neighborNode.getAttribute("y");
					x2 = goalNode.getAttribute("x");
					y2 = goalNode.getAttribute("y");
					h_n_prime = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
					
					if(g_n_prime + h_n_prime < G) {
						if(neighborNode.getId().equals(goalNode.getId())) {
							
							return path;	
						}
						else {
							open.addNode(neighborNode.getId());
							tempNode = open.getNode(neighborNode.getId());
							tempFWN = neighborNode.getAttribute("fwn");
							tempNode.setAttribute("fwn", tempFWN);
							x = neighborNode.getAttribute("x");
							tempNode.setAttribute("x", x);
							y = neighborNode.getAttribute("y");
							tempNode.setAttribute("y", y);
						}
					}
					
				}
				
				hasNode = false;
			}
			
		}
		
		return null;
	}
	
	public double cost(MultiGraph newSolution) { 
		
		cost = newSolution.getNodeCount();
		
		return cost;
	}
	
	public MultiGraph getOpen() {
		
		return open;
	}
	
}
