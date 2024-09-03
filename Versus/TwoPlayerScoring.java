import java.util.*;

public class TwoPlayerScoring{
    private int[] scores;
    private int turn;
    private Stack<Integer> sumHistory;
    public TwoPlayerScoring(){
        scores = new int[2];
        scores[0] = scores[1] = 0;
        turn = 0;
        sumHistory = new Stack<>();
    }
    public void add(int score){
        sumHistory.push(scores[0] + scores[1]);
        scores[turn%2] += score;
        turn++;
    }
    public void undo(){
        if(sumHistory.size() == 0){
           return;
        }
        int sum = sumHistory.pop();
        turn--;
        scores[turn%2] = sum - scores[(turn + 1)%2];
    }
    public int getScore(int i){
        return scores[i%2];
    }
    public int evaluate(){
        return scores[0] - scores[1];
    }
    public int getTurn(){
        return turn%2;
    }
    public int getOrder(){
        return turn;
    }
}