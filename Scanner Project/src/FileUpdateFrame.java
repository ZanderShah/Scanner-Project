import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
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

public class FileUpdateFrame extends JFrame {
	private File selectedFile;
	private int newLineSkip, newFieldSpecification[];
	private ArrayList<JComboBox<String>> columns = new ArrayList<JComboBox<String>>();
	
	public FileUpdateFrame() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel columnSelection = new JPanel();
		columnSelection.setLayout(new BorderLayout());
		JPanel dropDown = new JPanel();
		dropDown.setLayout(new GridLayout());
		
		JPanel fileSelect = new JPanel();
		fileSelect.setLayout(new FlowLayout());
		JFileChooser jfc = new JFileChooser();
		JLabel currentFile = new JLabel("Current file: ");
		JButton select = new JButton("Choose file...");
		
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = jfc.showOpenDialog(FileUpdateFrame.this);
				if (status == JFileChooser.APPROVE_OPTION) {
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
//					dropDown.add(lineSkipSelection());

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
			
			private JComboBox<String> makeDropDown(int initial) {
				
				String[] options = { "Username", "First Name", "Last Name", "Grade", "Homeroom", "Password", "Email", "Address", 
						"Period 1", "Period 2", "Period 3", "Period 4", "Period 5", "Ignore"};
				newFieldSpecification = new int[options.length];
				JComboBox<String> fieldSelect = new JComboBox<String>(options);
				fieldSelect.setSelectedIndex(Math.min(options.length - 1, initial));
				fieldSelect.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						for (int j = 0; j < columns.size(); j++)
							if (e.getSource() == columns.get(j)) {
								String option = (String)(((JComboBox<String>)e.getSource()).getSelectedItem());
								for (int k = 0; k < options.length; k++)
									if (options[k].equals(option))
										newFieldSpecification[j] = k;
							}
					}
				});
				return fieldSelect;
			}
			
//			private int countRows(File selectedFile) {
//				try {
//					int rows = 0;
//					BufferedReader br = new BufferedReader(new FileReader(selectedFile));
//					
//					while (br.ready()) {
//						br.readLine();
//						rows++;
//					}
//					
//					br.close();
//					return rows;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return 0;
//			}
			
//			private JComboBox<Integer> lineSkipSelection() {
//				Integer[] options = new Integer[countRows(selectedFile) + 1];
//				for (int i = 0; i < options.length; i++)
//					options[i] = i;
//				JComboBox<Integer> lineSkip = new JComboBox<Integer>(options);
//				lineSkip.setSelectedIndex(0);
//				lineSkip.addActionListener(new ActionListener(){
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						JComboBox<Integer> info = (JComboBox)e.getSource();
//						int index = (int) info.getSelectedItem();
//						newLineSkip = index;
//					}
//				});
//				return lineSkip;
//			}
		});
		fileSelect.add(select);
		fileSelect.add(currentFile);
		
		JPanel options = new JPanel();
		options.setLayout(new FlowLayout());
		options.add(new JLabel("Skip lines: "));
		JTextField lineSkip = new JTextField();
		options.add(lineSkip);
		options.add(new JLabel("Delete source after import: "));
		JCheckBox delete = new JCheckBox();
		options.add(delete);
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		options.add(save);
		
		mainPanel.add(fileSelect);
		mainPanel.add(columnSelection);
		mainPanel.add(options);
		
		setContentPane(mainPanel);
		
		pack();
	}
}