import java.util.*;

class HeadLight{
	public final int row;
	public final int col;
	private final int[] headDir; //row, col
    public HeadLight(int row, int col, int drow, int dcol) {
        this.row = row;
        this.col = col;
        this.headDir = new int[]{drow, dcol};
    }
    @Override
    public boolean equals(Object obj) {
        HeadLight other = (HeadLight) obj;
        return row == other.row && col == other.col && headDir[0] == other.headDir[0] && headDir[1] == other.headDir[1];
    }
    public int[] getDirection(int type){ //row, col
    	int[] dir = new int[]{0, 0};
    	if(type == 0){
    		return dir;
    	}
        int sign = (type > 0)?1:-1;
    	dir[0] = headDir[0]*sign;
    	dir[1] = headDir[1]*sign;
    	return dir;
    }
}

class TileState{
	public final int row;
	public final int col;
	public final char tile;
	public TileState(int row, int col, char tile) {
        this.row = row;
        this.col = col;
        this.tile = tile;
    }
    @Override
    public boolean equals(Object obj) {
        TileState other = (TileState) obj;
        return row == other.row && col == other.col && tile == other.tile;
    }
}

public class Board{
    public static final int HALF_LIGHT = Character.MAX_VALUE - 2;
    public static final int FULL_LIGHT = Character.MAX_VALUE - 1;
    public static final int HALF_MIRROR = Character.MAX_VALUE;
    public static final int SOURCE = (char)MC.SOURCE;
    public static final int BLOCK = (char)MC.BLOCK;
    public static final int GROUND = (char)MC.GROUND;
	private char [][]map;
	private int height, width;
	private Stack<ArrayList<TileState>> mapHistory;
    private Stack<HeadLight> [] lightHistory;
	private HeadLight[] headLight; // Calculable from the current map
	public Board(char [][]matrix) throws Exception{ // Get a copy of map
        if(matrix[0].length == 0) 
            throw new Exception("Length of map is zero.");
        this.headLight = new HeadLight[]{null, null};
        this.mapHistory = new Stack<>();
        this.lightHistory = new Stack[]{new Stack<>(), new Stack<>()};
        this.height = matrix.length;
        this.width = matrix[0].length;
        this.map = new char[this.height][this.width];
        for (int i = 0; i < this.height; i++){
        	for (int j = 0; j < this.width; j++){
                if(matrix[i][j] == GROUND)
                	this.map[i][j] = GROUND;
                else this.map[i][j] = BLOCK;
            }
        }   
    }
    public HashSet<Placement> getAvailablePlace(){
        HashSet<Placement> hs = new HashSet<>();
        if(headLight[0] == null || headLight[1] == null){
            for(int i = 1; i < width; i++){
                if(map[0][i] == GROUND)
                    hs.add(new Placement(0, i, 0));
                if(map[height - 1][i] == GROUND)
                    hs.add(new Placement(height - 1, i, 0));
            }
            for(int i = 1; i < height; i++){
                if(map[i][0] == GROUND)
                    hs.add(new Placement(i, 0, 0));
                if(map[i][width - 1] == GROUND)
                    hs.add(new Placement(i, width - 1, 0));
            }
        }
        for(int i = 0; i < 2; i++){
            if(headLight[i] != null){
                hs.add(new Placement(headLight[i].row, headLight[i].col, 1));
                hs.add(new Placement(headLight[i].row, headLight[i].col, -1));    
            }
        }
        return hs;
    }
    public boolean bothAreAlive(){
        return (headLight[0] != null && headLight[1] != null);
    }
    /* Helper method */
    private ArrayList<TileState> placeLightPath(int row, int col, int drow, int dcol, int headIndex){
        ArrayList<TileState> changes = new ArrayList<>();
        int firstRow = row;
        int firstCol = col;
        changes.add(new TileState(row, col, map[row][col]));
        headLight[headIndex] = null;
        while(row + drow >= 0 && row + drow < height && col + dcol >= 0 && col + dcol < width){
            // Narrow-coded
            if(headLight[1 - headIndex] != null && row == headLight[1 - headIndex].row && col == headLight[1 - headIndex].col)
                headLight[1 - headIndex] = null;            
            row += drow;
            col += dcol;
            if(map[row][col] == BLOCK || map[row][col] == HALF_MIRROR){
                if(map[row - drow][col - dcol] == HALF_LIGHT && (firstRow != row - drow || firstCol != col - dcol))
                    headLight[headIndex] = new HeadLight(row - drow, col - dcol, (dcol!=0)?1:0, (drow!=0)?1:0);
                break;
            } 
            changes.add(new TileState(row, col, map[row][col]));
            if(map[row][col] < HALF_LIGHT) 
                map[row][col] = HALF_LIGHT;
            else map[row][col] = FULL_LIGHT;         
        }
        return changes;
    }
    public boolean place(int row, int col, int type){
        if(!getAvailablePlace().contains(new Placement(row, col, type))) 
            return false;
        // Narrow-coded
        for(int i = 0; i < 2; i++){
            lightHistory[i].push(headLight[i]);
        }

        if(type == 0){
            int headIndex = (headLight[0]==null)?0:1;
            if(row == 0){
                mapHistory.push(placeLightPath(row, col, 1, 0, headIndex));
                map[row][col] = SOURCE;
            }
            else if(row == height - 1){
                mapHistory.push(placeLightPath(row, col, -1, 0, headIndex));
                map[row][col] = SOURCE;
            }
            else if(col == 0){
                mapHistory.push(placeLightPath(row, col, 0, 1, headIndex));
                map[row][col] = SOURCE;
            }
            else if(col == width - 1){
                mapHistory.push(placeLightPath(row, col, 0, -1, headIndex));
                map[row][col] = SOURCE;
            }
        }
        else for(int i = 0; i < 2; i++){
            if(headLight[i] != null && headLight[i].row == row && headLight[i].col == col){
                int[] dir = headLight[i].getDirection(type);
                if(dir[0] == 0 && dir[1] == 0) 
                    continue; 
                mapHistory.push(placeLightPath(row, col, dir[0], dir[1], i));
                map[row][col] = HALF_MIRROR;
                break;
            }
        }
        if(mapHistory.size() == 0) return false;
        return true;
	}
    public int peek(){
        if(mapHistory.size() == 0)
            return -1;
        return mapHistory.peek().size() - 1;
    }
	public void undo(){ 
        if(mapHistory.size() == 0) return;
        for(int i = 0; i < 2; i++){
            headLight[i] = lightHistory[i].pop();
        }
        ArrayList<TileState> state = mapHistory.pop();
        for(TileState ts: state){
            map[ts.row][ts.col] = ts.tile;
        }
	}
    public char getTile(int row, int col){
        return map[row][col];
    }
	public String getMapString(){
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++){
                sb.append(map[i][j]);
            }
        }
        return sb.toString();
	}
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for(int i = 0; i < map[0].length; i++){
            sb.append(String.format("%3d", i));
        }
        sb.append('\n');
        for(int i = 0; i < height; i++){
            sb.append(String.format("%2d", i));
            for(int j = 0; j < width; j++){
                if(map[i][j] == BLOCK){
                    sb.append("  B");
                }
                else if(map[i][j] == SOURCE){
                    sb.append("  S");
                }
                else if(map[i][j] == GROUND){
                    sb.append("  G");
                }
                else if(map[i][j] == HALF_LIGHT){
                    sb.append(" HL");
                }
                else if(map[i][j] == FULL_LIGHT){
                    sb.append("  L");
                }
                else if(map[i][j] == HALF_MIRROR){
                    sb.append("  M");
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}