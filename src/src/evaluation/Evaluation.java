package evaluation;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;

public class Evaluation {
	private Evaluation() {
		
	}
	
	public static int evaluate(Board board) {
		int score = 0;
		for(Piece piece:board.getPieces()) {
			score+=piece.getPieceEnum().getValue()*Long.bitCount(piece.getBitboard());
		}
		return score*board.getSideToMove().getEvalSign();
	}
}
