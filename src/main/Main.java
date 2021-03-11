package main;

import utilities.*;

import java.util.*;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.*;
import boardRepresentation.Pieces.*;
import evaluation.Evaluation;
import gui.GraphicUserInterface;
import search.Search;

public class Main {
	public static boolean RUN = false;
	public static Color playerColor;
	public static volatile Color sideToMove;
	public static void main(String[] args) {
		Board board = new Board.Builder().init("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -").build();
		sideToMove = board.getSideToMove();
		playerColor = Color.WHITE;
		GraphicUserInterface gui = new GraphicUserInterface(board);
		while(RUN) {
			if(sideToMove!=playerColor) {
				HashMap<Move, Integer> moves = Search.negaMaxRoot(board, 4);
				Move bestMove = Search.getBestMove(moves);
				bestMove.makeMove(board);
				board.updateSideToMove();
				for(Piece piece: board.getPieces()) {
					gui.pieces[piece.getPieceEnum().getIndex()] = piece.getBitboard();
				}
				sideToMove = playerColor;
				gui.repaint();
			}
		}
	}
	
}
