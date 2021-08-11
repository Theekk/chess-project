package application;

import boardgame.Board;
import boardgame.Position;
import chess.ChessRules;

public class Program {
    public static void main(String[] args){
        ChessRules chessRules = new ChessRules();
        UI.printBoard(chessRules.getPieces());
        System.out.println("AAAA");
    }
}
