public class Student implements Comparable<Student>
{
	private String id, firstName, lastName, grade, homeroom, password, email,
			address, p1, p2, p3, p4, p5;

	/**
	 * Constructs a student object from information read in from the csv file
	 * 
	 * @param data information for the student (null if the file does not 
	 * contain the piece of information)
	 */
	Student(String[] data)
	{
		id = data[Fields.USERNAME];
		firstName = data[Fields.FIRST_NAME];
		lastName = data[Fields.LAST_NAME];
		grade = data[Fields.GRADE];
		homeroom = data[Fields.HOMEROOM];
		password = data[Fields.PASSWORD];
		email = data[Fields.EMAIL];
		address = data[Fields.ADDRESS];
		p1 = data[Fields.PERIOD_1];
		p2 = data[Fields.PERIOD_2];
		p3 = data[Fields.PERIOD_3];
		p4 = data[Fields.PERIOD_4];
		p5 = data[Fields.PERIOD_5];
	}

	/**
	 * Gets the student's id
	 * 
	 * @return the student's id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Gets the student's first name
	 * 
	 * @return the student's first name
	 */
	public String getFirst()
	{
		return firstName;
	}

	/**
	 * Gets the student's last name
	 * 
	 * @return the student's last name
	 */
	public String getLast()
	{
		return lastName;
	}

	/**
	 * Gets the student's grade
	 * 
	 * @return the student's grade
	 */
	public String getGrade()
	{
		return grade;
	}

	/**
	 * Gets the student's homeroom
	 * 
	 * @return the student's homeroom
	 */
	public String getHomeroom()
	{
		return homeroom;
	}

	/**
	 * Gets the student's password
	 * 
	 * @return the student's password
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Gets the student's email
	 * 
	 * @return the student's email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * Gets the student's address
	 * 
	 * @return the student's address
	 */
	public String getAddress()
	{
		return address;
	}

	public int compareTo(Student student)
	{
		return id.compareTo(student.getId());
	}

	public boolean equals(Student student)
	{
		return id.compareTo(student.getId()) == 0;
	}

	public String toString()
	{
		return id;
	}
}
