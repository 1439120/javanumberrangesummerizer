package numberrangesummarizer;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Werner
 *
 *         Implement this Interface to produce a comma delimited list of
 *         numbers,
 *         grouping the numbers into a range when they are sequential.
 *
 *
 *         Sample Input: "1,3,6,7,8,12,13,14,15,21,22,23,24,31
 *         Result: "1, 3, 6-8, 12-15, 21-24, 31"
 *
 *         The code will be evaluated on
 *         - functionality
 *         - style
 *         - robustness
 *         - best practices
 *         - unit tests
 */
public interface NumberRangeSummarizer {

    // collect the input
    Collection<Integer> collect(String input);

    // get the summarized string
    String summarizeCollection(Collection<Integer> input);

}

class SummerizerDefinition implements NumberRangeSummarizer {
    public Collection<Integer> collect(String input) {
        // remove commas and split
        String[] splitted_array = input.split(",");
        // this variable stores the returned output
        Collection<Integer> output = new ArrayList<Integer>();
        // convert string to integer
        for (String i : splitted_array) {
            try {
                output.add(Integer.parseInt(i.trim()));
            } catch (NumberFormatException e) {
                if (i.trim().length() > 0) {
                    System.out.println("Invalid input \"" + i + "\"");
                    throw new NumberFormatException();
                }
            }

        }
        return output;
    }

    public String summarizeCollection(Collection<Integer> input) {
        if (input.size() == 0)
            return "";
        // remove duplicates
        ArrayList<Integer> no_duplicates = new ArrayList<>(new HashSet<>(input));
        // sort
        Collections.sort(no_duplicates);
        // stores the output in list format
        ArrayList<String> output = new ArrayList<String>();
        // stores that starting point of the range
        Integer range_start = no_duplicates.get(0);
        output.add(range_start.toString());
        for (Integer i = 0; i < no_duplicates.size(); i++) {
            if (i == 0) {
                continue;
            } else if (no_duplicates.get(i) - no_duplicates.get(i - 1) == 1) { // checks if the elements are sequential
                // change the last element to the provided range
                output.set(output.size() - 1, range_start.toString() + "-" + no_duplicates.get(i).toString());
            } else {
                output.add(no_duplicates.get(i).toString());
                range_start = no_duplicates.get(i);
            }
        }

        return output.toString().replace("[", "").replace("]", "");
    }
}
