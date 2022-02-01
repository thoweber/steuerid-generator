package guru.thomasweber.steuerid;

public enum SteuerIdMode {
	CLASSIC(9), V2016(8);

	private int uniqueDigits;

	private SteuerIdMode(int uniqueDigits) {
		this.uniqueDigits = uniqueDigits;
	}

	public int uniqueDigits() {
		return this.uniqueDigits;
	}
}