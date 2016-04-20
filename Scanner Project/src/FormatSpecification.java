/**
 * Defines how a CSV file should be interpreted
 * fieldDefinitions defines what each column of the CSV file should be interpreted as
 * skipLines defines how many lines at the beginning of the file should be ignored (such as titles)
 * 
 * @author Callum Moseley
 * @version April 2016
 *
 */
public class FormatSpecification {
	private int[] fieldDefinitions;
	private int skipLines;
	
	/**
	 * Initialises the format specification
	 * @param fd The field definitions
	 * @param skip The number of lines to skip
	 */
	public FormatSpecification(int[] fd, int skip) {
		fieldDefinitions = fd;
		skipLines = skip;
	}
	
	/**
	 * Gets the field definitions
	 * 
	 * @return The field definitions
	 */
	public int[] getDefinitions() {
		return fieldDefinitions;
	}
	
	/**
	 * Gets the number of lines skipped
	 * 
	 * @return The number of lines to skip 
	 */
	public int getLines() { 
		return skipLines;
	}
}
