package com.burak.studentmanagement.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.user.UserDto;

public interface StudentService extends UserDetailsService {
	
	public Student findByStudentName(String userName);

	public void save(UserDto userDto);
	
	public void save(Student student);
	
	public Student findByStudentId(int id);
	
	public List<Student> findAllStudents();
	
	public void deleteById(int id);
}
