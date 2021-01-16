package boardRepresentation.Pieces;

import java.util.Collection;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;

public abstract class Piece {
	protected long bitboard;
	protected final Color color;
	protected final PieceEnum pieceType;
	
	public Piece(Builder<?> builder) {
		bitboard = builder.bitboard;
		color = builder.color;
		pieceType = builder.pieceType;
	}
	
	abstract static class Builder<T extends Builder<T>>{
		long bitboard;
		Color color;
		PieceEnum pieceType;
		public T setBitboard(long bitboard) {
			this.bitboard = bitboard;
			return self();
		}
		public T setColor(Color color) {
			this.color = color;
			return self();
		}
		public T setPieceEnum(PieceEnum pieceType) {
			this.pieceType = pieceType;
			return self();
		}
		
		abstract Piece build();
		protected abstract T self();
	}
	
	public abstract Collection<Move> genPseudoMoves(Board board);
	
	public long getBitboard() {
		return bitboard;
	}
	public Color getColor() {
		return color;
	}
	public PieceEnum getPieceEnum() {
		return pieceType;
	}
}
