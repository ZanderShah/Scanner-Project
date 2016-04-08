import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ScannerMain extends JFrame {

	public static void main(String[] args) {
		ScannerMain s = new ScannerMain();
		s.readUnencrypted(new BinaryTree<Student>(), new int[] {0, 1, 2, -1, 3, 4, 5}, 1);
	}
	
	/**
	 * Reads an unencrypted CSV file of student data according to the supplied format of the file
	 * @param tree The tree to store student data in
	 * @param fieldDefinitions The definitions of what each column in the file refers to
	 * @param skipLines The number of lines to skip at the beginning of the file
	 */
	public void readUnencrypted(BinaryTree<Student> tree, int[] fieldDefinitions, int skipLines) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("StudentInfoFile.csv")));
			for (int i = 0; i < skipLines; i++) {
				br.readLine();
			}

			// Read and parse all student records
			while (br.ready()) {
				String line = br.readLine();
				
				String[] fields = line.split(",");
				String[] studentDefinition = new String[13];
				
				// Determine what each column is based off the definitions
				for (int field = 0; field < fields.length; field++) {
					if (fieldDefinitions[field] != -1) {
						
						// Sanitize input strings
						fields[field] = fields[field].trim();
						fields[field] = fields[field].replaceAll("[=\\\"]", "");
						
						studentDefinition[fieldDefinitions[field]] = fields[field];
					}
				}
				
				tree.add(new Student(studentDefinition));
			}
			System.out.println();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "The file student info file could not be loaded", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}