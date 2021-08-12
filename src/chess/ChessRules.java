package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessRules {
    private Board board;

    public ChessRules(){
        board = new Board(8,8);
        setInitial();
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] posXY = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                posXY[i][j] = (ChessPiece) board.piece(i,j);
            }
        } return posXY;
    }


    public ChessPiece performChessMove(ChessPosition startPosition,ChessPosition lastPosition){
        Position start = startPosition.toPosition();
        Position last = lastPosition.toPosition();
        validateSourcePosition(start);
        Piece capturedPiece = makeMove(start,last);
        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece in the position");
        }
    }

    private Piece makeMove(Position start,Position last){
        Piece p = board.removePiece(start);
        Piece capturedPiece = board.removePiece(last);
        board.placePiece(p,last);
        return capturedPiece;
    }

    private void placeNewPiece(char column,int row,ChessPiece piece){
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
    }

    private void setInitial(){
        placeNewPiece('a',8,new Rook(board,Color.WHITE));
        placeNewPiece('e',8,new King(board,Color.WHITE));
        placeNewPiece('h',8,new Rook(board,Color.WHITE));

        placeNewPiece('a',1,new Rook(board,Color.BLACK));
        placeNewPiece('e',1,new King(board,Color.BLACK));
        placeNewPiece('h',1,new Rook(board,Color.BLACK));
    }

}
