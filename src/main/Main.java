package main;

import utilities.*;

import java.util.*;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.*;
import boardRepresentation.Pieces.Bishop;
import boardRepresentation.Pieces.*;
import boardRepresentation.Pieces.PieceEnum;

public class Main {

	public static void main(String[] args) {
		
		Board board = new Board.Builder().init().build();
		long startTime = System.nanoTime();
		//board.print();
		ArrayList<Move> moves = board.generateLegalMoves();//.get(18);
		//System.out.println(move.getInfo());
		//move.makeMove(board);
		//board.updateSideToMove();
		//board.print();
		//move.unmakeMove(board);
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + " ns"); 
	}

}
