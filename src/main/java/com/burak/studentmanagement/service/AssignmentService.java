package com.burak.studentmanagement.service;

import com.burak.studentmanagement.entity.Assignment;

public interface AssignmentService {
	
	public void save(Assignment assignment);
	
	public void deleteAssignmentById(int id);
}
