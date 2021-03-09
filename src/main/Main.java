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
import search.Search;

public class Main {

	public static void main(String[] args) {
		
		Board board = new Board.Builder().init("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ").build();
		board.print(true);
		HashMap<Move, Integer> moves = Search.negaMaxRoot(board, 4);
		Move bestMove = Search.getBestMove(moves);
		System.out.println(bestMove.getInfo() + " " + moves.get(bestMove));
	}
}
