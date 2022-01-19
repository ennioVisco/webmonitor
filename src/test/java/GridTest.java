import at.ac.tuwien.trustcps.grid.Grid;
import eu.quanticol.moonlight.signal.GraphModel;
import eu.quanticol.moonlight.signal.SpatialModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GridTest {
    @Test
    void test3() {
        GraphModel<Integer> grid = Grid.generateGrid(3, 3);

        GraphModel<Integer> stub = stubGrid();

        assertEquals(grid.size(), stub.size());
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                //System.out.println("("+ i + ", " + j + ")");
                Integer left = grid.get(i, j);
                Integer right = stub.get(i, j);
                assertEquals(left, right);
            }
        }
    }

    GraphModel<Integer> stubGrid() {
        GraphModel<Integer> model = new GraphModel<>(9);

        // 0
        model.add(0, 1, 1);
        model.add(0, 1, 3);
        // 1
        model.add(1, 1, 0);
        model.add(1, 1, 2);
        model.add(1, 1, 4);
        // 2
        model.add(2, 1, 1);
        model.add(2, 1, 5);
        // 3
        model.add(3, 1, 0);
        model.add(3, 1, 4);
        model.add(3, 1, 6);
        // 4
        model.add(4, 1, 1);
        model.add(4, 1, 3);
        model.add(4, 1, 5);
        model.add(4, 1, 7);
        // 5
        model.add(5, 1, 2);
        model.add(5, 1, 4);
        model.add(5, 1, 8);
        // 6
        model.add(6, 1, 3);
        model.add(6, 1, 7);
        // 7
        model.add(7, 1, 4);
        model.add(7, 1, 6);
        model.add(7, 1, 8);
        // 8
        model.add(8, 1, 5);
        model.add(8, 1, 7);

        return model;
    }
}
