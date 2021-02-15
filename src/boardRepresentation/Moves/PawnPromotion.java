package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;
import utilities.BitboardUtils;

public class PawnPromotion extends Move {
	private Piece promotedPiece;
	public PawnPromotion(Piece pieceToMove, int startPos, int endPos, Piece promotedPiece) {
		super(pieceToMove, startPos, endPos);
		this.promotedPiece = promotedPiece;
	}

	@Override
	public void makeMove(Board board) {
		enPassantFile = board.getEPFile();
		long newBitboard = pieceToMove.getBitboard()&~(BitboardUtils.SQUARE[0]>>>startPos);
		pieceToMove.setBitboard(newBitboard);
		promotedPiece.setBitboard(promotedPiece.getBitboard()|(BitboardUtils.SQUARE[0]>>>endPos));
		board.setEP(null);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
		
	}

	@Override
	public void unmakeMove(Board board) {
		long newBitboard = pieceToMove.getBitboard()|(BitboardUtils.SQUARE[0]>>>startPos);
		pieceToMove.setBitboard(newBitboard);
		promotedPiece.setBitboard(promotedPiece.getBitboard()&~(BitboardUtils.SQUARE[0]>>>endPos));
		board.setEP(enPassantFile);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
	}

}
