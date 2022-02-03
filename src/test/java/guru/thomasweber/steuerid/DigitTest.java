package guru.thomasweber.steuerid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class DigitTest {

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
	void allReturnsDigitsFromZeroToNine(int expected) {
		// when
		List<Digit> digits = Digit.all();
		// then
		assertEquals(expected, digits.get(expected).intValue());
	}

	@Test
	void allContainsExactlyTenDigits() {
		// when
		List<Digit> digits = Digit.all();
		// then
		assertEquals(10, digits.size());
	}

	static Stream<Digit> digitStreamSource() {
		return Digit.all().stream();
	}

	@ParameterizedTest
	@MethodSource("digitStreamSource")
	void stringValuesCorrespondToIntValues(Digit digit) {
		assertEquals(digit.intValue(), Integer.parseInt(digit.toString(), 10));
	}

	@ParameterizedTest
	@ValueSource(ints = { -2, -1, 10, 11, Integer.MAX_VALUE, Integer.MIN_VALUE })
	void digitThrowsIllegalArgumentExceptionWhenInputOutOfBounds(int i) {
		assertThrows(IllegalArgumentException.class, () -> Digit.digit(i));
	}
	
	@ParameterizedTest
	@MethodSource("digitStreamSource")
	void hashCode(Digit digit) {
		assertEquals(Objects.hash(digit.intValue()), digit.hashCode());
	}
	
}
