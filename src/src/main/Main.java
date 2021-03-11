package main;

import utilities.*;

import java.util.*;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.*;
import boardRepresentation.Pieces.Bishop;
import boardRepresentation.Pieces.*;
import boardRepresentation.Pieces.PieceEnum;
import evaluation.Evaluation;
import gui.GraphicUserInterface;
import search.Search;

public class Main {

	public static void main(String[] args) {
		Board board = new Board.Builder().init("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -").build();
		GraphicUserInterface gui = new GraphicUserInterface(board, board.getSideToMove());
		/*
		HashMap<Move, Integer> moves = Search.negaMaxRoot(board, 5);
		Move bestMove = Search.getBestMove(moves);
		System.out.println(bestMove.getInfo() + " " + moves.get(bestMove));
		*/
	}
}
