package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return"N";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mov = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        //UpRight
        p.setValues(position.getRow() - 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        // UpLeft
        p.setValues(position.getRow() - 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }

        //RightUp
        p.setValues(position.getRow() -1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        //RightDown
        p.setValues(position.getRow() + 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }

        //DownRight
        p.setValues(position.getRow() + 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        //DownLeft
        p.setValues(position.getRow() + 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }

        //LeftUp
        p.setValues(position.getRow() - 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        //LeftDown
        p.setValues(position.getRow() + 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mov[p.getRow()][p.getColumn()] = true;
        }

        return mov;
    }
}
