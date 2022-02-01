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

	String checksum(List<String> idDigits) {
//		Produkt := 10;
//		for i := 1 to n do
//		begin
//		  Summe := (Zeichenkette[i] + Produkt) MOD 10;
//		  if Summe = 0 then Summe := 10;
//		  Produkt := (Summe * 2) MOD 11;
//		end;
//		(* Prüfzeichenwert berechnen *)
//		Prüfzeichenwert := 11 - Produkt;
//		if Prüfzeichenwert = 10 then Prüfzeichenwert := 0;
		int result = 10;
		for (int i = 0; i < idDigits.size(); i++) {
			int sum = (Integer.valueOf(idDigits.get(i)) + result) % 10;
			if (sum == 0) {
				sum = 10;
			}
			result = (sum * 2) % 11;
		}
		result = 11 - result;
		if (result == 10) {
			result = 0;
		}
		return Integer.toString(result);
	}

	private void addClassic(List<String> idDigits, String multiOccurenceDigit) {
		// insert multiOccurenceDigit anywhere after the first digit
		idDigits.add(1 + nextInt(idDigits.size() - 1), multiOccurenceDigit);
	}

	private int nextInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}
}
