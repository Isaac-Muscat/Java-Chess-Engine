package boardRepresentation;

import utilities.BitboardUtils;
import utilities.File;

import java.util.ArrayList;

import boardRepresentation.Moves.Move;
import boardRepresentation.Pieces.*;

public class Board {
	private Color sideToMove;
	private File enPassantFile;
	private final Piece[] pieces;
	private long blackOccupied, whiteOccupied, occupied;
	
	public static class Builder {
		private Piece[] pieces = new Piece[PieceEnum.values().length];
		private long blackOccupied=0;
		private long whiteOccupied=0;
		private long occupied=0;
		private File enPassantFile = null;
		private Color sideToMove = Color.WHITE;
		
		public Builder() {
		}
		public Builder init(String fen) {
			Piece[] pieces = new Piece[PieceEnum.values().length];
			pieces[PieceEnum.BK.getIndex()] = King.getKings(Color.BLACK);
			pieces[PieceEnum.WK.getIndex()] = King.getKings(Color.WHITE);
			pieces[PieceEnum.BQ.getIndex()] = Queen.getQueens(Color.BLACK);
			pieces[PieceEnum.WQ.getIndex()] = Queen.getQueens(Color.WHITE);
			pieces[PieceEnum.BR.getIndex()] = Rook.getRooks(Color.BLACK);
			pieces[PieceEnum.WR.getIndex()] = Rook.getRooks(Color.WHITE);
			pieces[PieceEnum.BB.getIndex()] = Bishop.getBishops(Color.BLACK);
			pieces[PieceEnum.WB.getIndex()] = Bishop.getBishops(Color.WHITE);
			pieces[PieceEnum.BN.getIndex()] = Knight.getKnights(Color.BLACK);
			pieces[PieceEnum.WN.getIndex()] = Knight.getKnights(Color.WHITE);
			pieces[PieceEnum.BP.getIndex()] = Pawn.getPawns(Color.BLACK);
			pieces[PieceEnum.WP.getIndex()] = Pawn.getPawns(Color.WHITE);
			this.pieces = pieces;
			for(Piece piece: pieces) {
				piece.setBitboard(0L);
			}
			String[] attributes = fen.split(" ");
			String[] ranks = attributes[0].split("/");
			for(int rank = 0;rank<ranks.length;rank++) {
				int file = 0;
				for(int i = 0; i<ranks[rank].length();i++) {
					PieceEnum selected = PieceEnum.getPieceByString(Character.toString(ranks[rank].charAt(i)));
					if(selected!=null) {
						Piece piece = pieces[selected.getIndex()];
						long bitboard = piece.getBitboard();
						piece.setBitboard(bitboard|(BitboardUtils.SQUARE[rank*8+file]));
						file++;
					} else {
						file += Character.getNumericValue(ranks[rank].charAt(i));
					}
				}
				
			}
			if(attributes[1].equals("b")) {
				this.sideToMove = Color.BLACK;
			}
			
			for(int i = 0;i<attributes[2].length();i++) {
				if(attributes[2].charAt(i) == 'K') {
					King.getKings(Color.WHITE).setCastleKingside(true);
				}
				if(attributes[2].charAt(i) == 'Q') {
					King.getKings(Color.WHITE).setCastleQueenside(true);
				}
				if(attributes[2].charAt(i) == 'k') {
					King.getKings(Color.BLACK).setCastleKingside(true);
				}
				if(attributes[2].charAt(i) == 'q') {
					King.getKings(Color.BLACK).setCastleQueenside(true);
				}
			}
			
			if(attributes[3]!="-") {
				enPassantFile = File.getFileFromString(Character.toString(attributes[3].charAt(0)));
			}
			
			for(Piece piece: pieces) {
				if(piece.getColor()==Color.WHITE) {
					this.whiteOccupied |= piece.getBitboard();
				} else {
					this.blackOccupied |= piece.getBitboard();
				}
			}
			this.occupied = blackOccupied | whiteOccupied;
			return this;
		}
		
		public Board build(){
			return new Board(this);
		}
	}
	private Board(Builder builder) {
		pieces = builder.pieces;
		sideToMove = builder.sideToMove;
		enPassantFile = builder.enPassantFile;
		whiteOccupied = builder.whiteOccupied;
		blackOccupied = builder.blackOccupied;
		occupied = builder.occupied;
	}
	
	public boolean isInCheck() {
		if((King.getKings(sideToMove).getBitboard()&getOpponentAttackSet(sideToMove))!=0) {
			return true;
		}
		return false;
	}
	
	public BoardState getBoardState(int size) {
		if(size==0) {
			if(isInCheck()) {
				return sideToMove.inCheckMate();
			}
			return BoardState.STALEMATE;
		}
		return BoardState.NORMAL;
		
	}
	
	public ArrayList<Move> generateLegalMoves(){
		ArrayList<Move> moves = new ArrayList<Move>();
		int start = sideToMove.getStartIndex();
		for(int i = start;i<start+6;i++) {
			moves.addAll(pieces[i].genLegalMoves(this));
		}
		return moves;
	}
	
	public void updateSideToMove() {
		this.sideToMove = sideToMove.getOther();
	}
	
	public Color getSideToMove() {
		return this.sideToMove;
	}
	
	public void updateOccupied() {
		this.occupied = blackOccupied | whiteOccupied;
	}
	public void updateColorOccupied(Color color) {
		if(color==Color.BLACK) {
			this.blackOccupied = 0;
			for(int i = PieceEnum.BLACK_INDEX_START;i<PieceEnum.BLACK_INDEX_START+6;i++) {
				this.blackOccupied |= pieces[i].getBitboard();
			}
		}else {
			this.whiteOccupied = 0;
			for(int i = 0;i<PieceEnum.BLACK_INDEX_START;i++) {
				this.whiteOccupied |= pieces[i].getBitboard();
			}
		}
	}
	
	public void updateColorOccupied() {
		this.whiteOccupied = 0;
		for(int i = 0;i<PieceEnum.BLACK_INDEX_START;i++) {
			this.whiteOccupied |= pieces[i].getBitboard();
		}
		this.blackOccupied = 0;
		for(int i = PieceEnum.BLACK_INDEX_START;i<PieceEnum.BLACK_INDEX_START+6;i++) {
			this.blackOccupied |= pieces[i].getBitboard();
		}
	}
	
	public long getOpponentAttackSet(Color color) {
		long attackSet = 0;
		int startIndex=0;
		if(color==Color.WHITE)startIndex=PieceEnum.BLACK_INDEX_START;
		for(int i=startIndex;i<startIndex+6;i++) {
			attackSet|=pieces[i].genAttackSet(this);
		}
		return attackSet;
	}
	
	public Piece[] getPieces() {
		return this.pieces;
	}
	
	public File getEPFile() {
		return enPassantFile;
	}
	
	public Piece getPiece(PieceEnum pieceType) {
		return pieces[pieceType.getIndex()];
	}
	
	public long getOccupied() {
		return occupied;
	}
	
	public long getBlackOccupied() {
		return blackOccupied;
	}
	
	public long getWhiteOccupied() {
		return whiteOccupied;
	}
	
	public long getOpponentOccupied(Color color) {
		if(color==Color.WHITE) return blackOccupied;
		return whiteOccupied;
	}

	public void print(boolean verbose) {
		String[] board = new String[64];
		for(int i = 0;i<64;i++) {
			board[i] = "-";
		}
		for(Piece piece: pieces) {
			long bitboard = piece.getBitboard();
			long otherPieces = bitboard&(bitboard-1);
			long selected = bitboard&~otherPieces;
			while(selected!=0) {
				board[Long.numberOfLeadingZeros(selected)] = piece.getPieceEnum().getSymbol();
				long temp = otherPieces;
				otherPieces &= otherPieces-1;
				selected = temp&~otherPieces;
			}
		}
		for(int i = 0;i<64;i++) {
			if((i+1)%8==0) {
				System.out.println(board[i]);
				continue;
			}
			System.out.print(board[i]);
		}
		System.out.print("\n");
		if(verbose) {
			System.out.println("Side To Move: " + sideToMove);
			System.out.println("En Passant File: " + enPassantFile);
			System.out.println("White kCastle: " + King.getKings(Color.WHITE).getCastleKingside());
			System.out.println("White qCastle: " + King.getKings(Color.WHITE).getCastleQueenside());
			System.out.println("Black kCastle: " + King.getKings(Color.BLACK).getCastleKingside());
			System.out.println("Black qCastle: " + King.getKings(Color.BLACK).getCastleQueenside());
		}
	}
	
	public boolean validate() {
		if(Long.bitCount(King.getKings(Color.WHITE).getBitboard())==1&&Long.bitCount(King.getKings(Color.BLACK).getBitboard())==1) {
			return true;
		}
		return false;
	}

	public void setEP(File file) {
		this.enPassantFile = file;
		
	}
}
