package main;

import utilities.*;

import java.util.*;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.*;
import boardRepresentation.Pieces.Bishop;
import boardRepresentation.Pieces.*;
import boardRepresentation.Pieces.PieceEnum;
import utilities.Perft;

public class Main {

	public static void main(String[] args) {
		
		Board board = new Board.Builder().init().build();
		long startTime = System.nanoTime();
		System.out.println("Nodes searched: "+Perft.perft(board, 4));
		System.out.println(Move.numCaptures);
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)/1000000 + "millisecond");
		/*
		Piece p = board.getPiece(PieceEnum.WP);
		long startTime = System.nanoTime();
		ArrayList<Move> mofves = board.generateLegalMoves();
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + "ns"); 
		*/
	}

}
