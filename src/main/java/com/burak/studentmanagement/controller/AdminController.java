package com.burak.studentmanagement.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import com.burak.studentmanagement.entity.Assignment;
import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.GradeDetails;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.StudentCourseDetails;
import com.burak.studentmanagement.entity.Teacher;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.GradeDetailsService;
import com.burak.studentmanagement.service.StudentCourseDetailsService;
import com.burak.studentmanagement.service.StudentService;
import com.burak.studentmanagement.service.TeacherService;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentService studentService;
	
	
	@Autowired
	private StudentCourseDetailsService studentCourseDetailsService;
	
	@Autowired
	private GradeDetailsService gradeDetailsService;
	
	private int teacherDeleteErrorValue; //used for deleting teacher, 0 means the teacher has not any assigned courses, 1 means it has
	
	@GetMapping("/adminPanel")
	public String showAdminPanel() {
		
		return "admin/admin-panel";
	}
	
	@GetMapping("/adminInfo")
	public String showAdminInfo(Model theModel) {
		int courseSize = courseService.findAllCourses().size();
		theModel.addAttribute("courseSize", courseSize);
		int studentSize = studentService.findAllStudents().size();
		theModel.addAttribute("studentSize", studentSize);
		int teacherSize = teacherService.findAllTeachers().size();
		theModel.addAttribute("teacherSize", teacherSize);
		return "admin/admin-info";
	}
	
	@GetMapping("/students")
	public String showStudentList(Model theModel) {
		theModel.addAttribute("students", studentService.findAllStudents());
		
		return "admin/student-list"; 
	}
	
	@RequestMapping("/students/delete")
	public String deleteStudent(@RequestParam("studentId") int studentId) {
		List<StudentCourseDetails> list = studentCourseDetailsService.findByStudentId(studentId);
		for(StudentCourseDetails scd : list) { //deleting the student grades before deleting the student 
			int gradeId = scd.getGradeDetails().getId();
			studentCourseDetailsService.deleteByStudentId(studentId);
			gradeDetailsService.deleteById(gradeId);
		}
		studentService.deleteById(studentId);
		
		return "redirect:/admin/students";
	}
	
	@GetMapping("/students/{studentId}/courses")
	public String editCoursesForStudent(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		List<Course> courses = student.getCourses();
		
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courses);
		
		return "admin/student-course-list";
	}
	
	@GetMapping("/students/{studentId}/addCourse")
	public String addCourseToStudent(@PathVariable("studentId") int studentId, Model theModel) {
		Student student = studentService.findByStudentId(studentId);
		List<Course> courses = courseService.findAllCourses();
		
		for(int i = 0; i < courses.size(); i++) { //finding the courses that the current student has not enrolled yet
			if(student.getCourses().contains(courses.get(i))) {
				courses.remove(i);
				i--;
			}
		}
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courses); //unenrolled courses are displayed as drop-down list
		theModel.addAttribute("listSize", courses.size());
		return "admin/add-course";
	}
	
	@RequestMapping("/students/{studentId}/addCourse/save")
	public String saveCourseToStudent(@PathVariable("studentId") int studentId, @RequestParam("courseId") int courseId) {
		
		StudentCourseDetails sc = new StudentCourseDetails(studentId, courseId, new ArrayList<Assignment>() ,new GradeDetails());
		studentCourseDetailsService.save(sc);
			
		return "redirect:/admin/students/" + studentId + "/courses";
	}
	
	
	@GetMapping("/students/{studentId}/courses/delete/{courseId}")
	public String deleteCourseFromStudent(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId) {
		StudentCourseDetails scd = studentCourseDetailsService.findByStudentAndCourseId(studentId, courseId);
		int gradeId = scd.getGradeDetails().getId();
		
		//operations for removing the student from the course
		studentCourseDetailsService.deleteByStudentAndCourseId(studentId, courseId);
		gradeDetailsService.deleteById(gradeId);
		
		return "redirect:/admin/students/" + studentId + "/courses";
	}
	
	
	@GetMapping("/teachers")
	public String showTeacherList(Model theModel) {
		theModel.addAttribute("teachers", teacherService.findAllTeachers());
		theModel.addAttribute("error", teacherDeleteErrorValue); 
		teacherDeleteErrorValue = 0; //0 means the teacher has not any assigned courses, 1 means it has
		return "admin/teacher-list";
	}
	
	@GetMapping("/teachers/delete")
	public String deleteTeacher(@RequestParam("teacherId") int teacherId) {
		Teacher teacher = teacherService.findByTeacherId(teacherId);
		if(teacher.getCourses().size() == 0) { //if the teacher has courses assigned, the teacher cannot be deleted
			teacherService.deleteTeacherById(teacherId);
			teacherDeleteErrorValue = 0;
		} else {
			teacherDeleteErrorValue = 1; 
		}
		
		return "redirect:/admin/teachers";
	}
	
	
	@GetMapping("/addCourse")
	public String addCourse(Model theModel) {
		//add course form has a select teacher field where all teachers registered are showed as drop-down list
		List<Teacher> teachers = teacherService.findAllTeachers(); 
		
		theModel.addAttribute("course", new Course());
		theModel.addAttribute("teachers", teachers);
		
		return "admin/course-form";
	}
	
	@PostMapping("/saveCourse")
	public String saveCourse(@Valid @ModelAttribute("course") Course theCourse, 
			BindingResult theBindingResult, @RequestParam("teacherId") int teacherId, Model theModel) {
		
		if (theBindingResult.hasErrors()) { //course form has data validation rules. If fields are not properly filled out, form is showed again
			List<Teacher> teachers = teacherService.findAllTeachers();
			theModel.addAttribute("teachers", teachers);
			return "admin/course-form";
		}
		
		theCourse.setTeacher(teacherService.findByTeacherId(teacherId)); //setTeacher method also sets the teacher's course as this	
		courseService.save(theCourse);
		
		return "redirect:/admin/adminPanel"; 
	}
	
	@GetMapping("/courses")
	public String showCourses(Model theModel) {
		theModel.addAttribute("courses", courseService.findAllCourses());	
		
		return "admin/course-list";
	}
	
	
	@GetMapping("/courses/delete")
	public String deleteCourse(@RequestParam("courseId") int courseId) {		
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		
		for(Student student : students) {
			StudentCourseDetails scd = studentCourseDetailsService.findByStudentAndCourseId(student.getId(), courseId);
			int gradeId = scd.getGradeDetails().getId();
			studentCourseDetailsService.deleteByStudentAndCourseId(student.getId(), courseId);
			gradeDetailsService.deleteById(gradeId);
		}
		
		courseService.deleteCourseById(courseId);
		return "redirect:/admin/courses";
	}
	
	@GetMapping("/courses/{courseId}/students")
	public String showSudents(@PathVariable("courseId") int courseId, Model theModel) {		
		Course course = courseService.findCourseById(courseId);
		List<Student> students = course.getStudents();
		Teacher teacher = course.getTeacher();
		theModel.addAttribute("students", students);
		theModel.addAttribute("course", course);
		theModel.addAttribute("teacher", teacher);
		return "admin/course-student-list";
	}
	
	
	
	@GetMapping("/courses/{courseId}/students/delete")
	public String deleteStudentFromCourse(@PathVariable("courseId") int courseId, @RequestParam("studentId") int studentId) {
		StudentCourseDetails scd = studentCourseDetailsService.findByStudentAndCourseId(studentId, courseId);
		int gradeId = scd.getGradeDetails().getId();
		
		studentCourseDetailsService.deleteByStudentAndCourseId(studentId, courseId);
		gradeDetailsService.deleteById(gradeId);
		
		return "redirect:/admin/courses/" + courseId + "/students";
	}
	
	@GetMapping("/courses/{courseId}/students/addStudent")
	public String addStudentToCourse(@PathVariable("courseId") int courseId, Model theModel) {
		Course course = courseService.findCourseById(courseId);
		List<Student> students = studentService.findAllStudents();
		
		for(int i = 0; i < students.size(); i++) { 
			if(course.getStudents().contains(students.get(i))) {
				students.remove(students.get(i));
				i--;
			}
		}
		theModel.addAttribute("students", students); //all students who are not enrolled to the current course yet
		theModel.addAttribute("course", course);
		theModel.addAttribute("listSize", students.size());
		return "admin/add-student";
		
	}
	
	@RequestMapping("/courses/{courseId}/students/addStudent/save")
	public String saveStudentToCourse(@RequestParam("studentId") int studentId, @PathVariable("courseId") int courseId) {
		
		StudentCourseDetails sc = new StudentCourseDetails(studentId, courseId, new ArrayList<Assignment>() ,new GradeDetails());
		studentCourseDetailsService.save(sc);
		
		return "redirect:/admin/courses/" + courseId + "/students";
	}
}
