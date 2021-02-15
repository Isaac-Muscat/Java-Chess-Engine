package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Pieces.Piece;
import boardRepresentation.Pieces.Rook;
import boardRepresentation.Pieces.King;
import utilities.BitboardUtils;

public class CastleKingside extends Move{
	private boolean kCastle, qCastle;
	public CastleKingside(Piece pieceToMove) {
		super(pieceToMove);
	}

	@Override
	public void makeMove(Board board) {
		enPassantFile = board.getEPFile();
		Color color = pieceToMove.getColor();
		kCastle = King.getKings(color).getCastleKingside();
		qCastle = King.getKings(color).getCastleQueenside();
		pieceToMove.setBitboard(BitboardUtils.SQUARE[34+color.getDirection()*28]);
		Rook rooks = Rook.getRooks(color);
		rooks.setBitboard(BitboardUtils.SQUARE[33+color.getDirection()*28]|(rooks.getBitboard()&~BitboardUtils.SQUARE[35+color.getDirection()*28]));
		board.setEP(null);
		board.updateColorOccupied(color);
		board.updateOccupied();
		((King) pieceToMove).setCastleKingside(false);
		((King) pieceToMove).setCastleQueenside(false);
	}

	@Override
	public void unmakeMove(Board board) {
		Color color = pieceToMove.getColor();
		pieceToMove.setBitboard(BitboardUtils.SQUARE[32+color.getDirection()*28]);
		Rook rooks = Rook.getRooks(color);
		rooks.setBitboard(BitboardUtils.SQUARE[35+color.getDirection()*28]|(rooks.getBitboard()&~BitboardUtils.SQUARE[33+color.getDirection()*28]));
		board.setEP(enPassantFile);
		board.updateColorOccupied(color);
		board.updateOccupied();
		((King) pieceToMove).setCastleKingside(kCastle);
		((King) pieceToMove).setCastleQueenside(qCastle);
	}

}
