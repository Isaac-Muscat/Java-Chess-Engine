package utilities;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Moves.Move;

public class Perft {
	private Perft() throws Exception {
		throw new Exception("Don't Initialize this");
	}
	
	public static int perft(Board board, int depth) {
		
		if(depth == 0) {
			return 1;
		}
		ArrayList<Move> moves = board.generateLegalMoves();
		if(moves.size()==0) {
			System.out.println("1");
		}
		int moveCount = moves.size();
		int nodes = 0;
		
		for(int i=0; i < moveCount; i++) {
			Move m = moves.get(i);
			m.makeMove(board);
			board.updateSideToMove();
			nodes += perft(board, depth-1);
			m.unmakeMove(board);
			board.updateSideToMove();
		}
		return nodes;
		
	}
}
