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
		System.out.println(bestMoves.size());
		return bestMoves.get(0);
	}
	
	public static int negaMax(Board board, int depth) {
		if(depth == 0) {
			return Evaluation.evaluate(board);
		}
		int max = -1000000000;
		ArrayList<Move> moves = board.generateLegalMoves();
		switch(board.getBoardState(moves.size())) {
		case STALEMATE:
			return 0;
		case BLACK_IN_CHECKMATE:
			return 1000000000;
		case WHITE_IN_CHECKMATE:
			return 1000000000;
		default:
			break;
		}
		for(Move m: moves) {
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
			//System.out.println(m.getInfo() + " " + moves.get(m));
			m.unmakeMove(board);
			board.updateSideToMove();
		}
		return moves;
	}
}
