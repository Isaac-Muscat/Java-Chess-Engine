package boardRepresentation.Pieces;

import java.util.Collection;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;
import utilities.Rank;

public class Pawn extends Piece {
	public static final Pawn bPawns = new Pawn.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BP).build();
	public static final Pawn wPawns = new Pawn.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WP).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Pawn build() {
			return new Pawn(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Pawn(Builder builder) {
		super(builder);
	}

	@Override
	public Collection<Move> genPseudoMoves(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Pawn getPawns(Color color) {
		if(color==Color.WHITE) {
			return wPawns;
		}
		return bPawns;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=Rank.getRank(Rank.RANK_2);
		} else {
			bitboard|=Rank.getRank(Rank.RANK_7);
		}
		return bitboard;
	}
}
