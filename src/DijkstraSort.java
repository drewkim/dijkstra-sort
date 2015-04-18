import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*********************************************************************************************
 * Name: Drew Kim
 * Date: 2.5.15
 * Block: A
 * Program: Dijkstra Sort
 * Description: This is an implementation of the Dijkstra Algorithm. The context is computing
 * the easiest way for a rumor to make its way from one person to another. The program works
 * by prompting the user to enter a source name and a target name, and then running the
 * algorithm with the data set provided.
 * Resources: I used http://www.algolist.com/code/java/Dijkstra%27s_algorithm for the layout
 * of the data structures and basics of the algorithm.
 *********************************************************************************************/
public class DijkstraSort
{
	private static Scanner console;
	private static String cityFrom = "";
	private static String cityTo = "";

	/**
	 * Main method. Runs the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		console = new Scanner(System.in);
		Scanner file = getFile();
		int numNodes = file.nextInt();
		file.nextLine();
		String[] names = fillNames(file, numNodes);
		int[][] graph = fillGraph(file, numNodes);

		Node[] nodes = initializeNodes(names, numNodes);
		initializeEdges(nodes, graph);

		getCities(names);

		List<Node> path = computePath(nodes[searchInArray(nodes, cityFrom)], nodes[searchInArray(nodes, cityTo)]);
		System.out.println("The the easiest way for a rumor started by " + cityFrom + " to make its way to " + cityTo + "'s ears is: " + path);
		System.out.println("The amount of coercing needed to happen is " + nodes[searchInArray(nodes, cityTo)].nodeWeight + " begging sessions");
	}

	/**
	 * Creates, fills, and returns an array of the names of nodes based on a Scanner file.
	 * 
	 * @param file
	 * @param numNodes
	 * @return
	 */
	private static String[] fillNames(Scanner file, int numNodes)
	{
		String[] names = new String[numNodes];
		for(int i = 0; i < numNodes; i++)
			names[i] = file.next();
		return names;
	}
	
	/**
	 * Creates, fills and returns a 2D array of the weights of the nodes.
	 * 
	 * @param file
	 * @param numNodes
	 * @return
	 */
	private static int[][] fillGraph(Scanner file, int numNodes)
	{
		int[][] graph = new int[numNodes][numNodes];
		int j = 0;
		while(file.hasNext())
		{
			file.nextLine();
			for(int i = 0; i < numNodes; i++)
			{
				int num = file.nextInt();
				if(num != 0)
					graph[j][i] = num;
			}
			j++;
		}
		return graph;
	}
	
	/**
	 * Prompts the user to enter the source and target nodes.
	 * 
	 * @param names
	 */
	private static void getCities(String[] names)
	{
		while(!isValidCity(cityFrom, names) || !isValidCity(cityTo, names))
		{
			System.out.println("Enter the person starting the rumor:");
			cityFrom = console.nextLine();
			System.out.println("Enter the person recieving the rumor:");
			cityTo = console.nextLine();
		}
	}
	
	/**
	 * Creates, initializes, and fills the array of nodes with each node's adjacencies.
	 * 
	 * @param nodes
	 * @param graph
	 */
	private static void initializeEdges(Node[] nodes, int[][] graph)
	{
		for(int i = 0; i < nodes.length; i++)
		{
			Node n = nodes[i];
			for(int j = 0; j < nodes.length; j++)
			{
				if(j != i)
					n.edges.add(new Edge(nodes[j], graph[i][j]));
			}
		}
	}

	/**
	 * Creates, initializes, fills, and returns an array of nodes
	 * 
	 * @param names
	 * @param numNodes
	 * @return
	 */
	private static Node[] initializeNodes(String[] names, int numNodes)
	{
		Node[] nodes = new Node[numNodes];

		for(int i = 0; i < numNodes; i++)
		{
			nodes[i] = new Node(names[i]);
		}
		return nodes;
	}

	/**
	 * Implements the Dijkstra Algorithm to compute the shortest path. Returns a List of the shortest path.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Node> computePath(Node start, Node end)
	{
		start.nodeWeight = 0.;
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(start);
		while (!queue.isEmpty())
		{
			//Pulls the lightest Node from the queue
			Node current = queue.poll();
			
			//If the node pulled is the target node, algorithm has finished. Return the path
			if(current.equals(end))
			{
				List<Node> path = new ArrayList<Node>();
				for (Node vertex = end; vertex != null; vertex = vertex.previous)
					path.add(vertex);
				Collections.reverse(path);
				return path;
			}
			
			//Otherwise, iterate through the children of the pulled node and update the weights with the lightest ones.
			else
			{
				for (Edge e : current.edges)
				{
					Node n = e.to;
					double weight = e.weight;
					if(weight != 0)
					{
						double newWeight = current.nodeWeight + weight;
						if(newWeight < n.nodeWeight) 
						{
							queue.remove(n);
							n.nodeWeight = newWeight;
							n.previous = current;
							queue.add(n);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Checks to see if a String matches a name in the data set
	 * 
	 * @param city
	 * @param names
	 * @return
	 */
	private static boolean isValidCity(String city, String[] names)
	{
		for(int i = 0; i < names.length; i++)
		{
			if(city.equals(names[i]))
				return true;
		}
		return false;
	}

	/**
	 * Searches in the array of nodes for a name.
	 * 
	 * @param nodes
	 * @param str
	 * @return
	 */
	private static int searchInArray(Node[] nodes, String str)
	{		
		for(int i = 0; i < nodes.length; i++)
		{
			if(str.equals(nodes[i].toString()))
				return i;
		}
		return -1;
	}

	/**
	 * Retrieves the data from a text file.
	 * 
	 * @return
	 */
	private static Scanner getFile()
	{
		Scanner console = null;
		try 
		{
			console = new Scanner(new BufferedReader(new FileReader("data.txt")));
		}
		catch (IOException e)
		{	
			System.out.println("File error - file not found, could not be opened or could not be read.");
		}
		return console;
	}
}

/*********************************************************************************************
 * Node class. Holds parameters for a name, adjacencies, minimum distance, and previous node.
 *********************************************************************************************/
class Node implements Comparable<Node>
{
	public final String name;
	public ArrayList<Edge> edges = new ArrayList<Edge>();
	public double nodeWeight = Double.POSITIVE_INFINITY;
	public Node previous;

	/**
	 * Constructor. Gives the node a name.
	 * @param nameIn
	 */
	public Node(String nameIn) 
	{ 
		name = nameIn; 
	}

	/**
	 * ToString method. Returns the name of the node.
	 */
	public String toString() 
	{ 
		return name; 
	}

	/**
	 * CompareTo method. Compares the minimum distances of two nodes.
	 */
	public int compareTo(Node nodeIn)
	{
		return Double.compare(nodeWeight, nodeIn.nodeWeight);
	}
}

/*********************************************************************************************
 * Edge class. Holds parameters for a node pointing to and weight.
 *********************************************************************************************/
class Edge
{
	public final Node to;
	public final double weight;

	/**
	 * Constructor. Gives the edge the node its pointing to and the weight.
	 * 
	 * @param targetIn
	 * @param weightIn
	 */
	public Edge(Node targetIn, double weightIn)
	{ 
		to = targetIn; 
		weight = weightIn; 
	}
}