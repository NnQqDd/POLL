import java.io.*;
import java.util.*;

public class ExamplePvP{
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        char [][]map = new char[][]{ // CHAR
            {9, 3, 3, 3, 9},
            {3, 3, 3, 3, 3},
            {9, 9, 9, 3, 3},
            {3, 3, 9, 3, 3},
            {9, 3, 9, 3, 9}
        };
        Board board= new Board(map);
        TwoPlayerScoring scoring = new TwoPlayerScoring();
        String command;
        int row, col, type;
        HashSet<Placement> places;
        while(true){
            places = board.getAvailablePlace(); if(places.size() == 0) break;
            System.out.println("Player " + (scoring.getTurn() + 1));
            System.out.println("Scores " + scoring.getScore(0) + " - " + scoring.getScore(1) + " " + "[" + scoring.evaluate() + "]");
            System.out.print(board);
            System.out.print("Moves ");
            for(Placement x: places){
                System.out.print(x + " ");
            }
            System.out.println();
            command = sc.next();
            if(command.equals("place")){
                row = sc.nextInt();
                col = sc.nextInt();
                type = sc.nextInt();
                if(board.place(row, col, type)){
                    scoring.add(board.peek());
                }
            }
            if(command.equals("undo")){
                board.undo();
                scoring.undo();
            }
        }
        System.out.print("Scores " + scoring.getScore(0) + " - " + scoring.getScore(1) + " " + "[" + scoring.evaluate() + "]\n");
        if(scoring.evaluate() >= 0)
            System.out.print("The first player wins.");
        else 
            System.out.print("The second player wins.");
        return;
    }
}
/*
Player 1                                                                                                                                                                                                                                               
Scores 0 - 0 [0]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  G  G  G  B                                                                                                                                                                                                                                      
 1  G  G  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3  G  G  B  G  G                                                                                                                                                                                                                                      
 4  B  G  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (1, 0, 0) (4, 1, 0) (3, 0, 0) (0, 3, 0) (1, 4, 0) (0, 2, 0) (2, 4, 0) (0, 1, 0) (3, 4, 0)                                                                                                                                              
place 0 1 0                                                                                                                                                                                                                                            
Player 2                                                                                                                                                                                                                                               
Scores 1 - 0 [1]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  G HL  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3  G  G  B  G  G                                                                                                                                                                                                                                      
 4  B  G  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (1, 0, 0) (1, 1, 1) (4, 1, 0) (3, 0, 0) (0, 3, 0) (1, 4, 0) (0, 2, 0) (2, 4, 0) (3, 4, 0) (1, 1, -1)                                                                                                                                   
place 4 1 0                                                                                                                                                                                                                                            
Player 1                                                                                                                                                                                                                                               
Scores 1 - 1 [0]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  G HL  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3  G HL  B  G  G                                                                                                                                                                                                                                      
 4  B  S  B  G  B                                                                                                                                                                                                                                      
Moves (1, 1, 1) (3, 1, -1) (3, 1, 1) (1, 1, -1)                                                                                                                                                                                                        
place 3 1 -1                                                                                                                                                                                                                                           
Player 2                                                                                                                                                                                                                                               
Scores 2 - 1 [1]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  G HL  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3 HL  M  B  G  G                                                                                                                                                                                                                                      
 4  B  S  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (1, 0, 0) (1, 1, 1) (0, 3, 0) (1, 4, 0) (0, 2, 0) (2, 4, 0) (3, 4, 0) (1, 1, -1)                                                                                                                                                       
place 1 0 0                                                                                                                                                                                                                                            
Player 1                                                                                                                                                                                                                                               
Scores 2 - 5 [-3]                                                                                                                                                                                                                                      
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  S  L HL HL HL                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3 HL  M  B  G  G                                                                                                                                                                                                                                      
 4  B  S  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (0, 3, 0) (0, 2, 0) (2, 4, 0) (3, 4, 0)                                                                                                                                                                                                
undo                                                                                                                                                                                                                                                   
Player 2                                                                                                                                                                                                                                               
Scores 2 - 1 [1]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  G HL  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3 HL  M  B  G  G                                                                                                                                                                                                                                      
 4  B  S  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (1, 0, 0) (1, 1, 1) (0, 3, 0) (1, 4, 0) (0, 2, 0) (2, 4, 0) (3, 4, 0) (1, 1, -1)                                                                                                                                                       
undo                                                                                                                                                                                                                                                   
Player 1                                                                                                                                                                                                                                               
Scores 1 - 1 [0]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  G HL  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3  G HL  B  G  G                                                                                                                                                                                                                                      
 4  B  S  B  G  B                                                                                                                                                                                                                                      
Moves (1, 1, 1) (3, 1, -1) (3, 1, 1) (1, 1, -1)                                                                                                                                                                                                        
undo                                                                                                                                                                                                                                                   
Player 2                                                                                                                                                                                                                                               
Scores 1 - 0 [1]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  S  G  G  B                                                                                                                                                                                                                                      
 1  G HL  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3  G  G  B  G  G                                                                                                                                                                                                                                      
 4  B  G  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (1, 0, 0) (1, 1, 1) (4, 1, 0) (3, 0, 0) (0, 3, 0) (1, 4, 0) (0, 2, 0) (2, 4, 0) (3, 4, 0) (1, 1, -1)                                                                                                                                   
undo                                                                                                                                                                                                                                                   
Player 1                                                                                                                                                                                                                                               
Scores 0 - 0 [0]                                                                                                                                                                                                                                       
    0  1  2  3  4                                                                                                                                                                                                                                      
 0  B  G  G  G  B                                                                                                                                                                                                                                      
 1  G  G  G  G  G                                                                                                                                                                                                                                      
 2  B  B  B  G  G                                                                                                                                                                                                                                      
 3  G  G  B  G  G                                                                                                                                                                                                                                      
 4  B  G  B  G  B                                                                                                                                                                                                                                      
Moves (4, 3, 0) (1, 0, 0) (4, 1, 0) (3, 0, 0) (0, 3, 0) (1, 4, 0) (0, 2, 0) (2, 4, 0) (0, 1, 0) (3, 4, 0)       
*/