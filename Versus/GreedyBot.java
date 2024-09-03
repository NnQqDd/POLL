import java.util.*;

public class GreedyBot extends Bot{
	private Random random;
	public GreedyBot(){
		super();
		super.name = "Greedy bot";
		this.random = new Random();
	}
	@Override
	public Placement response(){
		ArrayList<Integer> scores = new ArrayList<>();
		ArrayList<Placement> potentialPlaces = new ArrayList<>(); 
		ArrayList<Placement> places = new ArrayList<>(super.board.getAvailablePlace());
		int maxScore = -1;
		for(Placement place: places){
			super.board.place(place.row, place.col, place.type);
			scores.add(board.peek());
			if(maxScore < board.peek()){
				maxScore = board.peek();
			}
			super.board.undo();
		}
		for(int i = 0; i < places.size(); i++){
			if(scores.get(i) == maxScore)
				potentialPlaces.add(places.get(i));
		}
		Placement place = null;
		if(!potentialPlaces.isEmpty()){
			place = potentialPlaces.get(random.nextInt(potentialPlaces.size()));
			super.board.place(place.row, place.col, place.type);
			super.scoring.add(maxScore);
		}
		return place;
	}

}
