package application;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessRules;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        ChessRules chessRules = new ChessRules();
        Scanner sc = new Scanner(System.in);

        while (true){
            UI.printBoard(chessRules.getPieces());
            System.out.println();
            System.out.print("Piece: ");
            ChessPosition start = UI.readChessPosition(sc);

            System.out.print("move: ");
            ChessPosition last = UI.readChessPosition(sc);

            ChessPiece capturedPiece = chessRules.performChessMove(start,last);

        }
    }
}
