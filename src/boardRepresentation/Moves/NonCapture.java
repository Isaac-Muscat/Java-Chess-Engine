package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;
import utilities.BitboardUtils;

public class NonCapture extends Move{

	public NonCapture(Piece pieceToMove, int startPos, int endPos) {
		super(pieceToMove, startPos, endPos);
	}

	@Override
	public void makeMove(Board board) {
		long newBitboard = (pieceToMove.getBitboard()&~(BitboardUtils.SQUARE[0]>>>startPos))|(BitboardUtils.SQUARE[0]>>>endPos);
		pieceToMove.setBitboard(newBitboard);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
		
	}

	@Override
	public void unmakeMove(Board board) {
		long newBitboard = (pieceToMove.getBitboard()&~(BitboardUtils.SQUARE[0]>>>endPos))
				|(BitboardUtils.SQUARE[0]>>>startPos);
		pieceToMove.setBitboard(newBitboard);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
	}

}
 