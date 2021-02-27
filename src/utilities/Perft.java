package utilities;

import java.util.ArrayList;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.*;
import boardRepresentation.Moves.Move;
import boardRepresentation.Pieces.King;

public class Perft {
	public static int promotionCount = 0;
	public static int castleCount = 0;
	public static int captureCount = 0;
	public static int epCount = 0;
	public static int checkmateCount = 0;
	public static int debuggingTemp = 0;
	public static int startingDepth = 0;
	private Perft() throws Exception {
		throw new Exception("Don't Initialize this");
	}
	
	public static void updateCounts(Move m) {
		if(m instanceof Capture || m instanceof KingOrRookCapture) {
			captureCount++;
		} else if(m instanceof CastleKingside || m instanceof CastleQueenside) {
			castleCount++;
		} else if(m instanceof EnPassant) {
			epCount++;
			captureCount++;
		} else if(m instanceof PawnPromotion) {
			promotionCount++;
		} else if(m instanceof PawnPromotionCapture) {
			promotionCount++;
			captureCount++;
		}
	}
	
	public static void printCounts() {
		System.out.println("Captures: "+captureCount);
		System.out.println("Ep: "+epCount);
		System.out.println("Castles: "+castleCount);
		System.out.println("Promotions: "+promotionCount);
		System.out.println("DebuggingTemp: " + debuggingTemp);
		System.out.println("Checkmates/Stalemates: "+checkmateCount + "\n");
	}
	
	public static int perft(Board board, int depth) {
		if(depth == 0) {
			return 1;
		}
		ArrayList<Move> moves = board.generateLegalMoves();
		if(moves.size()==0) {
			checkmateCount++;
		}
		int moveCount = moves.size();
		int nodes = 0;
		
		for(int i=0; i < moveCount; i++) {
			Move m = moves.get(i);
			if(depth == 1) {
				updateCounts(m);
				//System.out.println(m.getInfo());
				//board.print(false);
			}
			m.makeMove(board);
			board.updateSideToMove();
			if(!board.validate()) return 1000000000;
			nodes += perft(board, depth-1);
			m.unmakeMove(board);
			board.updateSideToMove();
		}
		return nodes;
		
	}
}
