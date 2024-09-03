public class Position{
    public final int row;
    public final int col;
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    @Override
    public boolean equals(Object obj) {
        Position other = (Position) obj;
        return row == other.row && col == other.col;
    }
    @Override
    public String toString(){
        return "(" + row + ", " + col + ")";
    }
}