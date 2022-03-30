package com.burak.studentmanagement.service;

import java.util.List;

import com.burak.studentmanagement.entity.StudentCourseDetails;

public interface StudentCourseDetailsService {
	
	public List<StudentCourseDetails> findByStudentId(int id);
	
	public StudentCourseDetails findByStudentAndCourseId(int studentId, int courseId);
	
	public void deleteByStudentId(int id);
	
	public void deleteByStudentAndCourseId(int studentId, int courseId);
	
	public void save(StudentCourseDetails studentCourseDetails);
}
