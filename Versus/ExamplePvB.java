import java.io.*;
import java.util.*;

public class ExamplePvB{
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        char [][]map = new char[][]{ //17 rows x 17 columns
            {9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9},
            {3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 9, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 9, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 9, 3, 9, 3, 9, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3},
            {9, 9, 9, 9, 9, 9, 3, 9, 9, 9, 3, 9, 9, 9, 9, 9, 9},
            {3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 9, 3, 9, 3, 9, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 9, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 9, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3},
            {9, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 9}
        };
        Board board = new Board(map);
        TwoPlayerScoring scoring = new TwoPlayerScoring();
        Bot bot = new GreedyBot(); bot.bind(board, scoring);
        //Bot bot = new ExpertBot(); bot.bind(board, scoring);
        int user = 0; // user's turn
        int row, col, type;
        HashSet<Placement> places;
        System.out.print("Move first? (y/n): ");
        if(sc.next().toLowerCase() == "n"){
        	user = 1;
        }
        while(true){
			places = board.getAvailablePlace(); if(places.size() == 0) break;
			if(user == scoring.getTurn()){
				System.out.println("\nPlayer " + (scoring.getTurn() + 1) + " (You)");	
			}
			else{
				System.out.println("\nPlayer " + (scoring.getTurn() + 1) + " " + bot.getName());
			}
        	System.out.print(board);
            System.out.println("Scores " + scoring.getScore(0) + " - " + scoring.getScore(1) + " " + "[" + scoring.evaluate() + "]");
            if(user == scoring.getTurn()){
                while(true){
                    System.out.print("Your move: ");
                	row = sc.nextInt();
                    if(row < 0){
                        System.out.println(places);
                        continue;
                    }
            		col = sc.nextInt();
            		type = sc.nextInt();
            		boolean valid = board.place(row, col, type);
                	if(valid){
                        scoring.add(board.peek());
                        break;
                    } 
                }	
            }
            else{
                Placement place = bot.response();
                if(place == null)
                    break;
                System.out.println(bot.getName() + " moves " + place);
            }

        }
        System.out.print("\n"+board);
        System.out.println("Scores " + scoring.getScore(0) + " - " + scoring.getScore(1) + " " + "[" + scoring.evaluate() + "]");
        if(scoring.evaluate() >= 1)
            System.out.print("The first player wins.");
        else 
            System.out.print("The second player wins.");
        return;
    }
}