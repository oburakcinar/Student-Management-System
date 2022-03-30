package com.burak.studentmanagement.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	
	public Role() {
		
	}
	
	public Role(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//overriding equals method in order to use contains method in CustomAuthenticationSuccessHadler class correctly 
	public boolean equals(Object comparedObject) {
	    
	    if (this == comparedObject) {
	        return true;
	    }

	   if (!(comparedObject instanceof Role)) {
	        return false;
	    }

	    Role comparedRole = (Role) comparedObject;

	    if (this.name.equals(comparedRole.name)) {
	        return true;
	    }

	    return false;
	}
}
