package utilities;

import utilities.BitboardUtils;

public enum File {
	FILE_A(0, "a"),
	FILE_B(1, "b"),
	FILE_C(2, "c"),
	FILE_D(3, "d"),
	FILE_E(4, "e"),
	FILE_F(5, "f"),
	FILE_G(6, "g"),
	FILE_H(7, "h");
	
	public static final int NUM_FILES = 8;
	public static final long[] FILES = createFiles();
	private final int value;
	private final String file;
	
	
	private File(int value, String file) {
		this.value = value;
		this.file = file;
	}
	
	public static File returnFileFromIndex(int index) {
		for(File file: File.values()) {
			if(file.getValue() == index%8) return file;
		}
		new Exception("ERROR");
		return FILE_A;
	}
	
	public static long getFile(File file) {
		return FILES[file.getValue()];
	}
	
	private static long[] createFiles() {
		long[] files = new long[NUM_FILES];
		long init = 0;
		for(int j = 0;j<BitboardUtils.NUM_SQUARES;j++) {
			if(j%8 == 7) {
				init |= 1L<<j;
			}
		}
		for(int i = 0;i<NUM_FILES;i++) {
			files[i] = init>>>i;
		}
		return files;
	}
	
	public int getValue() {
		return value;
	}

	public static File getFileFromString(String string) {
		for(File cur: File.values()) {
			if(cur.file.equals(string))return cur;
		}
		return null;
	}
	

}
