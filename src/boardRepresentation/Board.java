package boardRepresentation;

import boardRepresentation.Pieces.PieceEnum;
import utilities.BitboardUtils;
import utilities.File;
import boardRepresentation.Pieces.*;

public class Board {
	private Color sideToMove;
	private File enPassantFile;
	private final Piece[] pieces;
	private long blackOccupied, whiteOccupied, occupied;
	private BoardState boardState;
	
	public static class Builder {
		private Piece[] pieces = new Piece[PieceEnum.values().length];
		private long blackOccupied=0;
		private long whiteOccupied=0;
		private long occupied=0;
		private Color sideToMove = Color.WHITE;
		private BoardState boardState=BoardState.NORMAL;
		
		public Builder() {
		}
		public Builder init() {
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
		whiteOccupied = builder.whiteOccupied;
		blackOccupied = builder.blackOccupied;
		occupied = builder.occupied;
	}
	
	public void updateOccupied() {
		this.occupied = blackOccupied | whiteOccupied;
	}
	
	public void updateColorOccupied() {
		this.whiteOccupied = 0;
		this.blackOccupied = 0;
		for(Piece piece: pieces) {
			if(piece.getColor()==Color.WHITE) {
				this.whiteOccupied |= piece.getBitboard();
			} else {
				this.blackOccupied |= piece.getBitboard();
			}
		}
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
}
