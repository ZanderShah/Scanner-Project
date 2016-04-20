import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * A JFrame for loading a CSV file as the database, as well as specifying it's format
 * 
 * @author Callum Moseley
 * @version April 2016
 */
public class FileUpdateFrame extends JFrame {
	private File selectedFile;
	private ArrayList<JComboBox<String>> columns = new ArrayList<JComboBox<String>>();
	
	public FileUpdateFrame(GUI gui) {
		this.setAlwaysOnTop(true);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		// File selection
		JPanel fileSelect = new JPanel();
		fileSelect.setLayout(new FlowLayout());
		JFileChooser jfc = new JFileChooser();
		JLabel currentFile = new JLabel("Current file: ");
		JButton select = new JButton("Choose file...");
		
		// Field definitions and data preview
		JPanel columnSelection = new JPanel();
		columnSelection.setLayout(new BorderLayout());
		JPanel dropDown = new JPanel();
		dropDown.setLayout(new GridLayout());
		
		// Handler for selecting a file
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = jfc.showOpenDialog(FileUpdateFrame.this);
				if (status == JFileChooser.APPROVE_OPTION) {
					
					// Update drop down field choosers
					selectedFile = jfc.getSelectedFile();
					currentFile.setText("Current file: " + selectedFile.getPath());
					columnSelection.removeAll();
					dropDown.removeAll();
					int numCols = countColumns(selectedFile);
					for (int i = 0; i < numCols; i++) {
						columns.add(makeDropDown(i));
						dropDown.add(columns.get(columns.size() - 1));
					}
					columnSelection.add(dropDown, BorderLayout.NORTH);

					// Update the table showing preview data
					String[][] previewData = new String[4][numCols];
					try {
						BufferedReader br = new BufferedReader(new FileReader(selectedFile));
						for (int row = 0; br.ready() && row < 4; row++) {
							String[] line = br.readLine().split(",");
							for (int col = 0; col < line.length; col++) {
								previewData[row][col] = line[col];
							}
						}
						br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(FileUpdateFrame.this, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					String[] titles = new String[numCols];
					for (int i = 0; i < numCols; i++) {
						titles[i] = "Column " + (i + 1);
					}
					
					JTable preview = new JTable(previewData, titles);
					columnSelection.add(preview, BorderLayout.CENTER);
					
					pack();
				}
			}
			
			/**
			 * Utility method to count max columns in a CSV file
			 * 
			 * @param selectedFile The file to count columns of
			 * @return When interpreted as a CSV, the maximum number of columns in a record
			 */
			private int countColumns(File selectedFile) {
				try {
					int cols = 0;
					BufferedReader br = new BufferedReader(new FileReader(selectedFile));
					
					while (br.ready()) {
						String line = br.readLine();
						cols = Math.max(cols, line.split(",").length);
					}
					
					br.close();
					return cols;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			/**
			 * Utility to create a drop down menu for choosing field type
			 * 
			 * @param initialValue The value to start the drop down at
			 * @return The drop down menu object created
			 */
			private JComboBox<String> makeDropDown(int initialValue) {
				
				String[] options = { "Username", "First Name", "Last Name", "Grade", "Homeroom", "Password", "Email", "Address", 
						"Period 1", "Period 2", "Period 3", "Period 4", "Period 5", "Ignore"};
				JComboBox<String> fieldSelect = new JComboBox<String>(options);
				fieldSelect.setSelectedIndex(Math.min(options.length - 1, initialValue));
				return fieldSelect;
			}
		});
		fileSelect.add(select);
		fileSelect.add(currentFile);
		
		// Options for importing the file
		JPanel options = new JPanel();
		options.setLayout(new GridLayout());
		
		JPanel lineSkipPanel = new JPanel();
		lineSkipPanel.setLayout(new GridLayout());
		lineSkipPanel.add(new JLabel("Skip lines: "));
		JTextField lineSkip = new JTextField("0");
		lineSkipPanel.add(lineSkip);
		options.add(lineSkipPanel);
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.add(new JLabel("Delete source after import: "));
		JCheckBox delete = new JCheckBox();
		checkBoxPanel.add(delete);
		options.add(checkBoxPanel);
		
		// Handles when the file is saved and imported
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// Build the format specification
				int[] fields = new int[columns.size()];
				for (int i = 0; i < columns.size(); i++) {
					fields[i] = columns.get(i).getSelectedIndex();
				}
				
				// If the skip lines is invalid, abort 
				int lines = 0;
				try {
					lines = Integer.parseInt(lineSkip.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(FileUpdateFrame.this, "Invalid number of skipped lines.");
					return;
				}
				FormatSpecification fs = new FormatSpecification(fields, lines);
				
				// If no file is selected, abort saving
				if (selectedFile == null) {
					JOptionPane.showMessageDialog(FileUpdateFrame.this, "No file was selected", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Attempt to save the format specification and data files
				if (!FileIO.saveFormatSpecification(new File("config"), fs) ||
						!FileIO.encryptFile(selectedFile, new File("data"))) {
					JOptionPane.showMessageDialog(FileUpdateFrame.this, "There was an error saving the file", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// If requested, attempt to delete the source
				if (delete.isSelected()) {
					try {
						Files.delete(selectedFile.toPath());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(FileUpdateFrame.this, "The source file could not be deleted", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
				
				// Reload the files in the main program
				gui.loadDatabase();
				
				dispose();
			}
		});
		options.add(save);
		
		mainPanel.add(fileSelect, BorderLayout.NORTH);
		mainPanel.add(columnSelection, BorderLayout.CENTER);
		mainPanel.add(options, BorderLayout.SOUTH);
		
		setContentPane(mainPanel);
		
		pack();
	}
}