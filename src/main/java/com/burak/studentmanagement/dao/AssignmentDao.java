package com.burak.studentmanagement.dao;

import com.burak.studentmanagement.entity.Assignment;

public interface AssignmentDao {
	
	public void save(Assignment assignment);
	
	public void deleteAssignmentById(int id);
}
