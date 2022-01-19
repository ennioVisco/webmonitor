package at.ac.tuwien.trustcps.grid;

import eu.quanticol.moonlight.formula.DoubleDistance;
import eu.quanticol.moonlight.signal.DistanceStructure;
import eu.quanticol.moonlight.signal.GraphModel;
import eu.quanticol.moonlight.signal.SpatialModel;
import eu.quanticol.moonlight.util.Pair;
import eu.quanticol.moonlight.util.TestUtils;
import kotlin.collections.ArrayDeque;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * We are assuming the Subway network is part of an N x N at.ac.tuwien.cps.grid,
 * where all the edges represent the same distance.
 */
public class Grid {
    private Grid() {}

    public static GraphModel<Integer> getModel(int rows, int columns) {
        int totalSize = rows * columns;
        return createSpatialModel(totalSize, columns);
    }

    /**
     * Generates an N x M grid and returns a SpatialModel derived from it
     * @param n the N dimension of the N x M Grid
     * @param m the N dimension of the N x M Grid
     * @return an N-M-Grid spatial model
     */
    public static GraphModel<Integer> generateGrid(int n, int m) {
        int totalSize = n * m;
//        Map<Pair<Integer, Integer>, Integer> gridMap = new HashMap<>();
//        List<List<Integer>> grid = new ArrayList<>();
//        // the weight is constant and is 1
//
//        for(int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                List<Integer> ns = getNeighbours(i, j, m);
//                grid.add(toArray(i, j, m), ns);
//                for (int neighbour : ns) {
//                    gridMap.put(new Pair<>(toArray(i, j, m), neighbour), 1);
//                }
//            }
//        }
        return createSpatialModel(totalSize, m);
    }


    public static GraphModel<Integer> createSpatialModel(int size, int cols) {
        GraphModel<Integer> model = new GraphModel<>(size);

        for(int i = 0; i < size; i++) {
            List<Integer> ns = getNeighbours2(i, size, cols);
            for(Integer neighbour : ns) {
                model.add(i, 1, neighbour);
            }
        }

        return model;
    }

    private static List<Integer> getNeighboursArray(int node, int m) {
        Pair<Integer, Integer> coords = fromArray(node, m);
        return getNeighbours(coords.getFirst(), coords.getSecond(), m);
    }

    public static List<Integer> getNeighbours2(int node, int size, int cols) {
        List<Integer> neighbours = new ArrayDeque<>(4);

        // bot boundary
        if(node + cols < size)
            neighbours.add(node + cols);

        // top boundary
        if(node - cols >= 0)
            neighbours.add(node - cols);

        // left border
        if(node % cols == 0) {
            neighbours.add(node + 1);
        // right border
        } else if(node % cols == cols - 1) {
            neighbours.add(node - 1);
        // others
        } else {
            neighbours.add(node - 1);
            neighbours.add(node + 1);
        }

        return neighbours;
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
        List<Integer> neighbours = new ArrayList<>(8);


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
    private static Pair<Integer, Integer> fromArray(int a, int m) {
        int i = a % m;
        int j = a / m;
        return new Pair<>(i, j);
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
