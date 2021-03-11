package boardRepresentation.Pieces;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Capture;
import boardRepresentation.Moves.Move;
import boardRepresentation.Moves.NonCapture;
import utilities.BitboardUtils;

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
	
	public abstract ArrayList<Move> genPseudoMoves(Board board);
	public abstract long genAttackSet(Board board);
	
	public long getBitboard() {
		return bitboard;
	}
	
	public void setBitboard(long bitboard) {
		this.bitboard = bitboard;
	}
	public Color getColor() {
		return color;
	}
	public PieceEnum getPieceEnum() {
		return pieceType;
	}
	
	public ArrayList<Move> genLegalMoves(Board board){
		ArrayList<Move> moves = genPseudoMoves(board);
		for(int i = moves.size()-1;i>=0;i--) {
			moves.get(i).makeMove(board);
			long attacks = board.getOpponentAttackSet(color);
			long kingBitboard = King.getKings(color).getBitboard();
			moves.get(i).unmakeMove(board);
			if((attacks&kingBitboard)!=0) {
				moves.remove(i);
			}
		}
		return moves;
	}
	protected void genNonCaptures(long nonCaptures, long piece, ArrayList<Move> moves) {
		
		long otherMoves = nonCaptures&(nonCaptures-1);
		long moveBitboard = nonCaptures&~otherMoves;
		while(moveBitboard!=0) {
			moves.add(new NonCapture(this, Long.numberOfLeadingZeros(piece), Long.numberOfLeadingZeros(moveBitboard)));
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
	}
	protected void genCaptures(long captures, long piece, Piece[] pieces, ArrayList<Move> moves) {
		long otherMoves = captures&(captures-1);
		long moveBitboard = captures&~otherMoves;
		while(moveBitboard!=0) {
			for(Piece p: pieces) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&moveBitboard)) {
					moves.add(new Capture(this, Long.numberOfLeadingZeros(piece), Long.numberOfLeadingZeros(moveBitboard), p));
				}
			}
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
	}
	protected void genNonCaptures(long nonCaptures, int from, ArrayList<Move> moves) {
		while(nonCaptures!=0) {
			int to = Long.numberOfLeadingZeros(nonCaptures);
			moves.add(new NonCapture(this, from, to));
			nonCaptures^=BitboardUtils.SQUARE[to];
		}
	}
	protected void genCaptures(long captures, int from, Piece[] pieces, ArrayList<Move> moves) {
		while(captures!=0) {
			for(Piece p: pieces) {
				if(p.getColor()!=this.color) {
					long newCaptures = p.getBitboard()&captures;
					while(newCaptures!=0) {
						int to = Long.numberOfLeadingZeros(newCaptures);
						moves.add(new Capture(this, from, to, p));
						long capture = BitboardUtils.SQUARE[to];
						captures^= capture;
						newCaptures^=capture;
					}
				}
			}
		}
	}
	
}
