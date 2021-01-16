package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.*;

public class Capture extends Move{
	private Piece capturedPiece;
	public Capture(Piece pieceToMove, int startPos, int endPos, Piece capturedPiece) {
		super(pieceToMove, startPos, endPos);
		this.capturedPiece = capturedPiece;
	}
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
