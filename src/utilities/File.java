package utilities;

import utilities.BitboardUtils;

public enum File {
	FILE_A(0),
	FILE_B(1),
	FILE_C(2),
	FILE_D(3),
	FILE_E(4),
	FILE_F(5),
	FILE_G(6),
	FILE_H(7);
	
	public static final int NUM_FILES = 8;
	public static final long[] FILES = createFiles();
	private final int value;
	
	
	private File(int value) {
		this.value = value;
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
	

}
