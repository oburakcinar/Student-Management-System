package com.burak.studentmanagement.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.burak.studentmanagement.entity.Assignment;
import com.burak.studentmanagement.entity.AssignmentDetails;
import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.GradeDetails;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.StudentCourseDetails;
import com.burak.studentmanagement.entity.Teacher;
import com.burak.studentmanagement.service.AssignmentDetailsService;
import com.burak.studentmanagement.service.AssignmentService;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.GradeDetailsService;
import com.burak.studentmanagement.service.StudentCourseDetailsService;
import com.burak.studentmanagement.service.TeacherService;


@Controller
@RequestMapping("/teacher")
public class TeacherController {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentCourseDetailsService studentCourseDetailsService;
	
	@Autowired
	private AssignmentDetailsService assignmentDetailsService;
	
	@Autowired
	private AssignmentService assignmentService;
	
	@Autowired
	private GradeDetailsService gradeDetailsService;
	
	@GetMapping("/{teacherId}/courses")
	public String showTeacherCourses(@PathVariable("teacherId") int teacherId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		List<Course> courses = teacher.getCourses();
		
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("courses", courses);
		return "teacher/teacher-courses";
	}
	
	@GetMapping("/{teacherId}/courses/{courseId}")
	public String showTeacherCourseDetails(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		List<Course> courses = teacher.getCourses();
		List<Student> students = course.getStudents();
		
		if(students.size() != 0) {
			List<Assignment> assignments = studentCourseDetailsService.findByStudentAndCourseId(students.get(0).getId(), courseId).getAssignments();
			for(Assignment assignment : assignments) {
				int daysRemaining = findDayDifference(assignment);
				assignment.setDaysRemaining(daysRemaining);
				assignmentService.save(assignment);
			}
			if(assignments.size() == 0) {
				assignments = null;
			}
			List<GradeDetails> gradeDetailsList = new ArrayList<>();
			for(Student student : students) {
				gradeDetailsList.add(studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId).getGradeDetails());
			}
			HashMap<List<Student>, List<GradeDetails>> studentGradeList = new HashMap<>();
			studentGradeList.put(students, gradeDetailsList);
			theModel.addAttribute("studentGradeList", studentGradeList);
			theModel.addAttribute("assignments", assignments);		
		} 
		
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("students", students);
		
		return "teacher/teacher-course-details";
	}
	
	
	@GetMapping("/{teacherId}/courses/{courseId}/editGrades")
	public String editGradesForm(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		List<Course> courses = teacher.getCourses();
		List<Student> students = course.getStudents();
		
		List<GradeDetails> gradeDetailsList = new ArrayList<>();
		for(Student student : students) {
			gradeDetailsList.add(studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId).getGradeDetails());
		}
		
		HashMap<List<Student>, List<GradeDetails>> studentGradeList = new HashMap<>();
		studentGradeList.put(students, gradeDetailsList);
		
		theModel.addAttribute("studentGradeList", studentGradeList);
		theModel.addAttribute("course", course);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("students", students);
		theModel.addAttribute("gradeDetailsList", gradeDetailsList);
		
		return "teacher/edit-grades-form";
	}
	
	
	@PostMapping("/{teacherId}/courses/{courseId}/editGrades/save/{gradeDetailsId}")
	public String modifyGrades(@ModelAttribute GradeDetails gradeDetails, 
			@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId,
			@PathVariable("gradeDetailsId") int gradeDetailsId) throws Exception {
		
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		//StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(gradeDetailsId, courseId);
		StudentCourseDetails studentCourseDetails = gradeDetailsService.findById(gradeDetailsId).getStudentCourseDetails();
		studentCourseDetails.setGradeDetails(gradeDetails);
		studentCourseDetailsService.save(studentCourseDetails);
		gradeDetailsService.deleteById(gradeDetailsId);
		//gradeDetailsService.save(gradeDetails);
		
	    return "redirect:/teacher/" + teacherId + "/courses/" + courseId;
	}
	
	
	@GetMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}")
	public String showAssignmentDetails(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId, Model theModel) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		List<Course> courses = teacher.getCourses();
		//
		
		List<Assignment> assignments = new ArrayList<>();
		List<StudentCourseDetails> studentCourseDetails = new ArrayList<>();
		List<AssignmentDetails> studentCourseAssignmentDetails = new ArrayList<>();
		List<String> assignmentStatuses = new ArrayList<>();
		
		for(Student student : students) {
			AssignmentDetails studentCourseAssignmentDetail = assignmentDetailsService.
					findByAssignmentAndStudentCourseDetailsId(assignmentId, studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId).getId());
			studentCourseAssignmentDetails.add(studentCourseAssignmentDetail);
			if(studentCourseAssignmentDetail.getIsDone() == 0) {
				assignmentStatuses.add("incomplete");
			} else {
				assignmentStatuses.add("completed");
			}
		}
				
		HashMap<List<Student>, List<String>> list = new HashMap<>();
		list.put(students, assignmentStatuses);
		
		theModel.addAttribute("list", list);
		theModel.addAttribute("assignmentDetails", studentCourseAssignmentDetails);
		theModel.addAttribute("students", students);
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("teacher", teacher);
		
		return "teacher/teacher-assignment-status";
	}
	
	
	
	@GetMapping("/{teacherId}/courses/{courseId}/assignments/{assignmentId}/delete")
	public String deleteAssignment(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId,
			@PathVariable("assignmentId") int assignmentId) {
		assignmentService.deleteAssignmentById(assignmentId);
		
		return "redirect:/teacher/" + teacherId + "/courses/" + courseId;
	}
	
	@GetMapping("/{teacherId}/courses/{courseId}/addNewAssignment")
	public String addNewAssignment(@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		Assignment assignment = new Assignment();
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		List<Course> courses = teacher.getCourses();
		
		theModel.addAttribute("assignment", assignment);
		theModel.addAttribute("teacher", teacher);
		theModel.addAttribute("course", courseService.findCourseById(courseId));
		theModel.addAttribute("courses", courses);
		
		return "teacher/assignment-form";
	}
	
	@PostMapping("/{teacherId}/courses/{courseId}/addNewAssignment/save")
	public String saveAssignment(@Valid @ModelAttribute("assignment") Assignment assignment, BindingResult theBindingResult, 
			@PathVariable("teacherId") int teacherId, @PathVariable("courseId") int courseId, Model theModel) {
		
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		List<Course> courses = teacher.getCourses();
		
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("teacher", teacher);
			theModel.addAttribute("courses", courses);
			theModel.addAttribute("course", courseService.findCourseById(courseId));
			return "teacher/assignment-form";
		}
		
		assignment.setDaysRemaining(findDayDifference(assignment));
		assignmentService.save(assignment);
		
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		
		for(Student student : students) {
			StudentCourseDetails studentCourseDetails = studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId);
			AssignmentDetails assignmentDetail = new AssignmentDetails();
			assignmentDetail.setAssignmentId(assignment.getId());
			assignmentDetail.setStudentCourseDetailsId(studentCourseDetails.getId());
			assignmentDetail.setIsDone(0);
			assignmentDetailsService.save(assignmentDetail);
		}
		
		
		theModel.addAttribute("teacher", teacher);
		
		return "redirect:/teacher/" + teacherId + "/courses/" + courseId;
	}
	
	
	
	
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




