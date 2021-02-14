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
		
		/*
		Board board = new Board.Builder().init().build();
		int iter = 0;
		for(PieceEnum pieceType: PieceEnum.values()) {
			Piece tP = board.getPiece(pieceType);
			ArrayList<Move> moves = (ArrayList<Move>) tP.genPseudoMoves(board);
			for(Move m: moves) {
				m.makeMove(board);
				board.print();
				iter+=1;
				m.unmakeMove(board);
			}
		}
		System.out.println(iter);
		*/
	}

}
