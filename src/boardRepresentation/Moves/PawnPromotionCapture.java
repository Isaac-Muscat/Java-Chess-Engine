package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;
import utilities.BitboardUtils;

public class PawnPromotionCapture extends Move{
	private Piece promotedPiece, capturedPiece;
	public PawnPromotionCapture(Piece pieceToMove, int startPos, int endPos, Piece promotedPiece, Piece capturedPiece) {
		super(pieceToMove, startPos, endPos);
		this.promotedPiece = promotedPiece;
		this.capturedPiece = capturedPiece;
	}

	@Override
	public void makeMove(Board board) {
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		pieceToMove.setBitboard(pieceToMove.getBitboard()&~start);
		promotedPiece.setBitboard(promotedPiece.getBitboard()|end);
		capturedPiece.setBitboard(capturedPiece.getBitboard()&~end);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
		
	}

	@Override
	public void unmakeMove(Board board) {
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		pieceToMove.setBitboard(pieceToMove.getBitboard()|start);
		promotedPiece.setBitboard(promotedPiece.getBitboard()&~end);
		capturedPiece.setBitboard(capturedPiece.getBitboard()|end);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
	}
}
