package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.Teacher;

public interface TeacherDao {
	
	public Teacher findByTeacherName(String theTeacherName);
	
	public Teacher findByTeacherId(int id);
	
	public void save(Teacher teacher);
	
	public List<Teacher> findAllTeachers();
	
	public void deleteTeacherById(int id);
	
}
