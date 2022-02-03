package guru.thomasweber.steuerid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

final class Digit {
	
	private static final Digit[] DIGITS = { of(0), of(1), of(2), of(3), of(4), of(5), of(6),
			of(7), of(8), of(9) };

	private final int intValue;
	private final String str;
	private final int hashCode;

	private Digit(int val) {
		this.intValue = val;
		this.str = Integer.toString(val);
		this.hashCode = Objects.hash(val);
	}
	
	private static Digit of(int v) {
		return new Digit(v);
	}

	public static List<Digit> all() {
		return new ArrayList<>(Arrays.asList(DIGITS));
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
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

}
