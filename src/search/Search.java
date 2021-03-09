package search;

import java.util.HashMap;

import boardRepresentation.Board;
import boardRepresentation.Moves.*;
import evaluation.Evaluation;

public class Search {
	private Search() {
	}
	
	public static Move getBestMove(HashMap<Move, Integer> moves) {
		Move bestMove = null;
		for (Move move: moves.keySet()){
			if (bestMove == null || moves.get(move)>moves.get(bestMove)){
				bestMove = move;
		    }
            String key = move.getInfo();
            int value = moves.get(move);  
            System.out.println(key + " " + value);
		}
		return bestMove;
	}
	
	public static int negaMax(Board board, int depth) {
		if(depth == 0) {
			return Evaluation.evaluate(board);
		}
		int max = -1000000000;
		for(Move m: board.generateLegalMoves()) {
			m.makeMove(board);
			board.updateSideToMove();
			int score = -negaMax(board, depth-1);
			if(score>max) max = score;
			m.unmakeMove(board);
			board.updateSideToMove();
			
		}
		return max;
	}
	
	public static HashMap<Move, Integer> negaMaxRoot(Board board, int depth){
		HashMap<Move, Integer> moves = new HashMap<Move, Integer>();
		for(Move m: board.generateLegalMoves()) {
			m.makeMove(board);
			board.updateSideToMove();
			int score = -negaMax(board, depth-1);
			moves.put(m, score);
			m.unmakeMove(board);
			board.updateSideToMove();
		}
		return moves;
	}
}
