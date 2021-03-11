package boardRepresentation.Pieces;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.CastleKingside;
import boardRepresentation.Moves.CastleQueenside;
import boardRepresentation.Moves.KingOrRookCapture;
import boardRepresentation.Moves.KingOrRookNonCapture;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class King extends Piece {
	public static final King bKings = new King.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BK).build();
	public static final King wKings = new King.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WK).build();
	private boolean qCastle = false;
	private boolean kCastle = false;
	
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
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long o = board.getOccupied();
		int from = Long.numberOfLeadingZeros(bitboard);
		long m = BitboardUtils.getKingMask(from);
		long captures = board.getOpponentOccupied(color)&m;
		long nonCaptures = ~o&m;
		this.genCaptures(captures,from, board.getPieces(), moves);
		this.genNonCaptures(nonCaptures, from, moves);
		genCastles(board, moves);
		return moves;
	}
	@Override
	protected void genNonCaptures(long nonCaptures, int from, ArrayList<Move> moves) {
		while(nonCaptures!=0) {
			int to = Long.numberOfLeadingZeros(nonCaptures);
			moves.add(new KingOrRookNonCapture(this, from, to));
			nonCaptures^=BitboardUtils.SQUARE[to];
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
	
	private void genCastles(Board board, ArrayList<Move> moves){
		long attacks = board.getOpponentAttackSet(color);
		long o = board.getOccupied()&~bitboard;
		int dir = color.getDirection();
		if(kCastle&&((o|attacks)&(7L<<29+dir*28))==0) {
			moves.add(new CastleKingside(this));
		}
		if((o&(15L<<31+dir*28))==0&&qCastle&&(attacks&(7L<<31+dir*28))==0) {
			moves.add(new CastleQueenside(this));
		}
	}
	
	
	public boolean getCastleKingside() {
		return this.kCastle;
	}
	public boolean getCastleQueenside() {
		return this.qCastle;
	}
	public void setCastleKingside(boolean kCastle) {
		this.kCastle = kCastle;
	}
	public void setCastleQueenside(boolean qCastle) {
		this.qCastle = qCastle;
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

	@Override
	public long genAttackSet(Board board) {
		return BitboardUtils.getKingMask(Long.numberOfLeadingZeros(bitboard));
	}
}
