package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.King;
import boardRepresentation.Pieces.Piece;
import boardRepresentation.Pieces.PieceEnum;

public class KingOrRookCapture extends Capture{

	private boolean kCastle, qCastle;
	public KingOrRookCapture(Piece pieceToMove, int startPos, int endPos, Piece capturedPiece) {
		super(pieceToMove, startPos, endPos, capturedPiece);
	}

	@Override
	public void makeMove(Board board) {
		kCastle = pieceToMove.getColor().getKing().getCastleKingside();
		qCastle = pieceToMove.getColor().getKing().getCastleQueenside();
		PieceEnum pieceType = pieceToMove.getPieceEnum();
		if(pieceType==PieceEnum.BK||pieceType==PieceEnum.WK) {
			((King) pieceToMove).setCastleKingside(false);
			((King) pieceToMove).setCastleQueenside(false);
		} else {
			switch(endPos+pieceToMove.getColor().getDirection()*28) {
			case 35:
				((King) pieceToMove).setCastleKingside(false);
				break;
			case 28:
				((King) pieceToMove).setCastleQueenside(false);
				break;
			}
		}
		super.makeMove(board);
		
	}

	@Override
	public void unmakeMove(Board board) {
		pieceToMove.getColor().getKing().setCastleKingside(kCastle);
		pieceToMove.getColor().getKing().setCastleQueenside(qCastle);
		super.unmakeMove(board);
		
	}

}
