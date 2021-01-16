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
		Piece tP = board.getPieces(PieceEnum.WR);
		Collection<Move> moves = tP.genPseudoMoves(board);
		
	}

}
