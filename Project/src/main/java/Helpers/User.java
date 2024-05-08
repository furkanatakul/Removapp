package Helpers;

public class User {

	private int id;
	String name_surname, username, password, gender;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName_surname() {
		return name_surname;
	}
	public void setName_surname(String name_surname) {
		this.name_surname = name_surname;
	}
	public User(int id, String name_surname, String username, String password, String gender) {
		this.id = id;
		this.name_surname = name_surname;
		this.username = username;
		this.password = password;
		this.gender = gender;
	}
	public User() {}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}