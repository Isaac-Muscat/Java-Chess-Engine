package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import boardRepresentation.Board;
import boardRepresentation.Moves.*;
import evaluation.Evaluation;

public class Search {
	private Search() {
	}
	
	public static Move getBestMove(HashMap<Move, Integer> moves) {
		Move bestMove = null;
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		for (Move move: moves.keySet()){
			if (bestMove == null || moves.get(move)>moves.get(bestMove)){
				bestMove = move;
		    }
		}
		for (Move move: moves.keySet()){
			if (moves.get(move)==moves.get(bestMove)){
				bestMoves.add(move);
		    }
		}
		Collections.shuffle(bestMoves);
		return bestMoves.get(0);
	}
	
	public static int negaMax(Board board, int depth, int alpha, int beta) {
		if(depth == 0) {
			return Evaluation.evaluate(board);
		}
		int max = -1000000000;
		ArrayList<Move> moves = board.generateLegalMoves();
		if(moves.size()==0) return Evaluation.terminal_evaluate(board);
		
		for(Move m: moves) {
			m.makeMove(board);
			board.updateSideToMove();
			int score = -negaMax(board, depth-1, -beta, -alpha);
			if(score>max) max = score;
			if(score>alpha)alpha=score;
			m.unmakeMove(board);
			board.updateSideToMove();
			if(alpha>=beta)return alpha;
		}
		return max;
	}
	
	public static HashMap<Move, Integer> negaMaxRoot(Board board, int depth){
		int alpha = -1000000000;
		int beta = 1000000000;
		HashMap<Move, Integer> moves = new HashMap<Move, Integer>();
		for(Move m: board.generateLegalMoves()) {
			m.makeMove(board);
			board.updateSideToMove();
			int score = -negaMax(board, depth-1, -beta, -alpha);
			//System.out.println(m.getInfo());
			//System.out.println(score);
			if(score>alpha)alpha= -score;
			moves.put(m, score);
			m.unmakeMove(board);
			board.updateSideToMove();
		}
		return moves;
	}
}
