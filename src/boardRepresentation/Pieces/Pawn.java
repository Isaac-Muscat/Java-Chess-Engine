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
	public static int epCount = 0;
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
					long epLeft = ((bitboard&Rank.getRank(Rank.RANK_5))<<9)&File.getFile(epFile);
					if(epLeft>0)genEPLeft(epLeft, board, moves);
					long epRight = ((bitboard&Rank.getRank(Rank.RANK_5))<<7)&File.getFile(epFile);
					if(epRight>0)genEPRight(epRight, board, moves);
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
					long epLeftB = ((bitboard&Rank.getRank(Rank.RANK_4))>>>7)&File.getFile(epFileB);
					if(epLeftB>0)genEPLeft(epLeftB, board, moves);
					long epRightB = ((bitboard&Rank.getRank(Rank.RANK_4))>>>9)&File.getFile(epFileB);
					if(epRightB>0)genEPRight(epRightB, board, moves);
				}
				break;
		}
		return moves;
	}
	
	private void genEPLeft(long leftCaptures, Board board, ArrayList<Move> moves) {
		
		int endPos = Long.numberOfLeadingZeros(leftCaptures);
		int direction = color.getDirection();
		moves.add(new EnPassant(this, endPos-direction*(8-direction), endPos));
		epCount++;
		
	}
	
	private void genEPRight(long rightCaptures, Board board, ArrayList<Move> moves) {
		int endPos = Long.numberOfLeadingZeros(rightCaptures);
		int direction = color.getDirection();
		moves.add(new EnPassant(this, endPos-direction*(8+direction), endPos));
		epCount++;
	}

	private void genPromotions(long promotions, ArrayList<Move> moves){
		long otherMoves = promotions&(promotions-1);
		long moveBitboard = promotions&~otherMoves;
		while(moveBitboard!=0) {
			int endPos = Long.numberOfLeadingZeros(moveBitboard);
			int direction = color.getDirection();
			moves.add(new PawnPromotion(this, endPos-direction*8, endPos, Queen.getQueens(color)));
			moves.add(new PawnPromotion(this, endPos-direction*8, endPos, Knight.getKnights(color)));
			moves.add(new PawnPromotion(this, endPos-direction*8, endPos, Rook.getRooks(color)));
			moves.add(new PawnPromotion(this, endPos-direction*8, endPos, Bishop.getBishops(color)));
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
	}
	
 	protected void genCaptures(long rightCaptures, long leftCaptures, Board board, ArrayList<Move> moves) {
 		
		long otherMoves = rightCaptures&(rightCaptures-1);
		long moveBitboard = rightCaptures&~otherMoves;
		while(moveBitboard!=0) {
			int endPos = Long.numberOfLeadingZeros(moveBitboard);
			int direction = color.getDirection();
			for(Piece p: board.getPieces()) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&moveBitboard)) {
					Move.numCaptures++;
					if((moveBitboard&BitboardUtils.getRankMask(31*(1+direction)))!=0) {
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Queen.getQueens(color), p));
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Knight.getKnights(color), p));
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Rook.getRooks(color), p));
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Bishop.getBishops(color), p));
						continue;
					}
					moves.add(new Capture(this, endPos-direction*(8+direction), endPos, p));
				}
			}
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
		
		otherMoves = leftCaptures&(leftCaptures-1);
		moveBitboard = leftCaptures&~otherMoves;
		while(moveBitboard!=0) {
			int endPos = Long.numberOfLeadingZeros(moveBitboard);
			int direction = color.getDirection();
			for(Piece p: board.getPieces()) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&moveBitboard)) {
					Move.numCaptures++;
					if((moveBitboard&BitboardUtils.getRankMask(31*(1+direction)))!=0) {
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Queen.getQueens(color), p));
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Knight.getKnights(color), p));
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Rook.getRooks(color), p));
						moves.add(new PawnPromotionCapture(this, endPos-direction*8, endPos, Bishop.getBishops(color), p));
						continue;
					}
					moves.add(new Capture(this, endPos-direction*(8-direction), endPos, p));
				}
			}
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
	}
	
	@Override
	protected void genNonCaptures(long singleUp, long doubleUp, ArrayList<Move> moves) {
		long otherMoves = singleUp&(singleUp-1);
		long moveBitboard = singleUp&~otherMoves;
		while(moveBitboard!=0) {
			int endPos = Long.numberOfLeadingZeros(moveBitboard);
			moves.add(new NonCapture(this, endPos-(color.getDirection()*8), endPos));
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
		otherMoves = doubleUp&(doubleUp-1);
		moveBitboard = doubleUp&~otherMoves;
		while(moveBitboard!=0) {
			int endPos = Long.numberOfLeadingZeros(moveBitboard);
			moves.add(new DoublePawnPush(this, endPos-(color.getDirection()*16), endPos));
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
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
