package boardRepresentation.Pieces;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class Knight extends Piece{
	public static final Knight bKnights = new Knight.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BN).build();
	public static final Knight wKnights = new Knight.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WN).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Knight build() {
			return new Knight(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Knight(Builder builder) {
		super(builder);
	}

	@Override
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long knight = bitboard&~nextPieces;			//Bitboard of selected knight
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(knight!=0) {							//Basically loops over every knight (one color)
			
			long m = BitboardUtils.getKnightMask(Long.numberOfLeadingZeros(knight));
			
			long captures = board.getOpponentOccupied(color)&m;
			long nonCaptures = ~o&m&~board.getWhiteOccupied();
			genCaptures(captures, knight, board.getPieces(), moves);
			genNonCaptures(nonCaptures, knight, moves);
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			knight = temp&~nextPieces;
		}
		
		return moves;
	}
	
	public static Knight getKnights(Color color) {
		if(color==Color.WHITE) {
			return wKnights;
		}
		return bKnights;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[57]|BitboardUtils.SQUARE[62];
		} else {
			bitboard|=BitboardUtils.SQUARE[1]|BitboardUtils.SQUARE[6];
		}
		return bitboard;
	}

	@Override
	public long genAttackSet(Board board) {
		long movesBitboard = 0;
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long knight = bitboard&~nextPieces;			//Bitboard of selected knight
		while(knight!=0) {							//Basically loops over every knight (one color)
			
			movesBitboard |= BitboardUtils.getKnightMask(Long.numberOfLeadingZeros(knight));
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			knight = temp&~nextPieces;
		}
		return movesBitboard;
	}
}
