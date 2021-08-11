package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if(rows < 3 || columns <3){
          throw  new BoardException("Error: it's necessary to have a minimum the 3 columns and 3 lines.");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int rows, int columns){
        if(!positionExists(rows,columns)){
            throw new BoardException("Error: Position not existed on the Board");
        }
        return pieces[rows][columns];
    }

    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Error: Position not existed on the Board");
        }
        return pieces[getRows()][getColumns()];
    }

    public void placePiece(Piece piece,Position position) {
        if(thereIsAPiece(position)){
            throw new BoardException("Error: There is already a piece on position"+ position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row,int column){
        return row >= 0 && row <= rows && column >= 0 && column <= columns;
    }

    public boolean positionExists(Position position){
        return positionExists(position.getRow(),position.getColumn());
    }
    public boolean thereIsAPiece(Position position){
        if (!positionExists(position)){
            throw new BoardException("Error: Position not existed on the Board");
        }
        return piece(position) != null;
    }
}
