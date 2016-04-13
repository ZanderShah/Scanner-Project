

import javax.swing.*;
import javax.swing.GroupLayout.SequentialGroup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI extends JFrame implements ActionListener{
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	
	public GUI() {

		
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
		JButton saveButton = new JButton ("Save Username");
		JButton colorButton = new JButton ("Change Color");
		colorButton.setToolTipText("Changes the background colour");
		JTextField usernameField = new JTextField("Username");
		JLabel usernameLabel = new JLabel (username);
		
		
		
		quitButton.addActionListener(new quitListener());
		saveButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				username = usernameField.getText();
				usernameLabel.setText(username);
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
				.addComponent(saveButton)
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
					.addComponent(saveButton)
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

		
		
		// Limit input to 10 characters
		searchField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	            if (searchField.getText().length() >= 10 || !Character.isDigit(e.getKeyChar())){
	                e.consume();
	            }
	            if (e.getKeyChar() == KeyEvent.VK_ENTER){
	            	searchButton.doClick();
	            }
	        }
		});
		
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
				
				// Open information window
				String [] i = {"0123456789", "Abcde", "Defghijkl", "11", "Homeroom", "4s93je2", "abc@abc.com", "1 casdf", "1", "2", "3", "4", "5"};
				Student s = new Student (i);
				JFrame infoFrame = createInfoFrame(s);
				infoFrame.addWindowListener(new infoWindowListener(searchField));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


	class infoWindowListener implements WindowListener{
		
		JTextField field;
		
		infoWindowListener(JTextField field){
			this.field = field;
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			field.setText("");
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	
}