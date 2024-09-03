import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class ExampleMultiBvB{
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        char [][]map = new char[][]{ //17 rows x 17 columns
            {9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9},
            {3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3},
            {3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 9, 3, 9, 3, 9, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3},
            {9, 9, 3, 3, 9, 9, 3, 9, 9, 9, 3, 9, 9, 3, 3, 9, 9},
            {3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 9, 3, 9, 3, 9, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3},
            {3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3},
            {9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9}
        };
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<VersusTask> tasks = new ArrayList<>();
        int nThread = 10;
        int nMatch = 10;
        for(int i = 0; i < nThread; i++){
            VersusTask task = new VersusTask(i + 1, map, "exp", "greedy", nMatch);
            tasks.add(task);
            threads.add(new Thread(task));
        }
        for(int i = 0; i < nThread; i++){
            threads.get(i).start();
        }
        for(int i = 0; i < nThread; i++){
            threads.get(i).join();
        }
        int []point = new int[3];
        for(int i = 0; i < nThread; i++){
            int []arr = tasks.get(i).getPoint();
            point[0] += arr[0];
            point[1] += arr[1];
            point[2] += arr[2];
        }
        System.out.print(String.format("Final result: %d - %d - %d", point[0], point[1], point[2]));
        return;
    }
}


class VersusTask implements Runnable {
    char [][]map;
    String firstBot;
    String secondBot;
    private int maxMatch;
    private int[] point;
    private int threadCode;
    public VersusTask(int threadCode, char[][] map, String firstBot, String secondBot, int maxMatch){
        this.threadCode = threadCode;
        this.map = map.clone();
        this.firstBot = firstBot;
        this.secondBot = secondBot; 
        this.maxMatch = maxMatch;
        this.point = new int[3];
    }
    private Bot submitBot(String name){
        if(name.equals("random")){
            return new Bot();
        }   
        if(name.equals("greedy")){
            return new GreedyBot();
        }   
        if(name.equals("exp")){
            return new ExperimentBot();
        }  
        if(name.equals("expert")){
            return new ExpertBot();
        }
        return new Bot();
    }
    public void run(){    
        int match = 0;
        Bot botOne = submitBot(firstBot);
        Bot botTwo = submitBot(secondBot);
        System.out.println("Thread " + threadCode + ": " + botOne.getName() + " vs " + botTwo.getName());
        while(match < maxMatch){
            TwoPlayerScoring scoring = new TwoPlayerScoring();
            Board board = null;
            try{
                board = new Board(map);    
            }
            catch(Exception ex){
                System.out.println("Exception occured when create object Board in thread " + threadCode);
                return;
            }
            botOne.bind(board, scoring);
            botTwo.bind(board, scoring);
            Bot bot = null;
            while(true){
                if(board.getAvailablePlace().size() == 0) break;
                if(maxMatch == 1){
                    System.out.println("Player " + (scoring.getTurn() + 1));
                    System.out.println("Scores " + scoring.getScore(0) + " - " + scoring.getScore(1) + " " + "[" + scoring.evaluate() + "]");
                    System.out.print(board);
                }
                if(scoring.getTurn() == 0)
                    bot = botOne;
                else{
                    bot = botTwo;
                }
                Placement place = bot.response();
                if(place != null){
                    if(maxMatch == 1) System.out.println("Move " + place);
                }
                else break; 
            }
            if(scoring.evaluate() > 0)
                point[1]++;
            else if (scoring.evaluate() < 0)
                point[2]++;
            else
                point[0]++; 
            if(maxMatch < 1001)
                System.out.print(String.format("Thread %d, match %d: %d - %d - %d\n", threadCode, match + 1, point[0], point[1], point[2]));
            match++;    
            botOne = submitBot(firstBot);
            botTwo = submitBot(secondBot);
        }
    }
    public int[] getPoint(){
        return new int[]{point[0], point[1], point[2]};
    }
}

