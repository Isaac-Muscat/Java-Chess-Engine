package boardRepresentation.Moves;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Pieces.Pawn;
import boardRepresentation.Pieces.Piece;
import utilities.BitboardUtils;

public class EnPassant extends Move{
	public EnPassant(Piece pieceToMove, int startPos, int endPos) {
		
		super(pieceToMove, startPos, endPos);
		System.out.println(startPos);
	}
	
	@Override
	public void makeMove(Board board) {
		enPassantFile = board.getEPFile();
		Color color = pieceToMove.getColor();
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		pieceToMove.setBitboard((pieceToMove.getBitboard()&~start)|end);
		Pawn pawns = Pawn.getPawns(color.getOther());
		pawns.setBitboard(pawns.getBitboard()&~(BitboardUtils.getFileMask(endPos)&BitboardUtils.getRankMask(startPos)));
		board.setEP(null);
		board.updateColorOccupied();
		board.updateOccupied();
	}

	@Override
	public void unmakeMove(Board board) {
		Color color = pieceToMove.getColor();
		long start = BitboardUtils.SQUARE[0]>>>startPos;
		long end = BitboardUtils.SQUARE[0]>>>endPos;
		pieceToMove.setBitboard((pieceToMove.getBitboard()&~end)|start);
		Pawn pawns = Pawn.getPawns(color.getOther());
		pawns.setBitboard(pawns.getBitboard()|(BitboardUtils.getFileMask(endPos)&BitboardUtils.getRankMask(startPos)));
		board.setEP(enPassantFile);
		board.updateColorOccupied();
		board.updateOccupied();
	}

}
