package com.example.demo2.vo;

import java.util.List;

import com.example.demo2.entity.Course;
import com.example.demo2.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseRes {

	private Course course;
	
	private String studentName;
	
	private String studentId;
	
	private String courseId;
	private String message;
	
	private List<Course> courseList;

	public CourseRes() {
		
	}
	public CourseRes(Course course) {
		this.course = course;
	}
	
	public CourseRes(String studentId,String studentName) {
		this.studentId = studentId;
		this.studentName = studentName;
	}
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public List<Course> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
