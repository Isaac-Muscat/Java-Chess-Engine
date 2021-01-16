package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;
import utilities.File;

public class EnPassant extends Move{
	public EnPassant(Piece pieceToMove, int startPos, int endPos) {
		super(pieceToMove, startPos, endPos);
		// TODO Auto-generated constructor stub
	}

	private File enPassantFile;
	
	@Override
	public Board makeMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board unmakeMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}
