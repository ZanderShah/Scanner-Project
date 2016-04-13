import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class FileIO {

	public static void main(String[] args) throws Exception {
		
		FormatSpecification fs = new FormatSpecification(new int[] {
				Fields.USERNAME, Fields.FIRST_NAME, Fields.LAST_NAME,
				Fields.GRADE, Fields.HOMEROOM, Fields.PASSWORD, Fields.EMAIL,
				Fields.ADDRESS, Fields.PERIOD_1, Fields.PERIOD_2,
				Fields.PERIOD_3, Fields.PERIOD_4, Fields.PERIOD_5 }, 0);
		
		KeyGenerator keygen = KeyGenerator.getInstance("DES");
		keygen.init(56, new SecureRandom(new byte[] {1}));
		SecretKey key = keygen.generateKey();
		encryptFile(new File("FullStudentInfo.csv"), new File("encryptedTest"), key);
		readCSV(new BinaryTree<String, Student>(), fs,
			readEncrypted(new File("encryptedTest"), key));
	}

	/**
	 * Reads an unencrypted CSV file of student data according to the supplied
	 * format of the file
	 * 
	 * @param tree The tree to store student data in
	 * @param fieldDefinitions The definitions of what each column in the file refers to
	 * @param skipLines The number of lines to skip at the beginning of the file
	 * 
	 * @returns the number of students read, or -1 if there was an error
	 */
	public static int readCSV(BinaryTree<String, Student> tree,
			FormatSpecification fs, InputStream is) {
		BufferedReader br;

		int read = 0;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			for (int i = 0; i < fs.getLines(); i++) {
				br.readLine();
			}

			// Read and parse all student records
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				String[] studentDefinition = new String[13];

				// Determine what each column is based off the definitions
				for (int field = 0; field < fields.length; field++) {
					if (fs.getDefinitions()[field] != -1) {

						// Sanitize input strings
						fields[field] = fields[field].trim();
						fields[field] = fields[field].replaceAll("[=\\\"]", "");

						studentDefinition[fs.getDefinitions()[field]] = fields[field];
					}
				}

				if (studentDefinition[Fields.USERNAME].length() == 9) {
					studentDefinition[Fields.USERNAME] = "0" + studentDefinition[Fields.USERNAME];
				}
				tree.add(studentDefinition[0], new Student(studentDefinition));
				read++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return read;
	}
	
	public static InputStream readEncrypted(File f, Key k) throws Exception {
		Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, k);
		CipherInputStream cis = new CipherInputStream(new FileInputStream(f), c);
		return cis;
	}
	
	public static void encryptFile(File in, File out, Key k) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, k);
		CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(out), c);
		
		int read = 0;
		while ((read = fis.read()) != -1) {
			cos.write(read);
		}
		
		fis.close();
		cos.close();
	}
}