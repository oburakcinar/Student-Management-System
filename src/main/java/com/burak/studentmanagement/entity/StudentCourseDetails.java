package com.burak.studentmanagement.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Table
@Entity(name="student_course_details")
public class StudentCourseDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="student_id")
	private int studentId;
	
	@Column(name="course_id")
	private int courseId;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="assignment_details",
				joinColumns = @JoinColumn(name="student_course_details_id"),
				inverseJoinColumns = @JoinColumn(name="assignment_id"))
	private List<Assignment> assignments;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)  
	@JoinColumn(name="grade_details_id")
	private GradeDetails gradeDetails;
	
	public StudentCourseDetails() {
		
	}

	



	public StudentCourseDetails(int studentId, int courseId, List<Assignment> assignments,
			GradeDetails gradeDetails) {
		this.studentId = studentId;
		this.courseId = courseId;
		this.assignments = assignments;
		this.gradeDetails = gradeDetails;
	}





	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public int getStudentId() {
		return studentId;
	}



	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}



	public int getCourseId() {
		return courseId;
	}



	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}



	public List<Assignment> getAssignments() {
		return assignments;
	}
	
	public List<Assignment> getAssignmentsByOrder() {
		Collections.sort(assignments);
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}
	
	public void addAssignment(Assignment assignment) {
		if(assignments == null) {
			assignments = new ArrayList<>();
		}
		assignments.add(assignment);
	}
	
	public Assignment getAssignmentById(int assignmentId) {
		for(Assignment a : assignments) {
			if(a.getId() == assignmentId) {
				return a;
			}
		}
		
		return null;
	}





	public GradeDetails getGradeDetails() {
		return gradeDetails;
	}





	public void setGradeDetails(GradeDetails gradeDetails) {
		this.gradeDetails = gradeDetails;
	}

	
	
	
	
	
}


