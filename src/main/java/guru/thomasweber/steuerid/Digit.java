package guru.thomasweber.steuerid;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
final class Digit {

	@EqualsAndHashCode.Include
	private final int intValue;
	private final String str;

	private Digit(int val) {
		if (val < 0 || val > 9) {
			throw new IllegalArgumentException("Value must between 0 and 9");
		}
		this.intValue = val;
		this.str = Integer.toString(val);
	}

	public static Digit digit(int v) {
		return new Digit(v);
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

}