package com.burak.studentmanagement.dao;

import com.burak.studentmanagement.entity.Role;

public interface RoleDao {
	
	public Role findRoleByName(String theRoleName);
}
