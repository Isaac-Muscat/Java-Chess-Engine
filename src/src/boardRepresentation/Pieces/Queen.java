package boardRepresentation.Pieces;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class Queen extends Piece{
	public static final Queen bQueens = new Queen.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BQ).build();
	public static final Queen wQueens = new Queen.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WQ).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Queen build() {
			return new Queen(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Queen(Builder builder) {
		super(builder);
	}

	@Override
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long queens = bitboard;
		long o = board.getOccupied();
		while(queens!=0) {
			int from = Long.numberOfLeadingZeros(queens);
			long queen = BitboardUtils.SQUARE[from];
			long m = BitboardUtils.getDiagonalMask(from);
			long movesBitboard = m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getAntiDiagonalMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getRankMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getFileMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			long captures = board.getOpponentOccupied(color)&movesBitboard;
			long nonCaptures = ~o&movesBitboard;
			genCaptures(captures, queen, board.getPieces(), moves);
			genNonCaptures(nonCaptures, queen, moves);
			
			queens^=queen;
		}
		
		return moves;
	}
	
	public static Queen getQueens(Color color) {
		if(color==Color.WHITE) {
			return wQueens;
		}
		return bQueens;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[59];
		} else {
			bitboard|=BitboardUtils.SQUARE[3];
		}
		return bitboard;
	}

	@Override
	public long genAttackSet(Board board) {
		long movesBitboard = 0;
		long queens = bitboard;
		long o = board.getOccupied();
		while(queens!=0) {
			int from = Long.numberOfLeadingZeros(queens);
			long queen = BitboardUtils.SQUARE[from];
			long m = BitboardUtils.getDiagonalMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getAntiDiagonalMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getRankMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getFileMask(from);
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			
			queens^=queen;
		}
		return movesBitboard;
	}
}
