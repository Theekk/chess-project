package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.ChessRules;
import chess.Color;

public class King extends ChessPiece {

    private ChessRules chessRules;

    public King(Board board, Color color, ChessRules chessRules) {
        super(board, color);
        this.chessRules = chessRules;
    }
    @Override
    public String toString(){
        return"K";
    }

    private boolean canMove(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }
    private boolean testRookCastling(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }
    @Override
    public boolean[][] possibleMoves(){
        boolean [][] mov = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        //above
        p.setValues(position.getRow()-1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //down
        p.setValues(position.getRow()+1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //left
        p.setValues(position.getRow(), position.getColumn()-1);
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //right
        p.setValues(position.getRow(), position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //nw
        p.setValues(position.getRow()-1, position.getColumn()-1);
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //ne
        p.setValues(position.getRow()-1, position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //sw
        p.setValues(position.getRow()+1, position.getColumn()-1);
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }
        //se
        p.setValues(position.getRow()+1, position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)){
            mov[p.getRow()][p.getColumn()] = true;
        }

        //SpecialMove Castling
        if(getMoveCount() == 0 && !chessRules.getCheck()){
            //Castling kingSide rook
            Position pt1 = new Position(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(pt1)){
                Position p1 = new Position(position.getRow(), position.getColumn()+1);
                Position p2 = new Position(position.getRow(), position.getColumn()+2);
                if(getBoard().piece(p1) == null && getBoard().piece(p2) == null){
                    mov[position.getRow()][position.getColumn() + 2] = true;
                }
            }//Castling QueenSide rook
            Position pt2 = new Position(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(pt1)){
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null){
                    mov[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }
        return mov;
    }
}
