package numberrangesummarizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

public class NumberRangeSummarizerTest {
    String empty_input = "";
    String spaces_only_input = "     ";
    String commas_only_input = ",,,,,,,";
    String comma_spaces_input = ", ,,  ,   , ,,  ";
    String correct_input = "1,2,3,4,4,5,6,6,6,6";
    String non_sequential = "1,3,6,11,13,16,80";
    String incorrect_input = "a,b,c,d,e,f,g";
    String sample_input = "1,3,6,7,8,12,13,14,15,21,22,23,24,31";

    /*
     * Tests for managing the input data
     */
    // An empty string gives and empty array
    @Test
    public void EmptyStringGivesEmptyList() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        Collection<Integer> input = summerizer.collect(empty_input);
        assertEquals(0, input.size());
        // Spaces only input is empty input
        input = summerizer.collect(spaces_only_input);
        assertEquals(0, input.size());
        // Commas only input is empty input
        input = summerizer.collect(commas_only_input);
        assertEquals(0, input.size());
        // Commas and spaces only input is empty input
        input = summerizer.collect(comma_spaces_input);
        assertEquals(0, input.size());
    }

    // Number of numbers on list should be same as the returned list
    @Test
    public void NumberOfNumbersOnInputSameAsList() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        // input where commas separates numbers
        Collection<Integer> input = summerizer.collect(correct_input);
        int size_of_input = (int) correct_input.chars().filter(ch -> ch == ',').count();
        if (correct_input.length() != 0)
            size_of_input++;
        assertEquals(size_of_input, input.size());
        // input where commas can follow each other
        String loacl_input = ",,,,1,,,2,,3,,,";
        input = summerizer.collect(loacl_input);
        assertEquals(3, input.size());
        // spaces are ignored
        loacl_input = ",,,, 1 ,,,2 ,,3 ,,,";
        input = summerizer.collect(loacl_input);
        assertEquals(3, input.size());
    }

    // all numbers should be present on the list
    @Test
    public void InputNumberPresentInList() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        // normal format input
        Collection<Integer> input = summerizer.collect(correct_input);
        String[] splitted_array = correct_input.split(",");
        for (String item : splitted_array) {
            assertTrue(input.contains(Integer.parseInt(item)));
        }
        // input where commas can follow each other
        String loacl_input = ",,,,1,,,2,,3,,,";
        input = summerizer.collect(loacl_input);
        assertTrue(input.contains(1));
        assertTrue(input.contains(2));
        assertTrue(input.contains(3));
        assertEquals(3, input.size()); // check these three numbers are the only ones on the list
    }

    // invalid characters in input throws NumberFormatException,
    @Test
    public void InvalidCharacterThrowsNumberFormatException() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        // Invalid character throws NumberFormatException
        assertThrows(NumberFormatException.class, () -> {
            summerizer.collect(incorrect_input);
        }, "NumberFormatException was expected");

        // Numbers separated by spaces throws NumberFormatException
        assertThrows(NumberFormatException.class, () -> {
            summerizer.collect("1 2 3");
        }, "NumberFormatException was expected");
        assertThrows(NumberFormatException.class, () -> {
            summerizer.collect("1,2 3,4,5");
        }, "NumberFormatException was expected");
    }

    /*
     * Tests for correctness of the sumerized input
     */
    // empty input tests
    @Test
    public void EmptyInputGivesEmptySummary() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        // empty string
        Collection<Integer> input = summerizer.collect(empty_input);
        assertEquals("", summerizer.summarizeCollection(input));
        // spaces only input
        input = summerizer.collect(spaces_only_input);
        assertEquals("", summerizer.summarizeCollection(input));
        // commas only input
        input = summerizer.collect(commas_only_input);
        assertEquals("", summerizer.summarizeCollection(input));
        // comma spaces input
        input = summerizer.collect(comma_spaces_input);
        assertEquals("", summerizer.summarizeCollection(input));
    }

    // input with non seqeuntial numbers
    @Test
    public void NonSequentialNumbersInputTests() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        Collection<Integer> input = summerizer.collect(non_sequential);
        String summary = summerizer.summarizeCollection(input);
        // the range formatter should not be present on the summerizer
        assertFalse(summary.contains("-"));
        // all numbers should be present on the output
        String[] splitted_array = non_sequential.split(",");
        for (String elem : splitted_array) {
            assertTrue(summary.contains(elem));
        }
    }

    // input with seqeuntial numbers
    @Test
    public void SequentialNumbersInputTests() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        Collection<Integer> input = summerizer.collect(sample_input);
        String summary = summerizer.summarizeCollection(input);
        // the range formatter should be present on the summerizer
        assertTrue(summary.contains("-"));
        // should match the provided sample output
        assertEquals("1, 3, 6-8, 12-15, 21-24, 31", summary);
        // sequential input only gives the range only
        input = summerizer.collect("1,2,3,4,5,6,7,8,9,10,11");
        summary = summerizer.summarizeCollection(input);
        assertEquals("1-11", summary);
    }

    // number repetions
    @Test
    public void NumberRepeatedOnInputAreNotRepeated() {
        NumberRangeSummarizer summerizer = new SummerizerDefinition();
        Collection<Integer> input = summerizer.collect("1,1,1,1,1,1,3,3,3,3,3,3");
        String summary = summerizer.summarizeCollection(input);
        assertEquals("1, 3", summary);
        input = summerizer.collect("1,1,1,1,1,1,2,2,2,2,2");
        summary = summerizer.summarizeCollection(input);
        assertEquals("1-2", summary);
    }
}
