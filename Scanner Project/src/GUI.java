import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * GUI
 * @author Tony Wu
 * @version 0.1
 */

public class GUI extends JFrame {

	private BinaryTree<String, Student> students;

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */

	Color accentGreen = new Color(124, 218, 52);
	Color backgroundGrey = new Color(80, 80, 80);
	Color foregroundGrey = new Color(140, 140, 140);
	Color foregroundLightGrey = new Color(180, 180, 180);
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenWidth = (int) screenSize.getWidth();
	int screenHeight = (int) screenSize.getHeight();

	public GUI() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keygen.init(new SecureRandom(new byte[] { 1, 2, 3, 4, 5 })); // 100% random and secure key

		FileIO.setKey(keygen.generateKey());
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.setBackground(foregroundGrey);
		tabs.setForeground(accentGreen);
		UIManager.put("TabbedPane.selected", backgroundGrey);
		UIManager.put("TabbedPane.contentAreaColor", Color.BLACK);
		UIManager.put("TabbedPane.background", Color.RED);
		UIManager.put("TabbedPane.darkShadow", backgroundGrey);
		UIManager.put("TabbedPane.focus", accentGreen);
		UIManager.put("TabbedPane.selectHighlight", Color.BLACK);
		UIManager.put("TabbedPane.borderHightlightColor", foregroundGrey);
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));

		SwingUtilities.updateComponentTreeUI(tabs);

		// Add Search Panel
		createAndShowGUI(createSearchPanel());
		
		int response = JOptionPane.showConfirmDialog(this, "Would you like to load a new database?", "Database Update", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			JFrame file = new FileUpdateFrame(GUI.this);
			file.setVisible(true);
		} else {
			loadDatabase();
		}
	}

	public void loadDatabase() {
		students = new BinaryTree<String, Student>();

		int read = FileIO.readCSV(students, FileIO.readFormatSpecification(new File("config")), FileIO.readEncrypted(new File("data")));
		if (read == -1) {
			int response = JOptionPane.showConfirmDialog(this, "No database file was found. Would you like to load a database?", "Database Update", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				JFrame file = new FileUpdateFrame(GUI.this);
				file.setVisible(true);
			}
		}
	}

	Color c = new Color((int) (Math.random() * 255),
			(int) (Math.random() * 255), (int) (Math.random() * 255));
	String query = "";
	
	JTextField searchField = new JTextField();

	
	
	// Create search layout
	private JPanel createSearchPanel() {
		JPanel p = new JPanel(true);
		GroupLayout gl = new GroupLayout(p);
		p.setLayout(gl);
		p.setBackground(backgroundGrey);
		
		
		// Init Components

		JButton searchButton = new JButton("Search:");
		searchButton.setMnemonic(KeyEvent.VK_ENTER);
		JLabel title = new JLabel("Student Password Lookup", SwingConstants.CENTER);
		

		boolean[] selected = new boolean[1];
		selected[0] = false;

		// Detect selection to allow overwrite
		searchField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				
				if (e.getDot() == e.getMark()) {
					selected[0] = false;
				} else {
					selected[0] = true;
				}
			}
			
		});

		// Limit input to 10 characters
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) || !selected[0]
						&& (searchField.getText().length() >= 10)) {
					e.consume();
				}
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					searchButton.doClick();
				}
				if (searchField.getText().length() > 10) {
					searchField.setText(searchField.getText().substring(0, 10));
				}
			}
		});

		// Search Button
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Check input
				if (searchField.getText().length() == 9
						|| searchField.getText().length() == 10) {
					if (searchField.getText().length() == 9) {
						query = "0" + searchField.getText();
					} else if (searchField.getText().length() == 10) {
						query = searchField.getText();
					}

					System.out.println("Search: " + query);

					if (students == null) {
						JOptionPane.showMessageDialog(GUI.this, "No student database is loaded", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					// Search database
					Student result = students.getValue(query);

					// Open information window
					if (result != null) {
						JFrame infoFrame = createInfoFrame(result);
						infoFrame.addWindowListener(new infoWindowListener(
								searchField));
						
						
						// Auto close after time
						new Thread(){
					          @Override
					          public void run() {
					               try {
					                      Thread.sleep(10000); // time after which pop up will be disappeared.
					                      infoFrame.dispose();
					               } catch (InterruptedException e) {
					                      e.printStackTrace();
					               }
					          };
					    }.start();
					} else {
						// Show error message for 2 seconds
						JOptionPane err = new JOptionPane("The student was not found in the database.", JOptionPane.ERROR_MESSAGE);
						JDialog d = err.createDialog(GUI.this, "Error");
						new Thread() {
							@Override
							public void run() {
								try {
									Thread.sleep(2000);
									d.dispose();
									searchField.setText("");
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
						d.setVisible(true);
					}
				} else {
					// Show error message for 2 seconds
					JOptionPane err = new JOptionPane("Student number must be 9 or 10 digits in length.", JOptionPane.ERROR_MESSAGE);
					JDialog d = err.createDialog(GUI.this, "Error");
					new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(2000);
								d.dispose();
								searchField.setText("");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
					d.setVisible(true);
				}
			}

		});

		// LAYOUT
		// SET component appearance
		gl.setAutoCreateContainerGaps(false);
		gl.setAutoCreateGaps(false);
		searchField.setFont(new Font("Consolas", Font.BOLD, 36));
		searchButton.setFont(new Font("Rockwell", Font.PLAIN, 40));
		title.setFont(new Font("Rockwell", Font.PLAIN, 40));
		// searchField.setOpaque(false);
		searchField.setBorder(BorderFactory.createEmptyBorder());
		searchButton.setBorder(BorderFactory.createEmptyBorder());
		searchButton.setBackground(foregroundGrey);
		searchButton.setForeground(Color.WHITE);
		title.setForeground(Color.WHITE);
		UIManager.put("Button.select", accentGreen);
		UIManager.put("Button.highlight", backgroundGrey);
		UIManager.put("TextField.caretForeground", Color.BLACK);
		UIManager.put("TextField.inactiveBackground", foregroundGrey);

		// ADD compoment properties
		SwingUtilities.updateComponentTreeUI(searchButton);
		SwingUtilities.updateComponentTreeUI(searchField);
		
		System.out.println(title.getPreferredSize().getWidth());

		// ARRANGE components
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGap((screenWidth - (int)title.getPreferredSize().getWidth())/2)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(title)
						.addGroup(gl.createSequentialGroup()
							.addComponent(searchButton, 180, 180, 180)
							.addGap(15)
							.addComponent(searchField, 201, 201, 201)
						)
				)
		);

		gl.setVerticalGroup(gl
				.createSequentialGroup()
				.addGap(20)
				.addComponent(title)
				.addGap(200)
				.addGroup(
						gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(searchField)
								.addComponent(searchButton, 80, 80, 80)));
		
		searchField.requestFocus();

		return p;
	}

	private void createAndShowGUI(JPanel search) {
		// Create and set up the window.
		this.setTitle("Student Database");
		this.setUndecorated(true);

		/*JLabel title = new JLabel("Student Password Lookup", SwingConstants.CENTER);
		title.setFont(new Font("Rockwell", Font.PLAIN, 36));
		title.setBackground(backgroundGrey);
		title.setForeground(Color.BLACK);
		title.setOpaque(true);
		this.add(title, BorderLayout.NORTH);*/
		this.add(search, BorderLayout.CENTER);

		// Display the window.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(screenWidth, screenHeight);
		searchField.grabFocus();
		this.setVisible(true);
		
		this.addWindowListener( new WindowAdapter () {
			public void windowActivated( WindowEvent e ){
		    	searchField.requestFocusInWindow();
			}
		}); 
	}
	
	

	// Create Student Information Frame
	private JFrame createInfoFrame(Student s) {
		
		// Data labels
		JLabel nameLabel = new JLabel(s.getFirst() + " " + s.getLast());
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Calibri", Font.BOLD, 20));
		JLabel studentNoLabel2 = new JLabel(s.getId());
		studentNoLabel2.setForeground(Color.WHITE);
		studentNoLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		JLabel passwordLabel2 = new JLabel(s.getPassword());
		passwordLabel2.setForeground(Color.WHITE);
		passwordLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		JLabel gradeLabel2 = new JLabel(s.getGrade());
		gradeLabel2.setForeground(Color.WHITE);
		gradeLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		JLabel homeroomLabel2 = new JLabel(s.getHomeroom());
		homeroomLabel2.setForeground(Color.WHITE);
		homeroomLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		JLabel addressLabel2 = new JLabel(s.getAddress());
		addressLabel2.setForeground(Color.WHITE);
		addressLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));
		JLabel emailLabel2 = new JLabel(s.getEmail());
		emailLabel2.setForeground(Color.WHITE);
		emailLabel2.setFont(new Font("Calibri", Font.PLAIN, 16));

		// Regular labels
		JLabel studentNoLabel1 = new JLabel("Student No.");
		studentNoLabel1.setForeground(foregroundLightGrey);
		studentNoLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		JLabel passwordLabel1 = new JLabel("Password");
		passwordLabel1.setForeground(foregroundLightGrey);
		passwordLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		JLabel gradeLabel1 = new JLabel("Grade");
		gradeLabel1.setForeground(foregroundLightGrey);
		gradeLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		JLabel homeroomLabel1 = new JLabel("Homeroom");
		homeroomLabel1.setForeground(foregroundLightGrey);
		homeroomLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		JLabel addressLabel1 = new JLabel("Address");
		addressLabel1.setForeground(foregroundLightGrey);
		addressLabel1.setFont(new Font("Calibri", Font.BOLD, 16));
		JLabel emailLabel1 = new JLabel("Email");
		emailLabel1.setForeground(foregroundLightGrey);
		emailLabel1.setFont(new Font("Calibri", Font.BOLD, 16));

		JPanel p = new JPanel();
		p.setBackground(backgroundGrey);
		GroupLayout gl = new GroupLayout(p);
		p.setLayout(gl);
		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);
		gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(nameLabel)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(studentNoLabel1)
								.addComponent(passwordLabel1)
								.addComponent(gradeLabel1)
								.addComponent(homeroomLabel1)
								.addComponent(addressLabel1)
								.addComponent(emailLabel1))
						.addGap(50)
						.addGroup(gl.createParallelGroup(
								GroupLayout.Alignment.TRAILING)
								.addComponent(studentNoLabel2)
								.addComponent(passwordLabel2)
								.addComponent(gradeLabel2)
								.addComponent(homeroomLabel2)
								.addComponent(addressLabel2)
								.addComponent(emailLabel2))));

		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(nameLabel)
				.addGap(10)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(studentNoLabel1)
						.addComponent(studentNoLabel2))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(passwordLabel1)
						.addComponent(passwordLabel2))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(gradeLabel1)
						.addComponent(gradeLabel2))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(homeroomLabel1)
						.addComponent(homeroomLabel2))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(addressLabel1)
						.addComponent(addressLabel2))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(emailLabel1)
						.addComponent(emailLabel2)));

		ImageIcon infoIcon = new ImageIcon("open-envelopesmall2.png");
		JFrame infoFrame = new JFrame("Student Information");
		infoFrame.setIconImage(infoIcon.getImage());
		
		infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		infoFrame.add(p);
		infoFrame.pack();
		infoFrame.setLocation((screenWidth-infoFrame.getWidth())/2, (screenHeight-infoFrame.getHeight())/2);
		infoFrame.setVisible(true);

		return infoFrame;
	}
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		GUI g = new GUI();
		g.setVisible(true);
	}

	class quitListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}

	class infoWindowListener implements WindowListener {

		JTextField field;

		infoWindowListener(JTextField field) {
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

}
