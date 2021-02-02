package utilities;

import utilities.BitboardUtils;

public enum Rank {
	RANK_1(7),
	RANK_2(6),
	RANK_3(5),
	RANK_4(4),
	RANK_5(3),
	RANK_6(2),
	RANK_7(1),
	RANK_8(0);
	
	public static final int NUM_RANKS = 8;
	private final int value;
	public static final long[] RANKS = createRanks();
	private Rank(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Rank returnRankFromIndex(int index) {
		for(Rank rank: Rank.values()) {
			if(rank.getValue() == index/8) return rank;
		}
		new Exception("ERROR");
		return RANK_1;
	}
	
	public static long getRank(Rank rank) {
		return RANKS[7-rank.getValue()];
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
