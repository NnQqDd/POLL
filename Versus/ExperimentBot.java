import java.util.*;
import java.time.Duration;
import java.time.Instant;


class IntPair{
	public int first;
	public int second; 
	public IntPair(int x, int y){
		first = x;
		second = y;
	}
	public IntPair(){
		this.first = 0;
		this.second = 0;
	}
	public IntPair add(IntPair other){
		return new IntPair(first + other.first, second + other.second);
	}
	public double getRatio(){
		if(first + second == 0) return -1e9;
		return first/(first + second);
	}
}


public class ExperimentBot extends Bot{
	private HashMap<String, IntPair> randomScore; //Win-loss
	private Random numberGenerator;
	private double timeLimit; 
	// Init inside response()
	private Instant instant;
	private int botTurn;
	public ExperimentBot(){
		super();
		super.name = "Experiment bot";
		this.randomScore = new HashMap<>();
		this.numberGenerator = new Random();
		this.timeLimit = 100.0;
	}
	private ArrayList<Placement> getPotentialPlace(){
		ArrayList<Integer> scores = new ArrayList<>();
		ArrayList<Placement> potentialPlaces = new ArrayList<>(); 
		ArrayList<Placement> places = new ArrayList<>(super.board.getAvailablePlace());
		int firstScore = -1;
		for(Placement place: places){
			super.board.place(place.row, place.col, place.type);
			scores.add(super.board.peek());
			if(firstScore < super.board.peek()){
				firstScore = super.board.peek();
			}
			super.board.undo();
		}
		for(int i = 0; i < places.size(); i++){
			if(scores.get(i) == firstScore)
				potentialPlaces.add(places.get(i));
		}
		return potentialPlaces;
	}
	private Placement getRandomPotentialPlace(){
		ArrayList<Placement> places = getPotentialPlace();
		if(places.size() == 0) return null;
		return places.get(numberGenerator.nextInt(places.size()));
	}
	private IntPair randomTraverse(){
		if(Duration.between(instant, Instant.now()).toMillis() >= this.timeLimit){
			return new IntPair(0, 0);
		} 
		Placement place = getRandomPotentialPlace();
		if(place == null) {
			if(super.scoring.evaluate() == 0)
				return new IntPair(0, 0);
			if(super.scoring.getScore(botTurn) > super.scoring.getScore(1 - botTurn)) 
				return new IntPair(1, 0);
			return new IntPair(0, 1);
		}
		IntPair res = new IntPair(0, 0);
			super.board.place(place.row, place.col, place.type);
			super.scoring.add(board.peek());
			res.add(randomTraverse());
			super.board.undo();
			super.scoring.undo();
		String key = super.board.getMapString();
		IntPair eval = randomScore.getOrDefault(key, new IntPair(0, 0));
		randomScore.put(key, eval.add(res));
		return res;
	}
	@Override
	public Placement response(){
		ArrayList<Placement> places = getPotentialPlace();
		if(places.size() == 1) {
			super.board.place(places.get(0).row, places.get(0).col, places.get(0).type);
			super.scoring.add(super.board.peek());
			return places.get(0);
		}
		this.botTurn = super.scoring.getTurn();
		this.instant = Instant.now();
		double bestRatio = -1e9;
		Placement bestPlace = null;
		while(Duration.between(instant, Instant.now()).toMillis() < this.timeLimit){
			randomTraverse();
		} 
		for(Placement place: places){
			super.board.place(place.row, place.col, place.type);
			double ratio = randomScore.getOrDefault(super.board.getMapString(), new IntPair(0, 0)).getRatio();
			if(ratio >= bestRatio){ // Must be >=
				bestRatio = ratio;
				bestPlace = place;
			}
			super.board.undo();
		}
		if(bestPlace != null){
			super.board.place(bestPlace.row, bestPlace.col, bestPlace.type);
			super.scoring.add(super.board.peek());
		}
		return bestPlace;
	}
}

