package at.ac.tuwien.trustcps.grid;

import eu.quanticol.moonlight.signal.DistanceStructure;
import eu.quanticol.moonlight.signal.GraphModel;
import eu.quanticol.moonlight.signal.SpatialModel;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * We are assuming the Subway network is part of an N x N at.ac.tuwien.cps.grid,
 * where all the edges represent the same distance.
 */
public class Grid {
    private Grid() {}

    public static SpatialModel<Integer> getModel(int rows, int columns) {
        return generateGrid(rows, columns);
    }

    /**
     * Generates an N x M grid and returns a SpatialModel derived from it
     * @param n the N dimension of the N x M Grid
     * @param m the N dimension of the N x M Grid
     * @return an N-M-Grid spatial model
     */
    public static SpatialModel<Integer> generateGrid(int n, int m) {
        Map<Pair, Integer> gridMap = new HashMap<>();

        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++) {
                List<Integer> ns = getNeighbours(i, j, m);
                for(int node : ns)
                    gridMap.put(new Pair(toArray(i,j, m) , node), 1);
            }

        return createSpatialModel(n * m, gridMap);
    }

    public static <T> SpatialModel<T> createSpatialModel(int size, BiFunction<Integer, Integer, T> edges) {
        GraphModel<T> model = new GraphModel<>(size);

        for(int i = 0; i < size; ++i) {
            for(int j = 0; j < size; ++j) {
                T value = edges.apply(i, j);
                if (value != null) {
                    model.add(i, value, j);
                }
            }
        }

        return model;
    }

    /**
     * Surroundings of the current node
     * @param i first coordinate of the current node
     * @param j second coordinate of the current node
     * @param m the number of columns of the matrix
     * @return a List of the serialized coordinates of the nodes.
     *
     * @see #toArray for details on the serialization technique
     */
    public static List<Integer> getNeighbours(int i, int j, int m) {
        List<Integer> neighbours = new ArrayList<>(4);


        // left boundary
        if(i > 0)
            neighbours.add(toArray(i - 1, j, m));

        // top boundary
        if(j > 0)
            neighbours.add(toArray(i, j - 1, m));

        // right boundary
        if(i < m - 1)
            neighbours.add(toArray(i + 1, j, m));

        // bottom boundary
        if(j < m - 1)
            neighbours.add(toArray(i, j + 1, m));
//        // top-left corner
//        if(x > 0 && y > 0)
//            neighbours.add(toArray(x - 1, y - 1, size));
//
//        // bottom-right corner
//        if(x < size - 1 && y < size - 1)
//            neighbours.add(toArray(x + 1, y + 1, size));
//
//        // top-right corner
//        if(x > 0 && y < size - 1)
//            neighbours.add(toArray(x - 1, y + 1, size));
//
//        // bottom-left corner
//        if(x < size - 1 && y > 0)
//            neighbours.add(toArray(x + 1, y - 1, size));
        return neighbours;
    }

    /**
     * Given a pair of coordinates of a node,
     * it returns their array-style version
     * @param i first coordinate of the node
     * @param j second coordinate of the node
     * @param m the number of columns of the matrix
     * @return an int corresponding to the serialized coordinates.
     */
    private static int toArray(int i, int j, int m) {
        return i + j * m;
    }

    /**
     * Given the dimension n of the square matrix, it converts a position in
     * the array to a position in the n x n matrix.
     * @param a position in the array
     * @param m the number of columns of the matrix
     * @return the pair (i,j) of coordinates in the matrix.
     */
    private static Pair fromArray(int a, int m) {
        int i = a % m;
        int j = a / m;
        return new Pair(i, j);
    }

    /**
     * It calculates the proper distance, given a spatial model
     *
     * @param lowerBound double representing the starting position
     * @param upperBound double representing the ending position
     * @return a DoubleDistance object, meaningful in the given Spatial Model
     */
    public static Function<SpatialModel<Integer>, DistanceStructure<Integer, ?>>
    distance(int lowerBound, int upperBound)
    {
        return g -> new DistanceStructure<>(x -> x,
                                            new IntegerDistance(),
                                            lowerBound, upperBound,
                                            g);
    }
}
