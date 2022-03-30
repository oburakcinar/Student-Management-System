package com.burak.studentmanagement.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.burak.studentmanagement.entity.Assignment;
import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.GradeDetails;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.AssignmentDetails;
import com.burak.studentmanagement.entity.StudentCourseDetails;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.AssignmentDetailsService;
import com.burak.studentmanagement.service.StudentCourseDetailsService;
import com.burak.studentmanagement.service.StudentService;


@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private CourseService courseService;
	
	
	@Autowired
	private StudentCourseDetailsService studentCourseDetailsService;
	
	@Autowired
	private AssignmentDetailsService assignmentDetailsService;
	
	@GetMapping("/{studentId}/courses")
	public String showStudentPanel(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId); //accessing student logged in
		List<Course> courses = student.getCourses();
		
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courses);
		return "student/student-courses";
	}
	
	@GetMapping("/{studentId}/courses/{courseId}")
	public String showStudentCourse(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		List<Course> courses = student.getCourses();
		Course course = courseService.findCourseById(courseId);
		StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(studentId, courseId);
		List<Assignment> assignments = studentCourseDetails.getAssignments();
		
		for(Assignment assignment : assignments) { //updating days remaining using helper method defined below
			int daysRemaining = findDayDifference(assignment);
			assignment.setDaysRemaining(daysRemaining);
		}
		
		
		GradeDetails gradeDetails = studentCourseDetails.getGradeDetails();
		
		theModel.addAttribute("assignments", assignments);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("student", student);
		theModel.addAttribute("gradeDetails", gradeDetails);
		
		return "student/student-course-detail";
	}
	
	@GetMapping("/{studentId}/courses/{courseId}/assignment/{assignmentId}")
	public String showStudentAssignment(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId, 
			@PathVariable("assignmentId") int assignmentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		List<Course> courses = student.getCourses();
		Course course = courseService.findCourseById(courseId);
		StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(studentId, courseId);
		Assignment assignment = studentCourseDetails.getAssignmentById(assignmentId);
		AssignmentDetails assignmentDetails = assignmentDetailsService.findByAssignmentAndStudentCourseDetailsId(assignmentId, studentCourseDetails.getId());
		
		theModel.addAttribute("assignment", assignment);
		theModel.addAttribute("assignmentDetails", assignmentDetails);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("student", student);
		
		return "student/student-assignment-detail";
	}
	
	
	@GetMapping("/{studentId}/courses/{courseId}/markAsCompleted/{assignmentId}")
	public String markAsCompleted(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId,
									@PathVariable("assignmentId") int assignmentId, Model theModel) {
		//Student student = studentService.findByStudentId(studentId);
		//Course course = courseService.findCourseById(courseId);
		StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(studentId, courseId);
		AssignmentDetails assignmentDetails = assignmentDetailsService
													.findByAssignmentAndStudentCourseDetailsId(assignmentId, studentCourseDetails.getId());
		assignmentDetails.setIsDone(1); //assignment is completed
		assignmentDetailsService.save(assignmentDetails);
		return "redirect:/student/" + studentId + "/courses/" + courseId + "/assignment/" + assignmentId;
	}
	
	//helper method to find day difference between assignment due date and today
	private int findDayDifference(Assignment assignment) {
		String dateString = assignment.getDueDate();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate dueDate = LocalDate.parse(dateString, dtf);
			LocalDate today = LocalDate.now();
			int dayDiff = (int) Duration.between(today.atStartOfDay(), dueDate.atStartOfDay()).toDays();
			
			return dayDiff;	
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return -1;
	}

}
