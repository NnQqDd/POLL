import java.util.*;
import java.math.*;

/*
    dir: direction
    col: column
    dist: distance
    pos: position
*/


class Light{
    public final int row;
    public final int col;
    public final int rowDir;
    public final int colDir;
    public Light(int row, int col, int rowDir, int colDir) {
        this.row = row;
        this.col = col;
        this.rowDir = rowDir;
        this.colDir = colDir;
    }
    public int[] getDirection(int i){ //Matrix coordinate
        int[] dir = new int[]{0, 0};
        if(i == MC.LBCM){
            if(rowDir == 1 && colDir == 0){
                dir[0] = 0;
                dir[1] = 1;
            }
            if(rowDir == 0 && colDir == -1){
                dir[0] = -1;
                dir[1] = 0;
            }
        } else if(i == MC.LTCM){
            if(rowDir == -1 && colDir == 0){
                dir[0] = 0;
                dir[1] = 1;
            }
            if(rowDir == 0 && colDir == -1){
                dir[0] = 1;
                dir[1] = 0;
            }
        } else if(i == MC.RTCM){
            if(rowDir == -1 && colDir == 0){
                dir[0] = 0;
                dir[1] = -1;
            }
            if(rowDir == 0 && colDir == 1){
                dir[0] = 1;
                dir[1] = 0;
            }
        } else if(i == MC.RBCM){
            if(rowDir == 1 && colDir == 0){
                dir[0] = 0;
                dir[1] = -1;
            }
            if(rowDir == 0 && colDir == 1){
                dir[0] = -1;
                dir[1] = 0;
            }
        }
        return dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, rowDir, colDir);
    }

    @Override
    public boolean equals(Object obj) {
        Light other = (Light) obj;
        return row == other.row && col == other.col && rowDir == other.rowDir && colDir == other.colDir;
    }
    public Position getPosition(){
        return new Position(row, col);
    }
    @Override
    public String toString(){
        return "(" + row + ", " + col + ", " + rowDir + ", " + colDir + ")";
    }
}

public class Solver{
    private int[] dr = {1, -1, 0, 0};
    private int[] dc = {0, 0, 1, -1};
    private int[][]matrix;
    private int height, width;
    private HashSet<Light> rootStates;
    private HashSet<Light> memoStates;
    private HashMap<Light, Integer> gValue;
    private HashMap<Light, Light> parent;
    private ArrayDeque<Light> dqState;
    public Solver(int [][]map) throws Exception{ // Get a copy of map
        if(map[0].length == 0) 
            throw new Exception("Length of map is zero.");
        this.height = map.length;
        this.width = map[0].length;
        this.matrix = new int[height][width];
        this.rootStates = new HashSet<>();
        this.memoStates = new HashSet<>();
        this.gValue = new HashMap<>();
        this.parent = new HashMap<>();
        this.dqState = new ArrayDeque<>();
        for (int i = 0; i < this.height; i++)
            for (int j = 0; j < this.width; j++){
                if(MC.isNull(map[i][j])) 
                    throw new Exception("Undefined tile at index " + i + ", " + j);
                if(map[i][j] == MC.SOURCE){   
                    this.matrix[i][j] = MC.NONE;
                } else this.matrix[i][j] = map[i][j];
                if(map[i][j] == MC.GOAL){
                    for(int k = 0; k < 4; k++){
                        Light state = new Light(i, j, dr[k], dc[k]);
                        this.rootStates.add(state);
                        this.parent.put(state, state);
                        this.gValue.put(state, 0);
                        this.dqState.add(state);
                    }
                } 
            }
    }
    private boolean isBounded(int row, int col){
        return row >= 0 && row < height && col >= 0 && col < width;
    }
    private int abs(int n){
        return (n>=0)?n:-n;
    }
    private int moveCost(Light state, int rowDir, int colDir){
        if(state.rowDir == rowDir && state.colDir == colDir)
            if(this.isBounded(state.row + rowDir, state.col + colDir) && !MC.isFullBlock(matrix[state.row + rowDir][state.col + colDir]))
                return 0;
        if(this.abs(state.rowDir-rowDir) == 1 && this.abs(state.colDir-colDir) == 1)
            if(this.isBounded(state.row + rowDir, state.col + colDir)  && MC.isTurnable(matrix[state.row][state.col]) && !MC.isFullBlock(matrix[state.row + rowDir][state.col + colDir]))
                return 1;
        return MC.MAX_VALUE;
    }
    private int min(int a, int b){
        return (a < b) ? a : b;
    }
    private void bfs01(Light target){
        while(!dqState.isEmpty()){
            Light state = dqState.removeFirst();
            memoStates.add(state); 
            if(state.equals(target)) return;
            if(MC.isHalfMirror(matrix[state.row][state.col])){
                int []dir = state.getDirection(matrix[state.row][state.col]);
                if(
                    (dir[0] == 0 && dir[1] == 0)
                    || !this.isBounded(state.row + dir[0], state.col + dir[1])
                    || MC.isFullBlock(matrix[state.row + dir[0]][state.col + dir[1]])
                ) continue;
                Light tState = new Light(state.row + dir[0], state.col + dir[1], dir[0], dir[1]); 
                parent.put(tState, state);
                gValue.put(tState, gValue.get(state));
                dqState.addFirst(tState);
                continue;
            }
            for(int i = 0; i < 4; i++){
                Light newState = new Light(state.row + dr[i], state.col + dc[i], dr[i], dc[i]);
                int turn = this.moveCost(state, dr[i], dc[i]);
                if(turn == MC.MAX_VALUE) continue;
                int newDist = gValue.get(state) + turn;
                if(gValue.getOrDefault(newState, MC.MAX_VALUE) > newDist){
                    gValue.put(newState, newDist);
                    parent.put(newState, state);
                    if(turn == 1) dqState.addLast(newState);
                    else dqState.addFirst(newState);
                }
            }
        }
        return;
    }
    private ArrayList<Position> backPath(Light state){
        ArrayList<Position> path = new ArrayList<>();
        while(true){
            path.add(state.getPosition());
            state = parent.get(state);
            if(rootStates.contains(state)) {
                path.add(state.getPosition());
                break;
            }
        }
        return path;
    }

/*    public ArrayList<Light> backBackPath(int x, int y, int z, int t){
        this.searchDirection(x, y, z, t);
        Light state = new Light(x, y, z, t);
        ArrayList<Light> path = new ArrayList<>();
        while(true){
            path.add(state);
            state = parent.getOrDefault(state, null);
            if(state == null) break;
            if(rootStates.contains(state)) {
                path.add(state);
                break;
            }
        }
        return path;
    }*/

    private void searchDirection(int row, int col, int rowDir, int colDir){
        if(!this.isBounded(row, col) || MC.isFullBlock(matrix[row][col])) 
            return;
        if(rowDir*rowDir + colDir*colDir != 1) 
            return;
        Light state = new Light(row, col, rowDir, colDir);
        if(!memoStates.contains(state));
            bfs01(state);
    }
    private void searchDirections(int row, int col){
        if(!this.isBounded(row, col) || MC.isFullBlock(matrix[row][col])) 
            return;
        for(int i = 0; i < 4; i++){
            Light state = new Light(row, col, dr[i], dc[i]);
            if(!memoStates.contains(state))
                bfs01(state);
        }
    }
    private Light getNearestState(int row, int col){ // Nearest state to destination, already call bfs01
        if(!this.isBounded(row, col) || MC.isFullBlock(matrix[row][col])) 
            return null;
        this.searchDirections(row, col);
        Light nearestState = null; 
        int currDist = MC.MAX_VALUE;
        for(int i = 0; i < 4; i++){
            Light state = new Light(row, col, dr[i], dc[i]);
            int dist = gValue.getOrDefault(state, MC.MAX_VALUE);
            if(currDist > dist){
                currDist = dist;
                nearestState = state;
            }
        }
        return nearestState;      
    }

    /*** MAIN METHODS ***/
    
    public int solvedStateCount(){
        return memoStates.size();
    }

    /* Get path in any direction*/

    public int getNearestStatePathLength(int targetRow, int targetCol){
        Light state = this.getNearestState(targetRow, targetCol);
        return (state == null)?MC.MAX_VALUE:gValue.get(state);
    }
    public ArrayList<Position> getPath(int targetRow, int targetCol){
        this.searchDirections(targetRow, targetCol);
        Light state = this.getNearestState(targetRow, targetCol);
        return (state == null)?null:this.backPath(state);
    }
    public ArrayList<Position> getPlaceableMirrors(int targetRow, int targetCol){
        this.searchDirections(targetRow, targetCol);
        Light state = this.getNearestState(targetRow, targetCol);
        if(state == null) return null;
        ArrayList<Position> solution = this.backPath(state);
        ArrayList<Position> pMirrors = new ArrayList<>();
        for(int i = 2; i < solution.size(); i++){
            if(solution.get(i).row == solution.get(i - 1).row 
                && solution.get(i - 1).row == solution.get(i - 2).row) continue;
            if(solution.get(i).col == solution.get(i - 1).col 
                && solution.get(i - 1).col == solution.get(i - 2).col) continue;
            if(!MC.isHalfMirror(matrix[solution.get(i - 1).row][solution.get(i - 1).col]))
                pMirrors.add(solution.get(i - 1));
        }
        return pMirrors;
    }

    /* Get path in a specific direction*/

    public int getNearestStatePathLength(int targetRow, int targetCol, int rowDir, int colDir){
        this.searchDirection(targetRow, targetCol, -rowDir, -colDir);
        Light state = new Light(targetRow, targetCol, -rowDir, -colDir);
        return gValue.getOrDefault(state, MC.MAX_VALUE);
    }
    public ArrayList<Position> getPath(int targetRow, int targetCol, int rowDir, int colDir){
        this.searchDirection(targetRow, targetCol, -rowDir, -colDir);
        Light state = new Light(targetRow, targetCol, -rowDir, -colDir);
        if(!memoStates.contains(state))
            return null;
        return this.backPath(state);
    }
    public ArrayList<Position> getPlaceableMirrors(int targetRow, int targetCol, int rowDir, int colDir){
        this.searchDirection(targetRow, targetCol, -rowDir, -colDir);
        Light state = new Light(targetRow, targetCol, -rowDir, -colDir);
        if(!memoStates.contains(state))
            return null;
        ArrayList<Position> solution = this.backPath(state);
        ArrayList<Position> pMirrors = new ArrayList<>();
        for(int i = 2; i < solution.size(); i++){
            if(solution.get(i).row == solution.get(i - 1).row 
                && solution.get(i - 1).row == solution.get(i - 2).row) continue;
            if(solution.get(i).col == solution.get(i - 1).col 
                && solution.get(i - 1).col == solution.get(i - 2).col) continue;
            if(!MC.isHalfMirror(matrix[solution.get(i - 1).row][solution.get(i - 1).col]))
                pMirrors.add(solution.get(i - 1));
        }
        return pMirrors;
    }
} // Ignore blocks.

/*
A Star
- Admissability and consistency: Least turns when there are no obstacle.
Why not A Star
- May need to run multiple times (without memory) in a level.
*/
 

