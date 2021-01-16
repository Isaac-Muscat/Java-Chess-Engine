package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.*;

public abstract class Move {
	private int startPos, endPos;
	private Piece pieceToMove;
	public Move(Piece pieceToMove, int startPos, int endPos) {
		this.pieceToMove = pieceToMove;
		this.startPos = startPos;
		this.endPos = endPos;
	}
	
	public abstract Board makeMove(Board board);
	public abstract Board unmakeMove(Board board);
}
