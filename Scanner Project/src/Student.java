public class Student implements Comparable<Student>
{
	private String id, firstName, lastName, grade, homeroom, password, email,
			address, p1, p2, p3, p4, p5;

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

	public String getId()
	{
		return id;
	}

	public String getFirst()
	{
		return firstName;
	}

	public String getLast()
	{
		return lastName;
	}

	public String getGrade()
	{
		return grade;
	}

	public String getHomeroom()
	{
		return homeroom;
	}

	public String getPassword()
	{
		return password;
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
