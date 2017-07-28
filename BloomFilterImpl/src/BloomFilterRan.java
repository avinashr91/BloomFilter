import java.util.BitSet;
import java.util.Random;

public class BloomFilterRan {
	private int noOfStringsAdded, noOfHashFunctions, primeFilterSize;
	private BitSet filter;
	private int randHashConstants[][];


	// Constructor to create a random bloom filter based on the set size given
	public BloomFilterRan(int setSize) {
		
		this.primeFilterSize = GetNextPrime(setSize);
		this.noOfHashFunctions = (int) Math.round(.6931 * primeFilterSize/setSize); // ln 2 = 0.6931
		filter = new BitSet(primeFilterSize);
		this.noOfStringsAdded = 0;
		randHashConstants = new int[noOfHashFunctions][2];
		GenerateHashConstants();
	}

	// If k is the no of functions, generates k pairs of random (a,b) for the k hash functions 
	public void GenerateHashConstants() {
		Random r = new Random();
		for (int i = 0; i < noOfHashFunctions; i++) {
			randHashConstants[i][0] = r.nextInt(primeFilterSize);
			randHashConstants[i][1] = r.nextInt(primeFilterSize);
		}
	}

	// Adds a string to bloom filter
	public void add(String s) {
		s = s.toLowerCase();
		long h = s.hashCode() & 0x7FFFFFFF; // To fix the issue of hashcode sometimes returning negative values 
		int index;
		for(int i = 0; i < noOfHashFunctions; i++) {
			index = getIndex(h, i);
			filter.set(index);
		}
		noOfStringsAdded++;
	}

	// Generates different indexes for a hash function number and string hashcode
	public int getIndex(long h, int i) {
		return (int)((randHashConstants[i][0] * h + randHashConstants[i][1]) % primeFilterSize);
	}

	// Checks if a string is already added to the bloom filter
	public boolean appears(String s) {
		s = s.toLowerCase();
		int h = s.hashCode() & 0x7FFFFFFF;
		int index;
		for(int i = 0; i < noOfHashFunctions; i++) {
			index = getIndex(h, i);
			if (!filter.get(index)) {
				return false;
			}
		}
		return true;
	}

	// Helper function to get the next prime number, given a number
	public int GetNextPrime(int n) {
		int p = (n % 2 == 0) ? n + 1: n + 2;
		while(p>n) {
			if (IsPrime(p)) {
				return p;
			}
			p+=2;
		}
		return 0;
	}

	// Helper function to check if a number is prime
	public boolean IsPrime(int p) {
		int sqrt = (int) Math.sqrt(p) + 1;
		for (int i = 2; i < sqrt; i++) {
			if (p % i == 0) { 
				return false; 
			} 
		} 
		return true;

	}

	// Returns the bloom filter size
	public int filterSize() {
		return primeFilterSize;
	}

	// Returns the number of Strings added to filter
	public int dataSize() {
		return noOfStringsAdded;
	}

}