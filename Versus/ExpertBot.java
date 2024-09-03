import java.util.*;
import java.time.Duration;
import java.time.Instant;

public class ExpertBot extends Bot{
	private double timeLimit; 
	private int distance;
	private int bruteforceRange;
	private int bruteforceDistance;
	// Init inside response()
	private HashMap<String, Integer> positionEval;
	private Instant instant;
	private int botTurn;
	private boolean bruteforce;
	public ExpertBot(){
		super();
		super.name = "Expert bot";
		this.bruteforceDistance = 3;
		this.distance = 7;
		this.timeLimit = 2000.0;
	}
	private int max(int a, int b){
		return (a < b)?b:a;
	}
	private int min(int a, int b){
		return (a < b)?a:b;
	}
	private int sign(int a){
		return (a == 0)?0:((a > 0)?1:-1);
	}
	private ArrayList<Placement> getPotentialPlace(){
		if(bruteforce) 
			return new ArrayList<>(super.board.getAvailablePlace());
		ArrayList<Placement> potentialPlaces = new ArrayList<>(); 
		Placement []no = new Placement[]{null, null, null, null};
		int []scores = new int[]{-1, -1, -1, -1};
		for(Placement place: super.board.getAvailablePlace()){
			super.board.place(place.row, place.col, place.type);
			int score = super.board.peek();
			if(scores[0] < score){
				scores[3] = scores[2]; no[3] = no[2];
				scores[2] = scores[1]; no[2] = no[1];
				scores[1] = scores[0]; no[1] = no[0];
				scores[0] = score; no[0] = place;
			}
			else if(scores[1] < score){
				scores[3] = scores[2]; no[3] = no[2];
				scores[2] = scores[1]; no[2] = no[1];
				scores[1] = score; no[1] = place;
			}
			else if(scores[2] < score){
				scores[3] = scores[2]; no[3] = no[2];
				scores[2] = score; no[2] = place;
			}
			else if(scores[3] < score){
				scores[3] = score; no[3] = place;
			}
			super.board.undo();
		}
		for(int i = 0; i <= 3; i++){
			if(no[i] != null)
				potentialPlaces.add(no[i]);
			else break;
		}

		return potentialPlaces;
	}
	private int maxTraversal(int alpha, int beta, int depth){
		if(Duration.between(instant, Instant.now()).toMillis() >= this.timeLimit){
			return (int)1e9;
		} 
		String key = super.board.getMapString();
		ArrayList<Placement> places = getPotentialPlace();
		if(places.size() == 0 || (bruteforce && depth == bruteforceDistance) || depth == this.distance){
			positionEval.put(key, super.scoring.evaluate());
			return super.scoring.evaluate();
			//return sign(super.scoring.evaluate())*(int)1e9;
		}
		if(positionEval.containsKey(key))
			return positionEval.get(key);
		int val = (int)-1e9;
		for(Placement place: places){
			super.board.place(place.row, place.col, place.type);
			super.scoring.add(super.board.peek());
			val = max(val, minTraversal(alpha, beta, depth + 1));
			super.board.undo();
			super.scoring.undo();
			if(val >= beta) break;
			alpha = max(alpha, val);
		}
		positionEval.put(key, val);
		return val;
	}
	private int minTraversal(int alpha, int beta, int depth){
		if(Duration.between(instant, Instant.now()).toMillis() >= this.timeLimit){
			return (int)-1e9;
		} 
		String key = super.board.getMapString();
		ArrayList<Placement> places = getPotentialPlace();
		if(places.size() == 0 || (bruteforce && depth == bruteforceDistance) || depth == this.distance){
			positionEval.put(key, super.scoring.evaluate());
			return super.scoring.evaluate();
			//return sign(super.scoring.evaluate())*(int)1e9;
		}
		if(positionEval.containsKey(key))
			return positionEval.get(key);
		int val = (int)1e9;
		for(Placement place: places){
			super.board.place(place.row, place.col, place.type);
			super.scoring.add(super.board.peek());
			val = min(val, maxTraversal(alpha, beta, depth + 1));
			super.board.undo();
			super.scoring.undo();
			if(val <= alpha) break;
			beta = min(beta, val);
		}
		positionEval.put(key, val);
		return val;
	}
	@Override
	public Placement response(){	
		this.botTurn = super.scoring.getTurn();
		this.instant = Instant.now();
		this.positionEval = new HashMap<>();
		double prob = 0.5;
		if(super.board.bothAreAlive()){
			prob = 0.2;
		}
		else prob = 0.8;
		if(Math.random() >= prob)
			this.bruteforce = false;
		else this.bruteforce = true;
		int val = 0;
		if(botTurn == 0)
			val = maxTraversal((int)-1e9, (int)1e9, 1);
		else val = minTraversal((int)-1e9, (int)1e9, 1);
		ArrayList<Placement> places = getPotentialPlace();
		for(Placement place: places){
			super.board.place(place.row, place.col, place.type);
			String tkey = super.board.getMapString();
			if(positionEval.containsKey(tkey) && positionEval.get(tkey) == val){
				super.scoring.add(super.board.peek());
				return place;
			}
			super.board.undo();
		}
		//Only on timeout.
		if(places.size() != 0)
			return places.get(0);
		return null;
	}
}



