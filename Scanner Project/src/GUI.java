

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class GUI extends JFrame {
	
	private BinaryTree<String, Student> students;
	
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	
	public GUI() {

		students = new BinaryTree<String, Student>();
		
		FormatSpecification fs = new FormatSpecification(new int[] {
				Fields.USERNAME, Fields.FIRST_NAME, Fields.LAST_NAME,
				Fields.IGNORE, Fields.GRADE, Fields.HOMEROOM,
				Fields.PASSWORD}, 1);
		
		JTabbedPane tabs = new JTabbedPane();
		
		// Add Update Panel
		JComponent updatePanel = createUpdatePanel();
		tabs.addTab("Update", updatePanel);
		
		// Add Search Panel
		JComponent searchPanel = createSearchPanel();
		tabs.addTab("Search", searchPanel);
		
		
		createAndShowGUI(tabs);
	}
	
	Color c = new Color ((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
	
	String username = "Empty";
	
	String query = "";
	
	//boolean infoOpen = false;
	
	
	// Create update layout
	private JComponent createUpdatePanel() {
		JPanel p = new JPanel(true);
		GroupLayout gl = new GroupLayout (p);
		p.setLayout(gl);
		p.setBackground(c);
		
		
		
		JButton quitButton = new JButton ("Quit");
		JButton updateButton = new JButton ("Update Database");
		JButton colorButton = new JButton ("Change Color");
		colorButton.setToolTipText("Changes the background colour");
		JTextField usernameField = new JTextField("Username");
		JLabel usernameLabel = new JLabel (username);
		
		
		
		quitButton.addActionListener(new quitListener());
		updateButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
//				username = usernameField.getText();
//				usernameLabel.setText(username);
				
				JFrame file = createFileImport();
				file.setVisible(true);
			}
			
		});
		colorButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				c = new Color ((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
				p.setBackground(c);
			}
			
		});
		
		// LAYOUT
		
		gl.setAutoCreateContainerGaps(false);
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
			.addGroup(gl.createSequentialGroup()
				.addComponent(usernameLabel)
			)
			.addGap(10)
			.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(usernameField)
				.addComponent(updateButton)
			)
			.addGap(5)
			.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(colorButton)
				.addComponent(quitButton)
			)
			.addGap(20)
				
		);

		
		gl.setVerticalGroup(gl.createParallelGroup()
			.addGap(0)
			.addGroup(gl.createSequentialGroup()
				.addGap(50)
				.addComponent(usernameLabel)
				.addGap(50)
				)
			.addGroup(gl.createSequentialGroup()
				.addGap(5)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(usernameField)
					.addComponent(colorButton)
				)
				.addGap(5)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(updateButton)
					.addComponent(quitButton)
				)
				.addGap(20)
			)
		);
		
		return p;
	}
	
	
	
	// Create search layout
	private JComponent createSearchPanel() {
		JPanel p = new JPanel(true);
		GroupLayout gl = new GroupLayout (p);
		p.setLayout(gl);
		//p.setBackground(new Color);
		
		// Init Components
		JTextField searchField = new JTextField();
		JButton searchButton = new JButton ("Search");
		//searchButton.setMnemonic(KeyEvent.VK_A);

		boolean selected = false;
		
		// Limit input to 10 characters
		searchField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	            if (!selected && (searchField.getText().length() >= 10 || !Character.isDigit(e.getKeyChar()))){
	                e.consume();
	            }
	            if (e.getKeyChar() == KeyEvent.VK_ENTER){
	            	searchButton.doClick();
	            }
	        }
		});
		
		searchField.addCaretListener(new textFieldCaretListener(selected));
		
		// Search Button 
		searchButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//SEARCH
				if (searchField.getText().length() == 9){
					query = "0" + searchField.getText();
				}
				else if (searchField.getText().length() == 10){
					query = searchField.getText();
				}
				else {
					System.out.println("invalid input");
					return;
				}
				
				System.out.println("Search: " + query);
				
				//searchField.setText("");
				
				Student result = students.getValue(query);
				
				// Open information window
				
				if (result != null) {
					JFrame infoFrame = createInfoFrame(result);
					infoFrame.addWindowListener(new infoWindowListener(searchField));
				} else {
					JOptionPane.showMessageDialog(GUI.this, "The student was not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
				
		});
		
		
		//LAYOUT
		
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		searchField.setFont(new Font("Consolas", Font.BOLD, 16));
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
			.addComponent(searchField, 200,200,200)
			.addComponent(searchButton)
		);

		
		gl.setVerticalGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(searchField)
			.addComponent(searchButton)
		);
		
		return p;
	}
	
	private void createAndShowGUI(JComponent tabs) {
		//Create and set up the window.
		JFrame frame = new JFrame("Student Database");
		
		frame.add(tabs, BorderLayout.CENTER);
		
		
		//Display the window.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(300, 200);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	// Create Student Information Frame
	private JFrame createInfoFrame(Student s) {
		
		JLabel nameLabel = new JLabel(s.getFirst() + " " + s.getLast());
		nameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
		JLabel studentNoLabel2 = new JLabel(s.getId());
		studentNoLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		JLabel passwordLabel2 = new JLabel(s.getPassword());
		passwordLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		
		JLabel studentNoLabel1 = new JLabel("Student No.");
		studentNoLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		JLabel passwordLabel1 = new JLabel("Password");
		passwordLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		
		
		JPanel p = new JPanel ();
		GroupLayout gl = new GroupLayout(p);
		p.setLayout(gl);
		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);
		gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(nameLabel)
			.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
					.addComponent(studentNoLabel1)
					.addComponent(studentNoLabel2)
				)
				.addGroup(gl.createSequentialGroup()
					.addComponent(passwordLabel1)
					.addComponent(passwordLabel2)
				)
			)
		);
		
		//NOT DONE
		gl.setVerticalGroup(gl.createSequentialGroup()
			.addComponent(nameLabel)
			.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(studentNoLabel1)
				.addComponent(studentNoLabel2)
			)
			.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(passwordLabel1)
				.addComponent(passwordLabel2)
			)
		);
		
		JFrame infoFrame = new JFrame("Student Information");
		infoFrame.setVisible(true);
		infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//infoFrame.setSize(400, 300);
		infoFrame.add(p);
		infoFrame.pack();

		

		
		return infoFrame;
	}
	
	private File selectedFile;
	
	public JFrame createFileImport() {
		JFrame fileFrame = new JFrame();
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel columnSelection = new JPanel();
		JPanel dropDown = new JPanel();
		columnSelection.add(dropDown);
		JTable preview = new JTable();
		columnSelection.add(preview);
		
		JPanel fileSelect = new JPanel();
		fileSelect.setLayout(new FlowLayout());
		JFileChooser jfc = new JFileChooser();
		JLabel currentFile = new JLabel("Current file: ");
		JButton select = new JButton("Choose file...");
		
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = jfc.showOpenDialog(GUI.this);
				if (status == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();
					currentFile.setText("Current file: " + selectedFile.getPath());
					dropDown.removeAll();
					int numCols = countColumns(selectedFile);
					for (int i = 0; i < numCols; i++) {
						dropDown.add(makeDropDown());
					}
					
					
					
					fileFrame.pack();
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
					return cols;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			private JComboBox<String> makeDropDown() {
				JComboBox<String> fieldSelect = new JComboBox<String>();
				fieldSelect.addItem("Username");
				fieldSelect.addItem("First Name");
				fieldSelect.addItem("Last Name");
				fieldSelect.addItem("Grade");
				fieldSelect.addItem("Homeroom");
				fieldSelect.addItem("Password");
				fieldSelect.addItem("Email");
				fieldSelect.addItem("Address");
				fieldSelect.addItem("Period 1");
				fieldSelect.addItem("Period 2");
				fieldSelect.addItem("Period 3");
				fieldSelect.addItem("Period 4");
				fieldSelect.addItem("Period 5");
				fieldSelect.addItem("Ignore");
				return fieldSelect;
			}
		});
		fileSelect.add(select);
		fileSelect.add(currentFile);
		
		JPanel options = new JPanel();
		
		mainPanel.add(fileSelect);
		mainPanel.add(columnSelection);
		mainPanel.add(options);
		
		fileFrame.setContentPane(mainPanel);
		
		fileFrame.pack();
		
		return fileFrame;
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI a = new GUI();
			}
		});
	}
	
	class quitListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}

	class infoWindowListener implements WindowListener{
		
		JTextField field;
		
		infoWindowListener(JTextField field){
			this.field = field;
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			field.setText("");
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
	}
	
	class textFieldCaretListener implements CaretListener{
		boolean selected;
		textFieldCaretListener(boolean selected){
			this.selected = selected;
		}
		
		@Override
		public void caretUpdate(CaretEvent e) {
			if (e.getDot() == e.getMark()){
				selected = false;
			}
			else{
				selected = true;
			}
		}
		
	}
	
	
}
