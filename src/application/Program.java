package application;

import boardgame.Board;
import boardgame.Position;
import chess.ChessException;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessRules;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        ChessRules chessRules = new ChessRules();
        Scanner sc = new Scanner(System.in);
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessRules.getCheckMate()){
            try {
                UI.clearScreen();
                UI.printMatch(chessRules,captured);
                System.out.println();
                System.out.print("Piece: ");
                ChessPosition start = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessRules.possibleMoves(start);
                UI.clearScreen();
                UI.printBoard(chessRules.getPieces(),possibleMoves);
                System.out.print("move: ");
                ChessPosition last = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessRules.performChessMove(start, last);

                if(capturedPiece != null){
                    captured.add(capturedPiece);
                }

                if (chessRules.getPromoted() != null){
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.nextLine().toUpperCase();
                    while (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
                        System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
                        type = sc.nextLine().toUpperCase();
                    }
                    chessRules.replacePromotedPiece(type);
                }
            }catch(ChessException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }catch(InputMismatchException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessRules,captured);
    }
}
