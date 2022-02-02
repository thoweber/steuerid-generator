package guru.thomasweber.steuerid;

import static guru.thomasweber.steuerid.Digit.digit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SteuerIdGeneratorTest {

	@Test
	void test_generated_id_has_11_digits() {
		// given
		var generator = new SteuerIdGenerator();
		// when
		var id = generator.generate();
		// then
		assertEquals(11, id.length());
	}

	@Test
	void test_generated_id_does_not_start_with_zero() {
		// given
		var generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			var id = generator.generate();
			// then
			assertNotEquals('0', id.charAt(0), id);
		}
	}

	@Test
	void test_generate_CLASSIC_contains_9_distinct_digits_excluding_checksum() {
		// given
		var generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			var id = generator.generate(SteuerIdMode.CLASSIC);
			// then
			var idWithoutChecksum = id.substring(0, 10);
			assertEquals(SteuerIdMode.CLASSIC.uniqueDigits(),
					idWithoutChecksum.chars().mapToObj(String::valueOf).collect(Collectors.toSet()).size());
		}
	}

	@Test
	void test_generate_V2016_contains_8_distinct_digits_excluding_checksum() {
		// given
		var generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			var id = generator.generate(SteuerIdMode.V2016);
			// then
			var idWithoutChecksum = id.substring(0, 10);
			assertEquals(SteuerIdMode.V2016.uniqueDigits(),
					idWithoutChecksum.chars().mapToObj(String::valueOf).collect(Collectors.toSet()).size());
		}
	}

	@Test
	void test_generate_V2016_has_no_three_times_in_a_row_occurences() {
		// given
		var generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			var id = generator.generate(SteuerIdMode.V2016);
			// then
			var idWithoutChecksum = id.substring(0, 10);
			for (var pos = 0; pos < 10; pos++) {
				String triplet = idWithoutChecksum.substring(pos, pos + 1).repeat(3);
				assertEquals(-1, idWithoutChecksum.indexOf(triplet), idWithoutChecksum);
			}
		}
	}

	@Test
	void test_generated_id_ends_with_check_digit() {
		// given
		SteuerIdGenerator generator = new SteuerIdGenerator();
		for (int i = 0; i < 500; i++) {
			// when
			String id = generator.generate(i % 2 == 0 ? SteuerIdMode.CLASSIC : SteuerIdMode.V2016);
			// then
			var digits = stringToList(id.substring(0, 10));
			var computedChecksum = generator.checkDigit(digits);
			assertEquals(computedChecksum, digit(id.substring(10)));
		}
	}

	static Stream<Arguments> checksumValueSource() {
		return Stream.of(Arguments.of("0794", digit(5)));
	}

	@ParameterizedTest
	@MethodSource(value = "checksumValueSource")
	void test_checksum_computes_correctly(String digitString, Digit checksum) {
		// given
		var generator = new SteuerIdGenerator();
		// when
		var digits = stringToList(digitString);
		var computedChecksum = generator.checkDigit(digits);
		assertEquals(checksum, computedChecksum);
	}

	private ArrayList<Digit> stringToList(String digitString) {
		var digits = new ArrayList<Digit>();
		for (int i = 0; i < digitString.length(); i++) {
			digits.add(digit(digitString.substring(i, i + 1)));
		}
		return digits;
	}

}
