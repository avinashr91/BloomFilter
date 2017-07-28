import java.util.BitSet;

public class BloomFilterDet {
	private static final long FNV_64INIT = 2166136261L;
    private static final long FNV64PRIME = 16777619L;
	private int counter, filterSize, noOfHashFunctions;
	private BitSet filter;

	public BloomFilterDet(int setSize, int bitsPerElement) {
		this.filterSize = setSize * bitsPerElement;
		this.noOfHashFunctions = (int) (0.6931 * filterSize/setSize); // ln 2 = 0.6931
		filter = new BitSet(filterSize);
		this.counter = 0;
	}
	
	public void add(String s) {
		s = s.toLowerCase();
		long fnv = FNVHashValue(s);
		int index;
		for(int i = 0; i < noOfHashFunctions; i++) {
			index = getIndex(fnv, i);
			filter.set(index);
		}
		counter++;
	}

	public int getIndex(long rv, int hashno) {
		
		long right = rv & 0xFFFFL;
		long left = (rv >> 16) & 0xFFFFL;
		//long index = (long) ((left * hashno + right) % Math.pow(2, 64));
		int index = (int) ((left * hashno + right) % filterSize);
		return index;
	}

	public boolean appears(String s) {
		s = s.toLowerCase();
		long fnv = FNVHashValue(s);
		int index = (int)fnv % filterSize;
		for(int i = 0; i < noOfHashFunctions; i++) {
			index = getIndex(fnv, i);
			if (!filter.get(index)) {
				return false;
			}
		}
		return true;
	}

	public long FNVHashValue(final String k) {
        long rv = FNV_64INIT;
        int len = k.length();
        for(int i = 0; i < len; i++) {
            rv ^= k.charAt(i);
            rv = (long) ((rv * FNV64PRIME) % Math.pow(2, 32));
        }
        return rv;
    }

	public int filterSize() {
		return filterSize;
	}

	public int dataSize() {
		return counter;
	}
}