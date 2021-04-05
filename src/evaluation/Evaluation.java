package evaluation;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Moves.Move;
import boardRepresentation.Pieces.Piece;

public class Evaluation {
	private Evaluation() {
		
	}
	
	public static int evaluate(Board board) {
		int score = 0;
		ArrayList<Move> moves = board.generateLegalMoves();
		switch(board.getBoardState(moves.size())) {
		case STALEMATE:
			return 0;
		case BLACK_IN_CHECKMATE:
			return 1000000000*board.getSideToMove().getEvalSign();
		case WHITE_IN_CHECKMATE:
			return -1000000000*board.getSideToMove().getEvalSign();
		default:
			break;
		}
		for(Piece piece:board.getPieces()) {
			score+=piece.getPieceEnum().getValue()*Long.bitCount(piece.getBitboard());
		}
		return score*board.getSideToMove().getEvalSign();
	}

	public static int terminal_evaluate(Board board) {
		switch(board.getBoardState(0)) {
		case STALEMATE:
			return 0;
		case BLACK_IN_CHECKMATE:
			return 1000000000*board.getSideToMove().getEvalSign();
		case WHITE_IN_CHECKMATE:
			return -1000000000*board.getSideToMove().getEvalSign();
		default:
			return 0;
		}
	}
}
