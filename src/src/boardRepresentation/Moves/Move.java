package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.*;
import utilities.File;

public abstract class Move {
	protected File enPassantFile = null;
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
	public String getInfo() {
		String info = "";
		info += "MoveType: " + this.getClass()+"\n";
		info += "PieceToMove: " + pieceToMove.getPieceEnum()+"\n";
		info += "startPos: " + startPos +"\n";
		info += "endPos: " + endPos +"\n";
		return info;
	}
	
	public int getFrom() {
		return startPos;
	}
	public int getTo() {
		return endPos;
	}
}
