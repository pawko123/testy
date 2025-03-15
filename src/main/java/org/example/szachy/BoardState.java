package org.example.szachy;

import java.util.*;

public class BoardState {
    public Map<String, String> board;
    public static final String[] allowedPieces = {"p", "k"};

    public BoardState(int size){
        if(size < 1){
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        if(size > 26){
            throw new IllegalArgumentException("Size must be less than 27");
        }
        this.board = new HashMap<String, String>();
        char letter = 'a';
        for(int i = 1; i <= size; i++){
            for(int j = 1; j <= size; j++){
                String fieldname = (char)(letter + i - 1)+ Integer.toString(j);
                this.board.put(fieldname, "");
            }
        }
    }

    public void setField(String userInput){
        String pieceLetter = userInput.substring(0, 1);
        String pieceField = userInput.substring(1);
        if(Arrays.stream(allowedPieces).noneMatch(pieceLetter::equals)){
            throw new IllegalArgumentException("Piece not allowed");
        }
        if(!this.board.containsKey(pieceField)){
            throw new IllegalArgumentException("Fieldname does not exist");
        }
        this.board.put(pieceField, pieceLetter);
    }

    public Map<String, List<String>> availableMoves(){
        //find knight on board
        List<String> knightFields = new ArrayList<String>();
        Map<String, List<String>> viableMoves = new HashMap<String, List<String>>();
        for(Map.Entry<String, String> entry : this.board.entrySet()){
            if(entry.getValue().equals("k")){
                knightFields.add(entry.getKey());
            }
        }
        if(knightFields.isEmpty()){
            throw new IllegalArgumentException("There is no knight on the board");
        }
        for(String knightField : knightFields){
            List<String> viableMovesForPiece = new ArrayList<String>();
            //find all possible moves
            char letter = knightField.charAt(0);
            int number = Integer.parseInt(knightField.substring(1));
            String[] possibleMoves = {
                    (char)(letter + 2) + Integer.toString(number + 1),
                    (char)(letter + 2) + Integer.toString(number - 1),
                    (char)(letter - 2) + Integer.toString(number + 1),
                    (char)(letter - 2) + Integer.toString(number - 1),
                    (char)(letter + 1) + Integer.toString(number + 2),
                    (char)(letter + 1) + Integer.toString(number - 2),
                    (char)(letter - 1) + Integer.toString(number + 2),
                    (char)(letter - 1) + Integer.toString(number - 2)
            };

            for (String move : possibleMoves) {
                if(this.isFieldValid(move)){
                    viableMovesForPiece.add(move);
                }
            }
            viableMoves.put(knightField, viableMovesForPiece);
        }
        return viableMoves;
    }

    public boolean isFieldValid(String fieldname){
        if(!this.board.containsKey(fieldname)){
            return false;
        }
        String piece = this.board.get(fieldname);
        return !piece.equals("p");
    }
}
