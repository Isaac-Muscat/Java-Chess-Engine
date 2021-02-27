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
		
		Board board = new Board.Builder().init("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10").build();
		board.print(true);
		long startTime = System.nanoTime();
		Perft.startingDepth = 5;
		System.out.println("Nodes searched: "+Perft.perft(board, Perft.startingDepth));
		Perft.printCounts();
		board.print(true);
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)/1000000 + "millisecond");
	}
}
