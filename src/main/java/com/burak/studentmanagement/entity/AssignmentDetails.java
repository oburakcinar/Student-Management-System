package com.burak.studentmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity(name="assignment_details")
public class AssignmentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="assignment_id")
	private int assignmentId;
	
	@Column(name="student_course_details_id")
	private int studentCourseDetailsId;
	
	@Column(name="is_done")
	private int isDone; 
	
	public AssignmentDetails() {
		
	}
	
	
	public AssignmentDetails(int id, int assignmentId, int studentCourseDetailsId, int isDone) {
		this.id = id;
		this.assignmentId = assignmentId;
		this.studentCourseDetailsId = studentCourseDetailsId;
		this.isDone = isDone;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getAssignmentId() {
		return assignmentId;
	}


	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}


	public int getStudentCourseDetailsId() {
		return studentCourseDetailsId;
	}


	public void setStudentCourseDetailsId(int studentCourseDetailsId) {
		this.studentCourseDetailsId = studentCourseDetailsId;
	}


	public int getIsDone() {
		return isDone;
	}


	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}
	
	
	
}



