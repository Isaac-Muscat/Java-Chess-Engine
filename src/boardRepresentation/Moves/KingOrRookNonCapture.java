package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.King;
import boardRepresentation.Pieces.Piece;
import boardRepresentation.Pieces.PieceEnum;

public class KingOrRookNonCapture extends NonCapture{
	private boolean kCastle, qCastle;
	public KingOrRookNonCapture(Piece pieceToMove, int startPos, int endPos) {
		super(pieceToMove, startPos, endPos);
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
				pieceToMove.getColor().getKing().setCastleKingside(false);
				break;
			case 28:
				pieceToMove.getColor().getKing().setCastleQueenside(false);
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
