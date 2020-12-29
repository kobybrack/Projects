import heap.Heap;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/** Author: Koby Brackebusch
 * Date: 11/29/20
 * Purpose: to implement Dijkstra's algorithm to find the shortest paths
 * in a graph read from a text file */

/** Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 *   Graph g = // create your graph
 *   ShortestPaths sp = new ShortestPaths();
 *   Node a = g.getNode("A");
 *   sp.compute(a);
 *   Node b = g.getNode("B");
 *   LinkedList<Node> abPath = sp.getShortestPath(b);
 *   double abPathLength = sp.getShortestPathLength(b);
 *   */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node,PathData> paths;

    /** Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * backpointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.*/
    public void compute(Node origin) {
        paths = new HashMap<Node,PathData>();
        HashSet<Node> settled = new HashSet <Node> ();
        Heap<Node, Double> frontier = new Heap<Node, Double>();
        paths.put(origin, new PathData(0, null));
        frontier.add(origin, 0.0);
        while (frontier.size() != 0) {
            Node f = frontier.poll();
            settled.add(f);
            HashMap<Node, Double> neighbors = f.getNeighbors();
            Set<Node> keys = neighbors.keySet();
            for (Node w : keys) {
                double fDistance = paths.get(f).distance;
                double wDistance = neighbors.get(w);
                if (!settled.contains(w) && !frontier.contains(w)) {
                    PathData addThis = new PathData(fDistance + wDistance, f);
                    paths.put(w, addThis);
                    frontier.add(w, fDistance + wDistance);
                } else if (fDistance + wDistance < paths.get(w).distance) {
                    if (frontier.contains(w)) {
                        frontier.changePriority(w, fDistance + wDistance);
                    }
                    PathData addThis = new PathData(fDistance + wDistance, f);
                    paths.put(w, addThis);
                }
            }
        }
    }

    /** Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public double shortestPathLength(Node destination) {
        if (paths.containsKey(destination)) {
            return paths.get(destination).distance;
        }
        return -1;
    }

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public LinkedList<Node> shortestPath(Node destination) {
        LinkedList<Node> list = new LinkedList<Node>();
        while (paths.get(destination) != null) {
            list.addFirst(destination);
            destination = paths.get(destination).previous;
        }
        return list;
    }


    /** Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node. */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
    }


    /** Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a DB1B CSV file with
     * flight data. See GraphParser, BasicParser, and DB1BParser for more.*/
    protected static Graph parseGraph(String fileType, String fileName) throws
        FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
        // read command line args
        String fileType = args[0];
        String fileName = args[1];
        String origCode = args[2];


        String destCode = null;
        if (args.length == 4) {
            destCode = args[3];
        }

        //parse a graph with the given type and filename
        Graph graph;
        try {
            graph = parseGraph(fileType, fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file " + fileName);
            return;
        }
        graph.report();
        ShortestPaths test = new ShortestPaths();
        test.compute(graph.getNode(origCode));
        if (destCode != null) {  
            LinkedList<Node> list = test.shortestPath(graph.getNode(destCode));
            while (list.size() != 0) {
                Node head = list.poll();
                System.out.print(head + " ");
            }
            double pathLength = test.shortestPathLength(graph.getNode(destCode));
            if (pathLength == -1.0) {
                System.out.println("No path exists from " + origCode + " to " + destCode + ".");
            } else {
                System.out.print(pathLength);
            }
        } else {
            System.out.println("Shortest Paths From " + origCode + ":");
            for (Node key : test.paths.keySet()) {
                System.out.println(key + ": " + test.paths.get(key).distance);
            }
        }
    }

    


}
