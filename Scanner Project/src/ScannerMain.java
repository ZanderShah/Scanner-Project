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
	
	public void readUnencrypted(BinaryTree<Student> tree, int[] fieldDefinitions, int skipLines) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("StudentInfoFile.csv")));
			for (int i = 0; i < skipLines; i++) {
				br.readLine();
			}

			while (br.ready()) {
				String line = br.readLine();
				
				String[] fields = line.split(",");
				String[] studentDefinition = new String[13];
				
				for (int field = 0; field < fields.length; field++) {
					if (fieldDefinitions[field] != -1) {
						studentDefinition[fieldDefinitions[field]] = fields[field];
					}
				}
				
				tree.add(new Student(studentDefinition));
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "The file \"data.csv\" could not be loaded", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}