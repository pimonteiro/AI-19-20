package Util;

import Logic.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class FindShortestPath {
    // Below arrays details all 4 possible movements from a cell
    private static int[] row = { -1, 0, 0, 1 };
    private static int[] col = { 0, -1, 1, 0 };
    private static List<Position> path = new ArrayList<>();


    // Find shortest route in the matrix from source cell to destination cell
    public static Node findPath(int matrix[][], World world, int x, int y, int x_dest, int y_dest) {
        // create a queue and enqueue first node
        Queue<Node> q = new ArrayDeque<>();
        Node src = new Node(x, y, null);
        q.add(src);

        // set to check if matrix cell is visited before or not
        Set<String> visited = new HashSet<>();

        String key = src.getX() + "," + src.getY();
        visited.add(key);

        // run till queue is not empty
        while (!q.isEmpty())
        {
            // pop front node from queue and process it
            Node curr = q.poll();
            int i = curr.getX(), j = curr.getY();

            // return if destination is found
            if (i == x_dest && j == y_dest) {
                return curr;
            }

            // check all 4 movements from current cell
            // and recur for each valid movement
            for (int k = 0; k < 4; k++) {
                // value of velocity
                int n = matrix[i][j];
                //check for max velocity and to min velocity if necessary
                for (; n > 0; n--) {
                    // get next position coordinates using value of velocity
                    x = i + row[k] * n;
                    y = j + col[k] * n;

                    // check if it is possible to go to next position
                    // from current position
                    if (world.isValid(x, y)) {
                        // construct next cell node
                        Node next = new Node(x, y, curr);

                        key = next.getX() + "," + next.getY();

                        // if it not visited yet
                        if (!visited.contains(key)) {
                            // push it into the queue and mark it as visited
                            q.add(next);
                            visited.add(key);
                        }
                    }
                }
            }
        }

        // return null if path is not possible
        return null;
    }

    // Utility function to print path from source to destination
    private static int printPath(Node node) {
        if (node == null) {
            return 0;
        }
        int len = printPath(node.getParent());
        path.add(new Position(node.getX(),node.getY()));
        return len + 1;
    }

    public static Pair<Integer, Position> findShortestPath(World world, Position current, Position destination, int velocity) {
        int dimension = World.dimension;
        int[][] matrix = new int[dimension][dimension];

        for (int r = 0; r < dimension; r ++) {
            for (int c = 0; c < dimension; c++) {
                matrix[r][c] = velocity;
            }
        }

        // Find a route in the matrix from source cell to destination cell
        Node node = findPath(matrix, world, current.getX(), current.getY(), destination.getX(), destination.getY());

        int len = printPath(node) - 1;

        if (node != null) {
            System.out.print("Shortest path is: ");
            for(Position p: path){
                System.out.print("(" + p.getX() + "," + p.getY() + ") ");
            }
            System.out.println("\nShortest path length is " + len);

            return new Pair<>(len, path.get(1));
        } else {
            System.out.println("Destination not found");
            return null;
        }
    }
}
