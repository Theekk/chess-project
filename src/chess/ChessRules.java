package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessRules {
    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessRules(){
        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        setInitial();

    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck(){
        return check;
    }

    public ChessPiece getEnPassantVulnerable(){
        return enPassantVulnerable;
    }
    public boolean getCheckMate(){
        return checkMate;
    }
    public ChessPiece[][] getPieces(){
        ChessPiece[][] posXY = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                posXY[i][j] = (ChessPiece) board.piece(i,j);
            }
        } return posXY;
    }

    public boolean[][] possibleMoves(ChessPosition startPosition){
        Position position = startPosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition startPosition,ChessPosition lastPosition){
        Position start = startPosition.toPosition();
        Position last = lastPosition.toPosition();
        validateSourcePosition(start);
        validateTargetPosition(start,last);
        Piece capturedPiece = makeMove(start,last);

        if(testCheck(currentPlayer)){
            undoMove(start,last,capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(last);

        check = (testCheck(opponent(currentPlayer)))? true:false;

        if(testCheckMate(opponent(currentPlayer))){

            checkMate = true;
        }
        else {
            nextTurn();
        }
        //Special en Passant
        if (movedPiece instanceof Pawn && (last.getRow() == start.getRow() - 2) || (last.getRow() == start.getRow() + 2)){
            enPassantVulnerable = movedPiece;
        }
        else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece in the position.");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("The piece chosen is not your");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
    }

    private void validateTargetPosition(Position start,Position last){
        if(!board.piece(start).possibleMove(last)){
            throw new ChessException("The chosen piece can't move to target position.");
        }
    }

    private Piece makeMove(Position start,Position last) {
        ChessPiece p = (ChessPiece) board.removePiece(start);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(last);
        board.placePiece(p, last);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        //Special castling KingSide rook
        if (p instanceof King && last.getColumn() == start.getColumn() + 2) {
            Position startT = new Position(start.getRow(), start.getColumn() + 3);
            Position LastT = new Position(start.getRow(), start.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(startT);
            board.placePiece(rook, LastT);
            rook.increaseMoveCount();
        }

        //Special castling QueenSide rook
        if (p instanceof King && last.getColumn() == start.getColumn() - 2) {
            Position startT = new Position(start.getRow(), start.getColumn() - 4);
            Position lastT = new Position(start.getRow(), start.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(startT);
            board.placePiece(rook, lastT);
            rook.increaseMoveCount();
        }

        //Special en Passant
        if (p instanceof Pawn) {
            if (start.getColumn() != last.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(last.getRow() + 1, last.getColumn());
                } else {
                    pawnPosition = new Position(last.getRow() - 1, last.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece;
    }

    private void undoMove(Position start,Position last,Piece capturedPiece){
        ChessPiece p = (ChessPiece)board.removePiece(last);
        p.decreaseMoveCount();
        board.placePiece(p, start);

        if(capturedPiece != null ){
            board.placePiece(capturedPiece,last);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        //Special castling KingSide rook
        if(p instanceof King && last.getColumn() == start.getColumn() + 2){
            Position startT = new Position(start.getRow(), start.getColumn() + 3);
            Position lastT = new Position(start.getRow(), start.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(lastT);
            board.placePiece(rook,startT);
            rook.decreaseMoveCount();
        }

        //Special castling QueenSide rook
        if(p instanceof King && last.getColumn() == start.getColumn() - 2){
            Position startT = new Position(start.getRow(), start.getColumn() - 4);
            Position lastT = new Position(start.getRow(), start.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(lastT);
            board.placePiece(rook,startT);
            rook.decreaseMoveCount();
        }

        //Special en Passant
        if (p instanceof Pawn) {
            if (start.getColumn() != last.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece)board.removePiece(last);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, last.getColumn());
                }
                else {
                    pawnPosition = new Position(4, last.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private Color opponent(Color color){
        return(color == Color.WHITE)? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color){
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p :list){
            if (p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Tehere is no"+color+"King on the board!!!");
    }

    private boolean testCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for(Piece p: opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color){
        if(!testCheck(color)){
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p:list){
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if(mat[i][j]){
                        Position start = ((ChessPiece)p).getChessPosition().toPosition();
                        Position last = new Position(i,j);
                        Piece capturedPiece = makeMove(start,last);
                        boolean testCheck = testCheck(color);
                        undoMove(start,last,capturedPiece);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    private void placeNewPiece(char column,int row,ChessPiece piece){
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void setInitial(){
        placeNewPiece('a',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('b',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('c',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('d',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('e',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('f',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('g',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('h',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('a',1,new Rook(board,Color.WHITE));
        placeNewPiece('b',1,new Knight(board,Color.WHITE));
        placeNewPiece('c',1,new Bishop(board,Color.WHITE));
        placeNewPiece('d',1,new Queen(board,Color.WHITE));
        placeNewPiece('e',1,new King(board,Color.WHITE,this));
        placeNewPiece('f',1,new Bishop(board,Color.WHITE));
        placeNewPiece('g',1,new Knight(board,Color.WHITE));
        placeNewPiece('h',1,new Rook(board,Color.WHITE));


        placeNewPiece('a',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('b',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('c',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('d',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('e',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('f',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('g',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('h',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('a',8,new Rook(board,Color.BLACK));
        placeNewPiece('b',8,new Knight(board,Color.BLACK));
        placeNewPiece('c',8,new Bishop(board,Color.BLACK));
        placeNewPiece('d',8,new Queen(board,Color.BLACK));
        placeNewPiece('e',8,new King(board,Color.BLACK,this));
        placeNewPiece('f',8,new Bishop(board,Color.BLACK));
        placeNewPiece('g',8,new Knight(board,Color.BLACK));
        placeNewPiece('h',8,new Rook(board,Color.BLACK));
    }

    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
    }
}
