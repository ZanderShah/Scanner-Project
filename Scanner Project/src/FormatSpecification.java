public class FormatSpecification {
	private int[] fieldDefinitions;
	private int skipLines;
	
	public FormatSpecification(int[] fd, int skip) {
		fieldDefinitions = fd;
		skipLines = skip;
	}
	
	public int[] getDefinitions() {
		return fieldDefinitions;
	}
	
	public int getLines() { 
		return skipLines;
	}
}
