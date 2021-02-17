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

public class King extends Piece{
	public static int castleCount = 0;
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
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long o = bitboard|board.getOccupied();		//Bitboard of occupied squares
			
		long m = BitboardUtils.getKingMask(Long.numberOfLeadingZeros(bitboard));
			
		long captures = board.getOpponentOccupied(color)&m;
		long nonCaptures = ~o&m&~board.getWhiteOccupied();
		this.genCaptures(captures, bitboard, board.getPieces(), moves);
		this.genNonCaptures(nonCaptures, bitboard, moves);
		genCastles(board, moves);
		return moves;
	}
	@Override
	protected void genNonCaptures(long nonCaptures, long piece, ArrayList<Move> moves) {
		
		long otherMoves = nonCaptures&(nonCaptures-1);
		long moveBitboard = nonCaptures&~otherMoves;
		while(moveBitboard!=0) {
			moves.add(new KingOrRookNonCapture(this, Long.numberOfLeadingZeros(piece), Long.numberOfLeadingZeros(moveBitboard)));
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
	}
	@Override
	protected void genCaptures(long captures, long piece, Piece[] pieces, ArrayList<Move> moves) {
		
		long otherMoves = captures&(captures-1);
		long moveBitboard = captures&~otherMoves;
		while(moveBitboard!=0) {
			for(Piece p: pieces) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&moveBitboard)) {
					moves.add(new KingOrRookCapture(this, Long.numberOfLeadingZeros(piece), Long.numberOfLeadingZeros(moveBitboard), p));
					Move.numCaptures++;
				}
			}
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
	}
	
	private void genCastles(Board board, ArrayList<Move> moves){
		long attacks = board.getOpponentAttackSet(color);
		long o = board.getOccupied()&~bitboard;
		long dir = color.getDirection();
		if(Rook.getRooks(color).getBitboard()==BitboardUtils.SQUARE[35+color.getDirection()*28]&&kCastle&&((o|attacks)&(7L<<29+dir*28))==0) {
			moves.add(new CastleKingside(this));
			castleCount++;
		} else if(Rook.getRooks(color).getBitboard()==BitboardUtils.SQUARE[28+color.getDirection()*28]&&qCastle&&((o|attacks)&(15L<<31+dir*28))==0) {
			moves.add(new CastleQueenside(this));
			castleCount++;
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
