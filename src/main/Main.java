package main;


import java.util.*;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.*;
import boardRepresentation.Pieces.*;
import gui.GraphicUserInterface;
import search.Search;

public class Main {
	public static boolean RUN = false;
	public static Color playerColor;
	public static volatile Color sideToMove;
	public static String startPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -";
	public static String checkmateTest = "rnbqkbnr/pppp1ppp/8/4p3/8/5P2/PPPPP1PP/RNBQKBNR w KQkq -";
	public static void main(String[] args) {
		Board board = new Board.Builder().init(startPos).build();
		sideToMove = board.getSideToMove();
		playerColor = Color.WHITE;
		GraphicUserInterface gui = new GraphicUserInterface(board);
		while(RUN) {      `
			if(sideToMove!=playerColor) {
				HashMap<Move, Integer> moves = Search.negaMaxRoot(board, 4);
				Move bestMove = Search.getBestMove(moves);
				bestMove.makeMove(board);
				board.updateSideToMove();
				for(Piece piece: board.getPieces()) {
					if(Main.playerColor==boardRepresentation.Color.BLACK) {
						gui.pieces[piece.getPieceEnum().getIndex()] = Long.reverse(piece.getBitboard());
						continue;
					}
					gui.pieces[piece.getPieceEnum().getIndex()] = piece.getBitboard();
				}
				sideToMove = playerColor;
				gui.repaint();
			}
		}
	}
	
}
