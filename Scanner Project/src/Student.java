
public class Student implements Comparable<Student>
{
	private String id, firstName, lastName, homeroom, password;
	private int grade;
	
	Student(String id, String firstName, String lastName, int grade, String homeroom, String password)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.grade = grade;
		this.homeroom = homeroom;
		this.password = homeroom;
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
	
	public int getGrade()
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
