package boardRepresentation.Pieces;

import java.util.*;

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
	public Collection<Move> genPseudoMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long rook = bitboard&~nextPieces;			//Bitboard of selected rook
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(rook!=0) {							//Basically loops over every rook (one color)
			
			long m = BitboardUtils.getRankMask(Long.numberOfTrailingZeros(rook));
			long movesBitboard = m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			m = BitboardUtils.getFileMask(Long.numberOfLeadingZeros(rook));
			movesBitboard |= m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			
			long captures = board.getOpponentOccupied(color)&movesBitboard;
			long nonCaptures = ~board.getOccupied()&movesBitboard;
			moves.addAll(genCaptures(captures, rook, board.getPieces()));
			moves.addAll(genNonCaptures(nonCaptures, rook));
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			rook = temp&~nextPieces;
		}
		
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
		long nextPieces = bitboard&(bitboard-1); 	//Bitboard without next piece
		long rook = bitboard&~nextPieces;			//Bitboard of selected rook
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
		while(rook!=0) {							//Basically loops over every rook (one color)
			
			long m = BitboardUtils.getRankMask(Long.numberOfTrailingZeros(rook));
			movesBitboard |= m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			m = BitboardUtils.getFileMask(Long.numberOfLeadingZeros(rook));
			movesBitboard |= m&(((o&m)-2*rook) ^ Long.reverse(Long.reverse(o&m)- 2*Long.reverse(rook)));
			
			
			long temp = nextPieces;
			nextPieces &= nextPieces-1;
			rook = temp&~nextPieces;
		}
		return movesBitboard;
	}
}
