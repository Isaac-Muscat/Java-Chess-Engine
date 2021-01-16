package utilities;

import utilities.BitboardUtils;

public enum Rank {
	RANK_1(0),
	RANK_2(1),
	RANK_3(2),
	RANK_4(3),
	RANK_5(4),
	RANK_6(5),
	RANK_7(6),
	RANK_8(7);
	
	public static final int NUM_RANKS = 8;
	private final int value;
	public static final long[] RANKS = createRanks();
	private Rank(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static long getRank(Rank rank) {
		return RANKS[rank.getValue()];
	}
	
	private static long[] createRanks() {
		long[] ranks = new long[NUM_RANKS];
		long init = 0;
		for(int i = 0;i<NUM_RANKS; i++) {
			for(int j = 0;j<NUM_RANKS;j++) {
				init |= 1L<<j+i*NUM_RANKS;
			}
			ranks[i] = init;
			init=0;
		}
		return ranks;
	}
}
