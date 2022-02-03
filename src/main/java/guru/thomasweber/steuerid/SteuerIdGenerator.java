package guru.thomasweber.steuerid;

import static guru.thomasweber.steuerid.Digit.digit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SteuerIdGenerator {

	private static final Digit[] DIGITS = { digit(0), digit(1), digit(2), digit(3), digit(4), digit(5), digit(6),
			digit(7), digit(8), digit(9) };

	public String generate() {
		return generate(ThreadLocalRandom.current().nextBoolean() ? SteuerIdMode.CLASSIC : SteuerIdMode.V2016);
	}

	public String generate(SteuerIdMode mode) {
		List<Digit> availableDigits = Digit.all();
		List<Digit> idDigits = new ArrayList<>(11);

		// first digit is between 1 and 9 inclusive
		final Digit first = availableDigits.get(nextInt(9) + 1);
		idDigits.add(first);
		// remove first chosen digit from available digits
		availableDigits.remove(first);

		// add unique digits
		for (var i = 0; i < mode.uniqueDigits() - 1; i++) {
			Digit next = availableDigits.get(nextInt(availableDigits.size()));
			idDigits.add(next);
			availableDigits.remove(next);
		}

		// pick one existing number to add it according to mode rules
		Digit multiOccurenceDigit = idDigits.get(ThreadLocalRandom.current().nextInt(idDigits.size()));

		if (mode == SteuerIdMode.CLASSIC) {
			addClassic(idDigits, multiOccurenceDigit);
		} else {
			addV2016(idDigits, multiOccurenceDigit);
		}

		// add checksum
		idDigits.add(checkDigit(idDigits));
		return idDigits.stream().map(Object::toString).collect(Collectors.joining());
	}

	void addV2016(List<Digit> idDigits, Digit multiOccurenceDigit) {
		// add the first additional occurrence unconditionally
		addClassic(idDigits, multiOccurenceDigit);
		// insert the second time avoiding triplets
		// choose an offset and then linearly tryout positions
		int offset = nextInt(idDigits.size());
		for (var i = 0; idDigits.size() < 10; i++) {
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

	boolean canInsertAt(int insertPos, Digit multiOccurenceDigit, List<Digit> idDigits) {
		int checkStart = Math.max(0, insertPos - 1);
		for (var p = checkStart; p < idDigits.size() && p < insertPos + 1; p++) {
			if (idDigits.get(p).equals(multiOccurenceDigit)) {
				return false;
			}
		}
		return true;
	}

	Digit checkDigit(List<Digit> idDigits) {
		int result = 10;
		for (int i = 0; i < idDigits.size(); i++) {
			int sum = (idDigits.get(i).intValue() + result) % 10;
			if (sum == 0) {
				sum = 10;
			}
			result = (sum * 2) % 11;
		}
		result = 11 - result;
		if (result == 10) {
			result = 0;
		}
		return DIGITS[result];
	}

	private void addClassic(List<Digit> idDigits, Digit multiOccurenceDigit) {
		// insert multiOccurenceDigit anywhere after the first digit
		idDigits.add(1 + nextInt(idDigits.size() - 1), multiOccurenceDigit);
	}

	private int nextInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}
}
