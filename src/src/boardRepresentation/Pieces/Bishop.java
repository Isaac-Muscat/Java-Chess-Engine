package boardRepresentation.Pieces;

import java.util.ArrayList;

import utilities.BitboardUtils;
import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;

public class Bishop extends Piece{
	public static final Bishop bBishops = new Bishop.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BB).build();
	public static final Bishop wBishops = new Bishop.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WB).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Bishop build() {
			return new Bishop(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Bishop(Builder builder) {
		super(builder);
	}

	@Override
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long bishops = bitboard;
		long o = board.getOccupied();
		while(bishops!=0) {
			int from = Long.numberOfLeadingZeros(bishops);
			long bishop = BitboardUtils.SQUARE[from];
			long m = BitboardUtils.getDiagonalMask(from);
			long movesBitboard = m&(((o&m)-2*bishop) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(bishop)));
			m = BitboardUtils.getAntiDiagonalMask(from);
			movesBitboard |= m&(((o&m)-2*bishop) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(bishop)));
			
			long captures = board.getOpponentOccupied(color)&movesBitboard;
			long nonCaptures = ~o&movesBitboard;
			genCaptures(captures, from, board.getPieces(), moves);
			genNonCaptures(nonCaptures, from, moves);
			bishops^=bishop;
		}
		
		return moves;
	}
	
	public static Bishop getBishops(Color color) {
		if(color==Color.WHITE) {
			return wBishops;
		}
		return bBishops;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[58]|BitboardUtils.SQUARE[61];
		} else {
			bitboard|=BitboardUtils.SQUARE[2]|BitboardUtils.SQUARE[5];
		}
		return bitboard;
	}

	@Override
	public long genAttackSet(Board board) {
		long movesBitboard = 0;
		long bishops = bitboard;
		long o = board.getOccupied();
		while(bishops!=0) {
			int pos = Long.numberOfLeadingZeros(bishops);
			long bishop = BitboardUtils.SQUARE[pos];
			long m = BitboardUtils.getDiagonalMask(pos);
			movesBitboard |= m&(((o&m)-2*bishop) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(bishop)));
			m = BitboardUtils.getAntiDiagonalMask(pos);
			movesBitboard |= m&(((o&m)-2*bishop) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(bishop)));
			bishops^=bishop;
		}
		return movesBitboard;
	}

}
