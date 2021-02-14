package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.*;
import utilities.BitboardUtils;

public class Capture extends Move{
	private Piece capturedPiece;
	public Capture(Piece pieceToMove, int startPos, int endPos, Piece capturedPiece) {
		super(pieceToMove, startPos, endPos);
		this.capturedPiece = capturedPiece;
	}
	@Override
	public void makeMove(Board board) {
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		pieceToMove.setBitboard((pieceToMove.getBitboard()&~start)|end);
		capturedPiece.setBitboard(capturedPiece.getBitboard()&~end);
		
		board.updateColorOccupied();
		board.updateOccupied();
		
	}

	@Override
	public void unmakeMove(Board board) {
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		pieceToMove.setBitboard((pieceToMove.getBitboard()&~end)|start);
		capturedPiece.setBitboard(capturedPiece.getBitboard()|end);
		
		board.updateColorOccupied();
		board.updateOccupied();
	}

}
