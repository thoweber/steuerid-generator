package guru.thomasweber.steuerid;

import java.util.ArrayList;
import java.util.Arrays;

final class Digit {
	
	private static final Digit[] DIGITS = { of(0), of(1), of(2), of(3), of(4), of(5), of(6),
			of(7), of(8), of(9) };

	private final int intValue;
	private final String str;

	private Digit(int val) {
		if (val < 0 || val > 9) {
			throw new IllegalArgumentException("Value must between 0 and 9");
		}
		this.intValue = val;
		this.str = Integer.toString(val);
	}
	
	public static ArrayList<Digit> all() {
		return new ArrayList<>(Arrays.asList(DIGITS));
	}
	
	public static Digit of(int v) {
		return new Digit(v);
	}

	public static Digit digit(int v) {
		if (v < 0 || v > 9) {
			throw new IllegalArgumentException("Value must between 0 and 9");
		}
		return DIGITS[v];
	}

	public static Digit digit(String s) {
		return digit(Integer.valueOf(s, 10));
	}

	int intValue() {
		return intValue;
	}

	@Override
	public String toString() {
		return str;
	}

	@Override
	public int hashCode() {
		return intValue;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

}
