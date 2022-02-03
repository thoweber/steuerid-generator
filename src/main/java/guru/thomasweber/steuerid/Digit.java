package guru.thomasweber.steuerid;

final class Digit {

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

	@Override
	public int hashCode() {
		return intValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (hashCode() != obj.hashCode()) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return getClass() == obj.getClass();
	}

}