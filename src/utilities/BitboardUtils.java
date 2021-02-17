package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public final class BitboardUtils {
	public static final int NUM_SQUARES = 64;
	public static final long[] SQUARE = createAllSquares();
	public static final long[] DIAGONAL = createAllDiagonals();
	public static final long[] ANTIDIAGONAL = createAllAntidiagonals();
	public static final long[] RANK = Rank.RANKS;
	public static final long[] FILE = File.FILES;
	public static final long[] KNIGHT_MOVES = createAllKnightMoves();
	public static final long[] KING_MOVES = createAllKingMove();
	
	
	private BitboardUtils() throws Exception {
		throw new Exception("Cannot create BoardUtils object!");
	}
	
	private static long[] createAllKingMove() {
		long[] kingMoves = new long[NUM_SQUARES];
		
		for(int i = 0; i<NUM_SQUARES;i++) {
			HashSet<Integer> offsets = new HashSet<Integer>(Arrays.asList(9, 8, 7, 1, -1, -7, -8, -9));
			long square = SQUARE[i];
			switch(File.returnFileFromIndex(i)) {
				case FILE_A:
					offsets.remove(Integer.valueOf(-9));
					offsets.remove(Integer.valueOf(-1));
					offsets.remove(Integer.valueOf(7));
					break;
				case FILE_H:
					offsets.remove(Integer.valueOf(1));
					offsets.remove(Integer.valueOf(9));
					offsets.remove(Integer.valueOf(-7));
					break;
				default:
					break;
			}
			switch(Rank.returnRankFromIndex(i)) {
				case RANK_1:
					offsets.remove(Integer.valueOf(9));
					offsets.remove(Integer.valueOf(8));
					offsets.remove(Integer.valueOf(7));
					break;
				case RANK_8:
					offsets.remove(Integer.valueOf(-7));
					offsets.remove(Integer.valueOf(-8));
					offsets.remove(Integer.valueOf(-9));
					break;
			default:
				break;
			}
			
			long possibleMoves = 0;
			for(int offset: offsets) {
				possibleMoves|=SQUARE[i+offset];
			}
			kingMoves[i] = possibleMoves;
		}
		
		return kingMoves;
	}

	private static long[] createAllKnightMoves() {
		long[] knightMoves = new long[NUM_SQUARES];
		
		for(int i = 0; i<NUM_SQUARES;i++) {
			HashSet<Integer> offsets = new HashSet<Integer>(Arrays.asList(17, 15, 10, 6, -6, -10, -15, -17));
			long square = SQUARE[i];
			switch(File.returnFileFromIndex(i)) {
				case FILE_A:
					offsets.remove(Integer.valueOf(-10));
					offsets.remove(Integer.valueOf(-17));
					offsets.remove(Integer.valueOf(6));
					offsets.remove(Integer.valueOf(15));
					break;
				case FILE_B:
					offsets.remove(Integer.valueOf(-10));
					offsets.remove(Integer.valueOf(6));
					break;
				case FILE_G:
					offsets.remove(Integer.valueOf(-6));
					offsets.remove(Integer.valueOf(10));
					break;
				case FILE_H:
					offsets.remove(Integer.valueOf(-15));
					offsets.remove(Integer.valueOf(-6));
					offsets.remove(Integer.valueOf(10));
					offsets.remove(Integer.valueOf(17));
					break;
				default:
					break;
			}
			switch(Rank.returnRankFromIndex(i)) {
				case RANK_1:
					offsets.remove(Integer.valueOf(10));
					offsets.remove(Integer.valueOf(17));
					offsets.remove(Integer.valueOf(6));
					offsets.remove(Integer.valueOf(15));
					break;
				case RANK_2:
					offsets.remove(Integer.valueOf(15));
					offsets.remove(Integer.valueOf(17));
					break;
				case RANK_7:
					offsets.remove(Integer.valueOf(-15));
					offsets.remove(Integer.valueOf(-17));
					break;
				case RANK_8:
					offsets.remove(Integer.valueOf(-15));
					offsets.remove(Integer.valueOf(-6));
					offsets.remove(Integer.valueOf(-10));
					offsets.remove(Integer.valueOf(-17));
					break;
			default:
				break;
			}
			
			long possibleMoves = 0;
			for(int offset: offsets) {
				possibleMoves|=SQUARE[i+offset];
			}
			knightMoves[i] = possibleMoves;
		}
		
		return knightMoves;
	}

	private static long[] createAllAntidiagonals() {
		long[] antiDiagonals = new long[15];
		for(int y=7;y>=0;y--) {
			for(int x = 0;x+y<8;x++){
				antiDiagonals[7-y] |= SQUARE[x+(y+x)*8];
			}
		}
		for(int i = 1; i<8;i++) {
			antiDiagonals[i+7] = Long.reverse(antiDiagonals[7-i]);
		}
		
		return antiDiagonals;
	}

	private static long[] createAllDiagonals() {
		long[] diagonals = new long[15];
		for(int y=0;y<8;y++) {
			for(int x = 0;x<=y;x++){
				diagonals[y] |= SQUARE[x+(y-x)*8];
			}
		}
		for(int x=1;x<8;x++) {
			for(int y = 7;y>=x;y--){
				diagonals[x+7] |= SQUARE[(x+7-y)+y*8];
			}
		}
		
		return diagonals;
	}

	public static void printBitboard(long bitboard) {
		String binary = Long.toBinaryString(bitboard);
		int length = binary.length();
		for(int i=0;i<64-length;i++) {
			binary="0"+binary;
		}
		for(int i = 0; i<NUM_SQUARES;i+=8) {
			if(i+8<NUM_SQUARES) {
				System.out.println(binary.substring(i, i+8));
			} else {
				System.out.println(binary.substring(i));
			}
		}
		System.out.println("\n");
	}
	
	public static long getRankMask(int index) {
		return RANK[index/8];
	}
	public static long getFileMask(int index) {
		return FILE[index%8];
	}
	public static long getDiagonalMask(int index) {
		return DIAGONAL[index/8+index%8];
	}
	public static long getAntiDiagonalMask(int index) {
		return ANTIDIAGONAL[Math.abs(index/8-index%8-7)];
	}
	public static long getKnightMask(int index) {
		return KNIGHT_MOVES[index];
	}
	public static long getKingMask(int index) {
		return KING_MOVES[index];
	}
	
	private static long[] createAllSquares() {
		long[] allSquares = new long[NUM_SQUARES];
		long init=1L<<NUM_SQUARES-1;
		for(int i =0;i<NUM_SQUARES;i++) {
			allSquares[i]= init>>>i;
		}
		return allSquares;
	}
	
}
