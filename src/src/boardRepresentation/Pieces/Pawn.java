package boardRepresentation.Pieces;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Capture;
import boardRepresentation.Moves.EnPassant;
import boardRepresentation.Moves.Move;
import boardRepresentation.Moves.NonCapture;
import boardRepresentation.Moves.PawnPromotion;
import boardRepresentation.Moves.PawnPromotionCapture;
import boardRepresentation.Moves.DoublePawnPush;
import utilities.BitboardUtils;
import utilities.File;
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
	public ArrayList<Move> genPseudoMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		long o = board.getOccupied();
		long empty = ~o;
		switch(color) {
			case WHITE:
				long singleUp = ((bitboard&~Rank.getRank(Rank.RANK_7))<<8)&empty;
				long doubleUp = ((singleUp&Rank.getRank(Rank.RANK_3))<<8)&empty;
				genNonCaptures(singleUp, doubleUp, moves);
				long rightCaptures = ((bitboard&~File.getFile(File.FILE_H))<<7)&o;
				long leftCaptures = ((bitboard&~File.getFile(File.FILE_A))<<9)&o;
				genCaptures(rightCaptures, leftCaptures, board, moves);
				long promotions = ((bitboard&Rank.getRank(Rank.RANK_7))<<8)&empty;
				genPromotions(promotions, moves);
				File epFile = board.getEPFile();
				if(epFile!=null&&(bitboard&Rank.getRank(Rank.RANK_5))!=0) {
					long epLeft = ((~File.getFile(File.FILE_A)&bitboard&Rank.getRank(Rank.RANK_5))<<9)&File.getFile(epFile);
					if(epLeft!=0)genEPLeft(epLeft, board, moves);
					long epRight = ((~File.getFile(File.FILE_H)&bitboard&Rank.getRank(Rank.RANK_5))<<7)&File.getFile(epFile);
					if(epRight!=0)genEPRight(epRight, board, moves);
				}
			
				break;
			case BLACK:
				long singleUpB = ((bitboard&~Rank.getRank(Rank.RANK_2))>>>8)&empty;
				long doubleUpB = ((singleUpB&Rank.getRank(Rank.RANK_6))>>>8)&empty;
				genNonCaptures(singleUpB, doubleUpB, moves);
				long rightCapturesB = ((bitboard&~File.getFile(File.FILE_H))>>>9)&o;
				long leftCapturesB = ((bitboard&~File.getFile(File.FILE_A))>>>7)&o;
				genCaptures(rightCapturesB, leftCapturesB, board, moves);
				long promotionsB = ((bitboard&Rank.getRank(Rank.RANK_2))>>>8)&empty;
				genPromotions(promotionsB, moves);
				File epFileB = board.getEPFile();
				if(epFileB!=null&&(bitboard&Rank.getRank(Rank.RANK_4))!=0) {
					long epLeftB = ((~File.getFile(File.FILE_A)&bitboard&Rank.getRank(Rank.RANK_4))>>>7)&File.getFile(epFileB);
					if(epLeftB!=0)genEPLeft(epLeftB, board, moves);
					long epRightB = ((~File.getFile(File.FILE_H)&bitboard&Rank.getRank(Rank.RANK_4))>>>9)&File.getFile(epFileB);
					if(epRightB!=0)genEPRight(epRightB, board, moves);
				}
				break;
		}
		return moves;
	}
	
	private void genEPLeft(long leftCaptures, Board board, ArrayList<Move> moves) {
		
		int to = Long.numberOfLeadingZeros(leftCaptures);
		int direction = color.getDirection();
		moves.add(new EnPassant(this, to-direction*(8-direction), to));
		
		
	}
	
	private void genEPRight(long rightCaptures, Board board, ArrayList<Move> moves) {
		int to = Long.numberOfLeadingZeros(rightCaptures);
		int direction = color.getDirection();
		moves.add(new EnPassant(this, to-direction*(8+direction), to));
	}

	private void genPromotions(long promotions, ArrayList<Move> moves){
		int direction = color.getDirection();
		while(promotions!=0) {
			int to = Long.numberOfLeadingZeros(promotions);
			moves.add(new PawnPromotion(this, to-direction*8, to, Queen.getQueens(color)));
			moves.add(new PawnPromotion(this, to-direction*8, to, Knight.getKnights(color)));
			moves.add(new PawnPromotion(this, to-direction*8, to, Rook.getRooks(color)));
			moves.add(new PawnPromotion(this, to-direction*8, to, Bishop.getBishops(color)));
			promotions^=BitboardUtils.SQUARE[to];
		}
	}
	
 	protected void genCaptures(long rightCaptures, long leftCaptures, Board board, ArrayList<Move> moves) {
 		int direction = color.getDirection();
		while(rightCaptures!=0) {
			int to = Long.numberOfLeadingZeros(rightCaptures);
			long capture = BitboardUtils.SQUARE[to];
			for(Piece p: board.getPieces()) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&capture)) {
					if((capture&BitboardUtils.getRankMask(31*(1+direction)))!=0) {
						moves.add(new PawnPromotionCapture(this, to-direction*(8+direction), to, Queen.getQueens(color), p));
						moves.add(new PawnPromotionCapture(this, to-direction*(8+direction), to, Knight.getKnights(color), p));
						moves.add(new PawnPromotionCapture(this, to-direction*(8+direction), to, Rook.getRooks(color), p));
						moves.add(new PawnPromotionCapture(this, to-direction*(8+direction), to, Bishop.getBishops(color), p));
					} else {
						moves.add(new Capture(this, to-direction*(8+direction), to, p));
					}
				}
			}
			
			rightCaptures^=capture;
		}
		
		while(leftCaptures!=0) {
			int to = Long.numberOfLeadingZeros(leftCaptures);
			long capture = BitboardUtils.SQUARE[to];
			for(Piece p: board.getPieces()) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&capture)) {
					if((capture&BitboardUtils.getRankMask(31*(1+direction)))!=0) {
						moves.add(new PawnPromotionCapture(this, to-direction*(8-direction), to, Queen.getQueens(color), p));
						moves.add(new PawnPromotionCapture(this, to-direction*(8-direction), to, Knight.getKnights(color), p));
						moves.add(new PawnPromotionCapture(this, to-direction*(8-direction), to, Rook.getRooks(color), p));
						moves.add(new PawnPromotionCapture(this, to-direction*(8-direction), to, Bishop.getBishops(color), p));
					} else {
						moves.add(new Capture(this, to-direction*(8-direction), to, p));
					}
				}
			}
			leftCaptures^=capture;
		}
	}
	
	@Override
	protected void genNonCaptures(long singleUp, long doubleUp, ArrayList<Move> moves) {
		int direction = color.getDirection();
		while(singleUp!=0) {
			int to = Long.numberOfLeadingZeros(singleUp);
			moves.add(new NonCapture(this, to-(direction*8), to));
			singleUp^=BitboardUtils.SQUARE[to];
		}
		while(doubleUp!=0) {
			int to = Long.numberOfLeadingZeros(doubleUp);
			moves.add(new DoublePawnPush(this, to-(direction*16), to));
			doubleUp^=BitboardUtils.SQUARE[to];
		}
		
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

	@Override
	public long genAttackSet(Board board) {
		long movesBitboard = 0;
		if(color==Color.WHITE) {
			movesBitboard |= (bitboard&~File.getFile(File.FILE_H))<<7;
			movesBitboard |= (bitboard&~File.getFile(File.FILE_A))<<9;
		}else {
			movesBitboard |= (bitboard&~File.getFile(File.FILE_H))>>>9;
			movesBitboard |= (bitboard&~File.getFile(File.FILE_A))>>>7;
		}
		
		return movesBitboard;
	}
}
