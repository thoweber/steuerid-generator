package guru.thomasweber.steuerid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SteuerIdGenerator {

	private static final String[] DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public String generate() {
		return generate(SteuerIdMode.V2016);
	}

	public String generate(SteuerIdMode mode) {
		List<String> availableDigits = new ArrayList<>(Arrays.asList(DIGITS));
		List<String> idDigits = new ArrayList<>();

		// first digit is between 1 and 9 inclusive
		idDigits.add(availableDigits.get(nextInt(9) + 1));
		// remove first chosen digit from available digits
		availableDigits.remove(idDigits.get(0));

		// remove enough random digits to ensure uniqueness
		while (availableDigits.size() >= mode.uniqueDigits()) {
			availableDigits.remove(nextInt(availableDigits.size()));
		}

		// add unique digits
		for (var i = 0; i < mode.uniqueDigits() - 1; i++) {
			String next = availableDigits.get(nextInt(availableDigits.size()));
			idDigits.add(next);
			availableDigits.remove(next);
		}

		// pick one existing number to add it according to mode rules
		String multiOccurenceDigit = idDigits.get(ThreadLocalRandom.current().nextInt(idDigits.size()));

		if (mode == SteuerIdMode.CLASSIC) {
			addClassic(idDigits, multiOccurenceDigit);
		} else {
			addV2016(idDigits, multiOccurenceDigit);
		}

		// add checksum
		idDigits.add("C");
		return idDigits.stream().map(Object::toString).collect(Collectors.joining());
	}

	void addV2016(List<String> idDigits, String multiOccurenceDigit) {
		// add the first additional occurrence unconditionally
		addClassic(idDigits, multiOccurenceDigit);
		// insert the second time avoiding triplets
		// choose an offset and then linearly tryout positions
		int offset = nextInt(idDigits.size());
		for (var i = 0; i < 10 && idDigits.size() < 10; i++) {
			int insertPos = (offset + i) % idDigits.size();
			// do not insert at the first position in order to avoid inserting a zero
			if (insertPos == 0) {
				continue;
			}
			boolean canInsert = canInsertAt(insertPos, multiOccurenceDigit, idDigits);
			if (canInsert) {
				idDigits.add(insertPos, multiOccurenceDigit);
			}
		}
	}

	boolean canInsertAt(int insertPos, String multiOccurenceDigit, List<String> idDigits) {
		int checkStart = Math.max(0, insertPos - 1);
		for (var p = checkStart; p < idDigits.size() && p < insertPos + 1; p++) {
			if (idDigits.get(p).equals(multiOccurenceDigit)) {
				return false;
			}
		}
		return true;
	}

	void addClassic(List<String> idDigits, String multiOccurenceDigit) {
		// insert multiOccurenceDigit anywhere after the first digit
		idDigits.add(1 + nextInt(idDigits.size() - 1), multiOccurenceDigit);
	}

	private int nextInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}
}
