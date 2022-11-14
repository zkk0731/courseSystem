package com.example.demo2.vo;

import java.util.List;

import com.example.demo2.entity.Course;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseRes {

	private Course course;

	@JsonProperty("student_name")
	private String studentName;
	@JsonProperty("student_id")
	private String studentId;
	@JsonProperty("course_id")
	private String courseId;
	@JsonProperty("total_credit")
	private Integer totlaCredit;

	private String message;
	@JsonProperty("course_list")
	private List<Course> courseList;
	@JsonProperty("message_list")
	private List<String> messageList;

	public CourseRes() {

	}

	public CourseRes(Course course) {
		this.course = course;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	public Integer getTotlaCredit() {
		return totlaCredit;
	}

	public void setTotlaCredit(Integer totlaCredit) {
		this.totlaCredit = totlaCredit;
	}

	public CourseRes(String studentId, String studentName) {
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
