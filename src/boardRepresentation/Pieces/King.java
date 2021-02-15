package boardRepresentation.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.CastleKingside;
import boardRepresentation.Moves.CastleQueenside;
import boardRepresentation.Moves.KingOrRookCapture;
import boardRepresentation.Moves.KingOrRookNonCapture;
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
	private boolean qCastle = true;
	private boolean kCastle = true;
	
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
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
			
		long m = BitboardUtils.getKingMask(Long.numberOfLeadingZeros(bitboard));
			
		long captures = board.getOpponentOccupied(color)&m;
		long nonCaptures = ~o&m&~board.getWhiteOccupied();
		moves.addAll(this.genCaptures(captures, bitboard, board.getPieces()));
		moves.addAll(this.genNonCaptures(nonCaptures, bitboard));
		moves.addAll(genCastles(board));
		return moves;
	}
	@Override
	protected Collection<Move> genNonCaptures(long nonCaptures, long piece) {
		List<Move> moves = new ArrayList<>();
		
		long otherMoves = nonCaptures&(nonCaptures-1);
		long moveBitboard = nonCaptures&~otherMoves;
		while(moveBitboard!=0) {
			moves.add(new KingOrRookNonCapture(this, Long.numberOfLeadingZeros(piece), Long.numberOfLeadingZeros(moveBitboard)));
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
		return moves;
	}
	@Override
	protected Collection<Move> genCaptures(long captures, long piece, Piece[] pieces) {
		List<Move> moves = new ArrayList<>();
		
		long otherMoves = captures&(captures-1);
		long moveBitboard = captures&~otherMoves;
		while(moveBitboard!=0) {
			for(Piece p: pieces) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&moveBitboard)) {
					moves.add(new KingOrRookCapture(this, Long.numberOfLeadingZeros(piece), Long.numberOfLeadingZeros(moveBitboard), p));
				}
			}
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
		return moves;
	}
	
	private Collection<Move> genCastles(Board board){
		List<Move> moves = new ArrayList<>();
		long attacks = board.getOpponentAttackSet(color);
		long o = board.getOccupied()&~bitboard;
		long dir = color.getDirection();
		if(kCastle&&((o|attacks)&(7L<<29+dir*28))==0) {
			moves.add(new CastleKingside(this));
		} else if(qCastle&&((o|attacks)&(15L<<31+dir*28))==0) {
			moves.add(new CastleQueenside(this));
		}
		
		
		return moves;
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
