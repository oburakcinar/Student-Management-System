package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.Course;

public interface CourseDao {
	
	public void saveCourse(Course course);
	
	public List<Course> findAllCourses();
	
	public Course findCourseById(int id);
	
	public void deleteCourseById(int id);
}
