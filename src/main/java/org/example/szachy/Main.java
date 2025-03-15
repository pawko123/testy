package org.example.szachy;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static BoardState board;
    static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        boardLoop();
        putPiecesLoop();
    }

    public static void boardLoop(){
        System.out.println("Enter board size:");
        boolean enteredValidSize = false;
        while(!enteredValidSize){
            try{
                int size = scanner.nextInt();
                board = new BoardState(size);
                enteredValidSize = true;
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("Invalid input. Please enter a valid integer size:");
                scanner.next();
            }
        }
    }

    public static void putPiecesLoop(){
        System.out.println("Enter piece to put on the board. Format is piece letterField numberField (pa20 or ka10). if you want to go forward enter showValidMoves:");
        boolean gonext = false;
        while(!gonext){
            try{
                String piece = scanner.next();
                if(piece.equals("showValidMoves")){
                    Map<String, List<String>> validMoves = board.availableMoves();
                    for(Map.Entry<String, List<String>> entry : validMoves.entrySet()){
                        System.out.println("Piece on field " + entry.getKey() + " can move to fields:" + entry.getValue());

                    }
                    gonext = true;
                    continue;
                }
                board.setField(piece);
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("Invalid input. Please enter a valid piece:");
                scanner.next();
            }
        }
    }
}
