import java.util.*;

public class GFG {

	private int dist[];//Distance Array
	private Set<Integer> settled; // Set of Visited Nodes
	private Character[] prev;// Antecedent array
	private PriorityQueue<Node> pq; // Priority Queue
	private int V;// Number of Nodes
	List<List<Node>> adj;// List of adjacents of a given node
	ArrayList<Character> nodeNames = new ArrayList<>();// ArrayList of character node names

	// Initialize GFG Class and fill the nodeNames ArrayList with characters in alphabetical order
	public GFG(int V) {
		this.V = V;
		dist = new int[V]; // dist is an Array with the size equal to the number of nodes
		settled = new HashSet<>(); // settled is HashSet keeping track of visited nodes
		prev = new Character[V];
		pq = new PriorityQueue<>(V, new Node());
		nodeNames.add('A'); nodeNames.add('B'); nodeNames.add('C'); nodeNames.add('D'); nodeNames.add('E'); nodeNames.add('F'); nodeNames.add('G'); nodeNames.add('H');
		nodeNames.add('I'); nodeNames.add('J'); nodeNames.add('K'); nodeNames.add('L'); nodeNames.add('M'); nodeNames.add('N'); nodeNames.add('O'); nodeNames.add('P');
	}

	// Dijkstra's Algorithm
	public double dijkstra(List<List<Node> > adj, int src,int end) {
		this.adj = adj; // Initialize the adj List

		for (int i = 0; i < V; i++) // Initialize every node int the dist array with the distance of MAX_VALUE
			dist[i] = Integer.MAX_VALUE;

		//Change the dist value of the starting node to 0
		dist[src] = 0;

		// Add the starting node to the priority queue
		pq.add(new Node(nodeNames.get(src),src, 0));

		while (!pq.isEmpty()) {

			// Retrieve the node with the minimal value
			int u = pq.remove().node;

			if(u==end) // if we already reached the destination return
				return dist[end];

			if (settled.contains(u)) // if we already visited this node, skip
				continue;

			settled.add(u); // Add node the antecedents array

			e_Neighbours(u); // Calculate dist to every neighbour of u
		}
		return Integer.MAX_VALUE;
	}

	// Function for calculating the distance to the neighbours for a given node
	private void e_Neighbours(int u) {

		int edgeDistance = -1;
		int newDistance = -1;

		// Iterate over every neighbour of u
		for (int i = 0; i < adj.get(u).size(); i++) {
			Node v = adj.get(u).get(i);

			if (!settled.contains(v.node)) { // if the v doesnt exist in the antecedent array, then calculate the distance
				edgeDistance = v.cost; // cost from u to v
				newDistance = dist[u] + edgeDistance; // cost from starting node to v


				if (newDistance < dist[v.node]) { // if the new distance is less than the distance given in the dist array fro node v, then update
					dist[v.node] = newDistance; // update optimal distance
					prev[v.node] = nodeNames.get(u); // update antecedent of node v
				}

				// Add v node to the priority queue
				pq.add(new Node(v.name, v.node, dist[v.node]));
			}
		}
	}

	public List<Character> reconstructPath(int start, int end, List<List<Node>> adj) {
	    if (end < 0 || end >= V) throw new IllegalArgumentException("Invalid node index"); // If the destination node is not between 0 or V throw an exception
	    if (start < 0 || start >= V) throw new IllegalArgumentException("Invalid node index"); // If the start node is not between 0 or V throw an exception
	    List<Character> path = new ArrayList<>(); // List of Characters containing the path from the start to the destination
	    double dist = dijkstra(adj,start, end); // Get the optimal dist to from the start to the distination
	    if (dist == Double.POSITIVE_INFINITY) return path; // if the dist is "POSITIVE_INFINITY" (unreachable node), then return empty path
	    for (Character at = nodeNames.get(end); at != null; at = prev[nodeNames.indexOf(at)]) path.add(at); // start from the destination node and get the antecedents until reaching a null value (start node)
	    Collections.reverse(path); // reverse path list so we obtain the path from start to destination, not from destination to start
	    return path;
	}

	// Main driver method
	public static void main(String arg[]) {
		int V;
		char start;
		char end;

		Scanner sc = new Scanner(System.in);

		System.out.print("Saisir le nombre totale de noeud :");
		V = sc.nextInt();
		System.out.print("Saisir le noeud de départ :");
		start = sc.next().charAt(0);

		GFG dpq = new GFG(V);


		List<List<Node> > adj = new ArrayList<List<Node>>();


		// Initialize the adjacency List for every node
		for (int i = 0; i < V; i++) {
			List<Node> item = new ArrayList<Node>();
			adj.add(item);
			System.out.println("Saisir le nombre des adjacent pour le noeud: " + dpq.nodeNames.get(i));
			int adjOfVert = sc.nextInt();
			if(adjOfVert == 0) continue;
			for (int j = 0; j < adjOfVert; j++) {
				System.out.println("Saisir le noeud adjacent et le cout: ");
				System.out.print("noeud: ");
				char name = sc.next().charAt(0);
				System.out.print("cout: ");
				int cost;
				do {
					cost = sc.nextInt();
					if(cost < 0) System.out.println("Veulliez saisir une valeur positive");
				}while(cost < 0);
				int node = dpq.nodeNames.indexOf(name);
				adj.get(i).add(new Node(name, node, cost));
			}
		}
		
		


		System.out.print("Saisir le noeud d'arrivée: ");
		end = sc.next().charAt(0);

		// Get the path to the destination node from the start node
		List<Character> path = dpq.reconstructPath(dpq.nodeNames.indexOf(start),dpq.nodeNames.indexOf(end),adj);

		// Print path
		System.out.print("le chemin de la start vers la destination : ");
		for (int i = 0; i <path.size(); i++) {
			if(i==path.size()-1)
				System.out.print(path.get(i));
			else
				System.out.print(path.get(i)+"->");
		}
		System.out.println(" avec une distance de " + dpq.dist[dpq.nodeNames.indexOf(end)]);
	}
}

// Node implements Comparator so the priority queue knows how to compare and get the minimal value
class Node implements Comparator<Node> {

	public int node; // Node Number
	public int cost; // Cost to this node
	public char name; // Node name

	public Node() {}

	public Node(char name,int node, int cost) {
		this.name = name;
		this.node = node;
		this.cost = cost;
	}


	@Override public int compare(Node node1, Node node2) {

		if (node1.cost < node2.cost)
			return -1;

		if (node1.cost > node2.cost)
			return 1;

		return 0;
	}
}

