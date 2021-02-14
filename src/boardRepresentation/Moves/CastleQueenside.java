package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Pieces.King;
import boardRepresentation.Pieces.Piece;
import boardRepresentation.Pieces.Rook;
import utilities.BitboardUtils;

public class CastleQueenside extends Move {
	public CastleQueenside(Piece pieceToMove) {
		super(pieceToMove);
	}

	@Override
	public void makeMove(Board board) {
		Color color = pieceToMove.getColor();
		kCastle = King.getKings(color).getCastleKingside();
		qCastle = King.getKings(color).getCastleQueenside();
		pieceToMove.setBitboard(BitboardUtils.SQUARE[30+color.getDirection()*28]);
		Rook rooks = Rook.getRooks(color);
		rooks.setBitboard(BitboardUtils.SQUARE[31+color.getDirection()*28]|(rooks.getBitboard()&~BitboardUtils.SQUARE[28+color.getDirection()*28]));
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
		rooks.setBitboard(BitboardUtils.SQUARE[28+color.getDirection()*28]|(rooks.getBitboard()&~BitboardUtils.SQUARE[31+color.getDirection()*28]));
		board.updateColorOccupied(color);
		board.updateOccupied();
		((King) pieceToMove).setCastleKingside(kCastle);
		((King) pieceToMove).setCastleQueenside(qCastle);
	}

}
