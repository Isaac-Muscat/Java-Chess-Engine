package boardRepresentation.Pieces;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.KingOrRookCapture;
import boardRepresentation.Moves.KingOrRookNonCapture;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class Rook extends Piece{
	public static final Rook bRooks = new Rook.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BR).build();
	public static final Rook wRooks = new Rook.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WR).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Rook build() {
			return new Rook(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Rook(Builder builder) {
		super(builder);
	}

	@Override
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long rooks = bitboard;
		long o = board.getOccupied();
		while(rooks!=0) {
			int from = Long.numberOfLeadingZeros(rooks);
			long rook = BitboardUtils.SQUARE[from];
			long m = BitboardUtils.getRankMask(from);
			long movesBitboard = m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			
			m = BitboardUtils.getFileMask(from);
			movesBitboard |= m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			
			long captures = board.getOpponentOccupied(color)&movesBitboard;
			long nonCaptures = ~o&movesBitboard;
			genCaptures(captures, from, board.getPieces(), moves);
			genNonCaptures(nonCaptures, from, moves);
			
			rooks^= rook;
		}
		
		return moves;
	}
	
	@Override
	protected void genNonCaptures(long nonCaptures, int from, ArrayList<Move> moves) {
		while(nonCaptures!=0) {
			int to = Long.numberOfLeadingZeros(nonCaptures);
			moves.add(new KingOrRookNonCapture(this, from, to));
			nonCaptures^= BitboardUtils.SQUARE[to];
		}
	}
	@Override
	protected void genCaptures(long captures, int from, Piece[] pieces, ArrayList<Move> moves) {
		while(captures!=0) {
			int to = Long.numberOfLeadingZeros(captures);
			long capture = BitboardUtils.SQUARE[to];
			for(Piece p: pieces) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&capture)) {
					moves.add(new KingOrRookCapture(this, from, to, p));
				}
			}
			captures^=capture;
		}
	}
	
	public static Rook getRooks(Color color) {
		if(color==Color.WHITE) {
			return wRooks;
		}
		return bRooks;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|= BitboardUtils.SQUARE[56]|BitboardUtils.SQUARE[63];
			
		} else {
			bitboard|=BitboardUtils.SQUARE[0]|BitboardUtils.SQUARE[7];
		}
		return bitboard;
	}

	@Override
	public long genAttackSet(Board board) {
		long movesBitboard = 0;
		long rooks = bitboard;
		long o = board.getOccupied();
		while(rooks!=0) {
			int from = Long.numberOfLeadingZeros(rooks);
			long rook = BitboardUtils.SQUARE[from];
			long m = BitboardUtils.getRankMask(from);
			movesBitboard |= m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			m = BitboardUtils.getFileMask(from);
			movesBitboard |= m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			
			rooks^=rook;
		}
		return movesBitboard;
	}
}
