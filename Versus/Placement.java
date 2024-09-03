import java.util.*;

public class Placement{
	public final int row;
    public final int col;
    public final int type; //negative, zero, positive
    public Placement(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, col, type);
    }
    @Override
    public boolean equals(Object obj) {
        Placement other = (Placement) obj;
        return row == other.row && col == other.col && type == other.type;
    }
    @Override
    public String toString(){
        return "(" + row + ", " + col + ", " + type + ")";
    }
}
