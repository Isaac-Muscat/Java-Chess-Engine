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
		long knights = bitboard;
		long o = board.getOccupied();
		while(knights!=0) {
			int from = Long.numberOfLeadingZeros(knights);
			long knight = BitboardUtils.SQUARE[from];
			long m = BitboardUtils.getKnightMask(from);
			long captures = board.getOpponentOccupied(color)&m;
			long nonCaptures = ~o&m;
			genCaptures(captures, from, board.getPieces(), moves);
			genNonCaptures(nonCaptures, from, moves);
			knights^= knight;
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
		long knights = bitboard;
		while(knights!=0) {
			movesBitboard |= BitboardUtils.getKnightMask(Long.numberOfLeadingZeros(knights));
			knights^=BitboardUtils.SQUARE[Long.numberOfLeadingZeros(knights)];
		}
		return movesBitboard;
	}
}
