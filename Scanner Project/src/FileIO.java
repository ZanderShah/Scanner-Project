import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

/**
 * Provides utility methods for interacting with files
 * 
 * @author Callum Moseley
 * @version April 2016
 *
 */
public class FileIO {
	
	private static Key k; // key used for encrypting stuff
	
	/**
	 * Sets the static key to use for all encryption
	 * 
	 * @param key the encryption key to use
	 */
	public static void setKey(Key key) {
		k = key;
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
			for (int i = 0; i < fs.getLines() && br.ready(); i++) {
				br.readLine();
			}

			// Read and parse all student records
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				String[] studentDefinition = new String[13];

				// Determine what each column is based off the definitions
				for (int field = 0; field < fields.length; field++) {
					if (fs.getDefinitions()[field] != Fields.IGNORE) {

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
		} catch (Exception e) {
			return -1;
		}
		return read;
	}
	
	/**
	 * Reads an encrypted file
	 * 
	 * @param f The encrypted file to be read
	 * @return The InputStream to read the file, or null if it could not be read
	 */
	public static InputStream readEncrypted(File f) {
		try {
			// Attempt to open and return a cipher input stream
			Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, k);
			CipherInputStream cis = new CipherInputStream(new FileInputStream(f), c);
			return cis;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Encrypts the given file
	 * 
	 * @param in The input file to be encrypted
	 * @param out The file to write the newly encrypted data to
	 * @return Whether the file was successfully written
	 */
	public static boolean encryptFile(File in, File out) {
		try {
			// Open all file streams
			FileInputStream fis = new FileInputStream(in);
			Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, k);
			CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(out), c);
			
			// Copy data
			int read = 0;
			while ((read = fis.read()) != -1) {
				cos.write(read);
			}
			
			fis.close();
			cos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Saves the format specification to a file. The format of the config file is
	 * number of columns
	 * definition of each column
	 * number of lines to skip
	 * 
	 * @param f The file to write the format specification
	 * @param fs The format specification to save to a file
	 * @return Whether the file was successfully saved
	 */
	public static boolean saveFormatSpecification(File f, FormatSpecification fs) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
			
			pw.println(fs.getDefinitions().length);
			for (int i = 0; i < fs.getDefinitions().length; i++) {
				pw.println(fs.getDefinitions()[i]);
			}
			pw.print(fs.getLines());
			pw.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Reads and returns a format specification from a file
	 * 
	 * @param f The file containing the format specification
	 * @return The read format specification, or null if there was a problem reading it
	 */
	public static FormatSpecification readFormatSpecification(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			int columns = Integer.parseInt(br.readLine());
			int[] fields = new int[columns];
			for (int i = 0; i < columns; i++) {
				fields[i] = Integer.parseInt(br.readLine());
			}
			int skipLines = Integer.parseInt(br.readLine());
			
			br.close();
			return new FormatSpecification(fields, skipLines);
		} catch (IOException e) {
			return null;
		}
	}
}