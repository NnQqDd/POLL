import java.util.*;

public class Bot{
	protected Board board; // Reference
	protected TwoPlayerScoring scoring; // Reference
	protected String name;
	public Bot(){
		name = "Random bot";
	}
	public void bind(Board board, TwoPlayerScoring scoring){
		this.board = board;
		this.scoring = scoring;
	}
	public Placement response(){
		for(Placement place: board.getAvailablePlace()){
			board.place(place.row, place.col, place.type);
			scoring.add(board.peek());
			return place;
		}
		return null;
	}
	public String getName(){
		return name;
	}
}
