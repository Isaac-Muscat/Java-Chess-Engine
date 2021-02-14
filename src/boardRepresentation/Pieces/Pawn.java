package boardRepresentation.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Capture;
import boardRepresentation.Moves.EnPassant;
import boardRepresentation.Moves.Move;
import boardRepresentation.Moves.NonCapture;
import boardRepresentation.Moves.PawnPromotion;
import boardRepresentation.Moves.PawnPromotionCapture;
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
	public Collection<Move> genPseudoMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		long empty = ~board.getOccupied();
		switch(color) {
			case WHITE:
				//gen single moves
				long singleUp = ((bitboard&~Rank.getRank(Rank.RANK_7))<<8)&empty;
				//gen double moves
				long doubleUp = ((((bitboard&Rank.getRank(Rank.RANK_2))<<8)&empty)<<8)&empty;
				moves.addAll(genNonCaptures(singleUp, doubleUp));
				//gen captures
				long rightCaptures = ((bitboard&~File.getFile(File.FILE_H))<<7)&~empty;
				long leftCaptures = ((bitboard&~File.getFile(File.FILE_A))<<9)&~empty;
				moves.addAll(genCaptures(rightCaptures, leftCaptures, board));
				//gen promotions
				long promotions = ((bitboard&Rank.getRank(Rank.RANK_7))<<8)&empty;
				moves.addAll(genPromotions(promotions));
				//gen en passant
				File epFile = board.getEPFile();
				if(epFile!=null) {
					long epLeft = (bitboard&Rank.getRank(Rank.RANK_5)<<9)&File.getFile(epFile);
					long epRight = (bitboard&Rank.getRank(Rank.RANK_5)<<7)&File.getFile(epFile);
					moves.addAll(genEP(epRight, epLeft, board));
				}
				break;
			case BLACK:
				//gen single moves
				long singleUpB = ((bitboard&~Rank.getRank(Rank.RANK_2))>>>8)&empty;
				//gen double moves
				long doubleUpB = ((((bitboard&Rank.getRank(Rank.RANK_7))>>>8)&empty)>>>8)&empty;
				moves.addAll(genNonCaptures(singleUpB, doubleUpB));
				//gen captures
				long rightCapturesB = ((bitboard&~File.getFile(File.FILE_H))>>>9)&~empty;
				long leftCapturesB = ((bitboard&~File.getFile(File.FILE_A))>>>7)&~empty;
				moves.addAll(genCaptures(rightCapturesB, leftCapturesB, board));
				//gen promotions
				long promotionsB = ((bitboard&Rank.getRank(Rank.RANK_2))>>>8)&empty;
				moves.addAll(genPromotions(promotionsB));
				//gen en passant
				File epFileB = board.getEPFile();
				if(epFileB!=null) {
					long epLeftB = (bitboard&Rank.getRank(Rank.RANK_4)>>>7)&File.getFile(epFileB);
					long epRightB = (bitboard&Rank.getRank(Rank.RANK_4)>>>9)&File.getFile(epFileB);
					moves.addAll(genEP(epRightB, epLeftB, board));
				}
				break;
		}
		
		
		
		return moves;
	}
	
	private Collection<Move> genEP(long rightCaptures, long leftCaptures, Board board) {
		List<Move> moves = new ArrayList<>();
		
		int endPos = Long.numberOfLeadingZeros(rightCaptures);
		int direction = color.getDirection();
		moves.add(new EnPassant(this, endPos-direction*(8+direction), endPos));
		
		endPos = Long.numberOfLeadingZeros(leftCaptures);
		direction = color.getDirection();
		moves.add(new EnPassant(this, endPos-direction*(8-direction), endPos));
		
		return moves;
	}

	private Collection<Move> genPromotions(long promotions){
		List<Move> moves = new ArrayList<>();
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
		
		
		return moves;
	}
	
 	protected Collection<Move> genCaptures(long rightCaptures, long leftCaptures, Board board) {
		List<Move> moves = new ArrayList<>();
		
		long otherMoves = rightCaptures&(rightCaptures-1);
		long moveBitboard = rightCaptures&~otherMoves;
		while(moveBitboard!=0) {
			int endPos = Long.numberOfLeadingZeros(moveBitboard);
			int direction = color.getDirection();
			for(Piece p: board.getPieces()) {
				if(p.getColor()!=this.color && 0!=(p.getBitboard()&moveBitboard)) {
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
					moves.add(new Capture(this, endPos-direction*(8-direction), endPos, p));
				}
			}
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
		
		return moves;
	}
	
	@Override
	protected Collection<Move> genNonCaptures(long singleUp, long doubleUp) {
		List<Move> moves = new ArrayList<>();
		
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
			moves.add(new NonCapture(this, endPos-(color.getDirection()*16), endPos));
			
			long temp = otherMoves;
			otherMoves &= otherMoves-1;
			moveBitboard = temp&~otherMoves;
		}
		
		return moves;
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
