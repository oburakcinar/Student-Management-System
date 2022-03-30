package com.burak.studentmanagement.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.burak.studentmanagement.entity.Role;


public class UserDto {
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String userName;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String password;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String firstName;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String lastName;
	
	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	private String email;
	
	private Role role;
	
	public UserDto() {
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	
	
}
