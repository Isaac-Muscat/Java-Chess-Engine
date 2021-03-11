package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;
import utilities.BitboardUtils;
import utilities.File;

public class DoublePawnPush extends NonCapture {

	public DoublePawnPush(Piece pieceToMove, int startPos, int endPos) {
		super(pieceToMove, startPos, endPos);
	}

	@Override
	public void makeMove(Board board) {
		long newBitboard = (pieceToMove.getBitboard()&~(BitboardUtils.SQUARE[0]>>>startPos))|(BitboardUtils.SQUARE[0]>>>endPos);
		pieceToMove.setBitboard(newBitboard);
		board.setEP(File.returnFileFromIndex(startPos));
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
		
	}
}
