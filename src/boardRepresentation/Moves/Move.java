package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.*;
import utilities.File;

public abstract class Move {
	protected boolean kCastle, qCastle;
	protected File epFile;
	protected int startPos, endPos;
	protected Piece pieceToMove;
	public Move(Piece pieceToMove, int startPos, int endPos) {
		this.pieceToMove = pieceToMove;
		this.startPos = startPos;
		this.endPos = endPos;
	}
	public Move(Piece pieceToMove) {
		this.pieceToMove = pieceToMove;
	}
	
	public abstract void makeMove(Board board);
	public abstract void unmakeMove(Board board);
}
