package boardRepresentation.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class King extends Piece{
	public static final King bKings = new King.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BK).build();
	public static final King wKings = new King.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WK).build();
	private boolean firstMove = true;
	private boolean qCastleRight = true;
	private boolean kCastleRight = true;
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		King build() {
			return new King(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private King(Builder builder) {
		super(builder);
	}

	@Override
	public Collection<Move> genPseudoMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long king = bitboard&~nextPieces;			//Bitboard of selected king
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(king!=0) {							//Basically loops over every king (one color)
			
			long m = BitboardUtils.getKingMask(Long.numberOfLeadingZeros(king));
			
			long captures = board.getOpponentOccupied(color)&m;
			long nonCaptures = ~o&m&~board.getWhiteOccupied();
			moves.addAll(genCaptures(captures, king, board.getPieces()));
			moves.addAll(genNonCaptures(nonCaptures, king));
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			king = temp&~nextPieces;
		}
		
		return moves;
	}
	
	public static King getKings(Color color) {
		if(color==Color.WHITE) {
			return wKings;
		}
		return bKings;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[60];
		} else {
			bitboard|=BitboardUtils.SQUARE[4];
		}
		return bitboard;
	}
}
