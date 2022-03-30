package com.burak.studentmanagement.service;

import java.util.List;

import com.burak.studentmanagement.entity.Course;

public interface CourseService {
	
	public void save(Course course);
	
	public List<Course> findAllCourses();
	
	public Course findCourseById(int id);
	
	public void deleteCourseById(int id);
}
