package boardRepresentation.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	public Collection<Move> genPseudoMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long bishop = bitboard&~nextPieces;			//Bitboard of selected bishop
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(bishop!=0) {							//Basically loops over every bishop (one color)
			
			long m = BitboardUtils.getDiagonalMask(Long.numberOfLeadingZeros(bishop));
			long movesBitboard = m&(((o&m)-2*bishop) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(bishop)));
			m = BitboardUtils.getAntiDiagonalMask(Long.numberOfLeadingZeros(bishop));
			movesBitboard |= m&(((o&m)-2*bishop) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(bishop)));
			
			long captures = board.getOpponentOccupied(color)&movesBitboard;
			long nonCaptures = ~board.getOccupied()&movesBitboard;
			moves.addAll(genCaptures(captures, bishop, board.getPieces()));
			moves.addAll(genNonCaptures(nonCaptures, bishop));
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			bishop = temp&~nextPieces;
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

}
