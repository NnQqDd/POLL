import java.io.*;
import java.util.*;
import java.math.*;


public class ExamplePuzzle {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
    	for(int i = -1; i <= 9; i++){
            System.out.println(String.format("Code %d means \"%s\".", i, MC.nameLookup(i)));
        }
        Solver polSolver = new Solver(new int[][]{ //13 rows x 11 columns
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {2, 2, 2, 2, 3, 0, 7, 2, 2, 2, 2},// Destination at row 6, column 5 (code 0)
            {3, 3, 3, 3, 3, 3, 5, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3}
        });
        // Get path from row 12, column 10 to the destination.
        System.out.println(polSolver.getPath(12, 10)); 
        // Get position of placeable mirrors.
        System.out.println(polSolver.getPlaceableMirrors(12, 10));
        // Get path length.
        System.out.println(polSolver.getNearestStatePathLength(12, 10));
        // Get number of solved states.
        System.out.println(polSolver.solvedStateCount());
        /* Matrix directions
        Left: (0, -1)
        Right: (0, 1)
        Up: (-1, 0)
        Down: (1, 0)
        */
        // Get path from row 12, column 10, direction (0, -1) (left) to the destination.
        System.out.println(polSolver.getPath(12, 10, 0, -1)); 
        // Get position of placeable mirrors.
        System.out.println(polSolver.getPlaceableMirrors(12, 10, 0, -1));
        // Get path length.
        System.out.println(polSolver.getNearestStatePathLength(12, 10, 0, -1));
        // Get number of solved states.
        System.out.println(polSolver.solvedStateCount());
        return;
    }
}
/* Matrix coordinates
-1 row: Up
+1 row: Down
-1 col: Right
+1 col: Left
*/
