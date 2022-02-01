package guru.thomasweber.steuerid;

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
		SteuerIdGenerator generator = new SteuerIdGenerator();
		// when
		String id = generator.generate();
		// then
		assertEquals(11, id.length());
	}

	@Test
	void test_generated_id_does_not_start_with_zero() {
		// given
		SteuerIdGenerator generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			String id = generator.generate();
			// then
			assertNotEquals('0', id.charAt(0), id);
		}
	}

	@Test
	void test_generate_CLASSIC_contains_9_distinct_digits_excluding_checksum() {
		// given
		SteuerIdGenerator generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			String id = generator.generate(SteuerIdMode.CLASSIC);
			// then
			String idWithoutChecksum = id.substring(0, 10);
			assertEquals(SteuerIdMode.CLASSIC.uniqueDigits(),
					idWithoutChecksum.chars().mapToObj(String::valueOf).collect(Collectors.toSet()).size());
		}
	}

	@Test
	void test_generate_V2016_contains_8_distinct_digits_excluding_checksum() {
		// given
		SteuerIdGenerator generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			String id = generator.generate(SteuerIdMode.V2016);
			// then
			String idWithoutChecksum = id.substring(0, 10);
			assertEquals(SteuerIdMode.V2016.uniqueDigits(),
					idWithoutChecksum.chars().mapToObj(String::valueOf).collect(Collectors.toSet()).size());
		}
	}

	@Test
	void test_generate_V2016_has_no_three_times_in_a_row_occurences() {
		// given
		SteuerIdGenerator generator = new SteuerIdGenerator();

		for (int i = 0; i < 10000; i++) {
			// when
			String id = generator.generate(SteuerIdMode.V2016);
			// then
			String idWithoutChecksum = id.substring(0, 10);
			for (var pos = 0; pos < 10; pos++) {
				String triplet = idWithoutChecksum.substring(pos, pos + 1).repeat(3);
				assertEquals(-1, idWithoutChecksum.indexOf(triplet), idWithoutChecksum);
			}
		}
	}

	static Stream<Arguments> checksumValueSource() {
		return Stream.of(Arguments.of("0794", "5"));
	}

	@ParameterizedTest
	@MethodSource(value = "checksumValueSource")
	void test_checksum_computes_correctly(String digitString, String checksum) {
		// given
		SteuerIdGenerator generator = new SteuerIdGenerator();
		var digits = new ArrayList<String>();
		for (int i = 0; i < digitString.length(); i++) {
			digits.add(digitString.substring(i, i + 1));
		}
		// when
		String computedChecksum = generator.checksum(digits);
		assertEquals(checksum, computedChecksum);
	}

}
