package utilities;


public final class BitboardUtils {
	public static final int NUM_SQUARES = 64;
	public static final long[] SQUARE = createAllSquares();
	
	
	private BitboardUtils() {
		new Exception("Cannot create BoardUtils object!");
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
		return Rank.RANKS[index/8];
	}
	public static long getFileMask(int index) {
		return File.FILES[index%8];
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
