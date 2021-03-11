package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.*;
import utilities.BitboardUtils;

public class Capture extends Move{
	private Piece capturedPiece;
	private boolean kCastle, qCastle;
	public Capture(Piece pieceToMove, int startPos, int endPos, Piece capturedPiece) {
		super(pieceToMove, startPos, endPos);
		this.capturedPiece = capturedPiece;
	}
	@Override
	public void makeMove(Board board) {
		enPassantFile = board.getEPFile();
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		pieceToMove.setBitboard((pieceToMove.getBitboard()&~start)|end);
		capturedPiece.setBitboard(capturedPiece.getBitboard()&~end);
		if(capturedPiece instanceof Rook) {
			kCastle = capturedPiece.getColor().getKing().getCastleKingside();
			qCastle = capturedPiece.getColor().getKing().getCastleQueenside();
			if((end&BitboardUtils.SQUARE[35-capturedPiece.getColor().getDirection()*28])!=0) {
				capturedPiece.getColor().getKing().setCastleKingside(false);
			} else if((end&BitboardUtils.SQUARE[28-capturedPiece.getColor().getDirection()*28])!=0) {
				capturedPiece.getColor().getKing().setCastleQueenside(false);
			}
		}
		board.setEP(null);
		board.updateColorOccupied();
		board.updateOccupied();
		
	}

	@Override
	public void unmakeMove(Board board) {
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		pieceToMove.setBitboard((pieceToMove.getBitboard()&~end)|start);
		if(capturedPiece instanceof Rook) {
			capturedPiece.getColor().getKing().setCastleKingside(kCastle);
			capturedPiece.getColor().getKing().setCastleQueenside(qCastle);
		}
		capturedPiece.setBitboard(capturedPiece.getBitboard()|end);
		board.setEP(enPassantFile);
		board.updateColorOccupied();
		board.updateOccupied();
	}

}
