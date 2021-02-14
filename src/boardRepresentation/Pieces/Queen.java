package boardRepresentation.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	public Collection<Move> genPseudoMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long queen = bitboard&~nextPieces;			//Bitboard of selected queen
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(queen!=0) {							//Basically loops over every queen (one color)
			
			long m = BitboardUtils.getDiagonalMask(Long.numberOfLeadingZeros(queen));
			long movesBitboard = m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getAntiDiagonalMask(Long.numberOfLeadingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getRankMask(Long.numberOfTrailingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getFileMask(Long.numberOfLeadingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			long captures = board.getOpponentOccupied(color)&movesBitboard;
			long nonCaptures = ~board.getOccupied()&movesBitboard;
			moves.addAll(genCaptures(captures, queen, board.getPieces()));
			moves.addAll(genNonCaptures(nonCaptures, queen));
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			queen = temp&~nextPieces;
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
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long queen = bitboard&~nextPieces;			//Bitboard of selected queen
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(queen!=0) {							//Basically loops over every queen (one color)
			
			long m = BitboardUtils.getDiagonalMask(Long.numberOfLeadingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getAntiDiagonalMask(Long.numberOfLeadingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getRankMask(Long.numberOfTrailingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			m = BitboardUtils.getFileMask(Long.numberOfLeadingZeros(queen));
			movesBitboard |= m&(((o&m)-2*queen) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(queen)));
			
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			queen = temp&~nextPieces;
		}
		return movesBitboard;
	}
}
