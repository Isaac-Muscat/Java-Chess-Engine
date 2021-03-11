package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Pieces.Piece;
import boardRepresentation.Pieces.Rook;
import utilities.BitboardUtils;

public class PawnPromotionCapture extends Move{
	private boolean kCastle, qCastle;
	private Piece promotedPiece, capturedPiece;
	public PawnPromotionCapture(Piece pieceToMove, int startPos, int endPos, Piece promotedPiece, Piece capturedPiece) {
		super(pieceToMove, startPos, endPos);
		this.promotedPiece = promotedPiece;
		this.capturedPiece = capturedPiece;
	}

	@Override
	public void makeMove(Board board) {
		enPassantFile = board.getEPFile();
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		pieceToMove.setBitboard(pieceToMove.getBitboard()&~start);
		promotedPiece.setBitboard(promotedPiece.getBitboard()|end);
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
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
		
	}

	@Override
	public void unmakeMove(Board board) {
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		pieceToMove.setBitboard(pieceToMove.getBitboard()|start);
		promotedPiece.setBitboard(promotedPiece.getBitboard()&~end);
		capturedPiece.setBitboard(capturedPiece.getBitboard()|end);
		if(capturedPiece instanceof Rook) {
			capturedPiece.getColor().getKing().setCastleKingside(kCastle);
			capturedPiece.getColor().getKing().setCastleQueenside(qCastle);
		}
		board.setEP(enPassantFile);
		board.updateColorOccupied(pieceToMove.getColor());
		board.updateOccupied();
	}
}
