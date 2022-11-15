package com.example.coursesystem.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseReq {

	@JsonProperty("student_name")
	private String studentName;
	@JsonProperty("student_id")
	private String studentId;

	private String id;

	private String name;

	private Integer day;

	private Integer start;

	private Integer end;

	private Integer credit;

	@JsonProperty("course_list")
	private List<String> courseList;

	private List<String> ids;

	private List<String> names;

	public CourseReq() {

	}
//	public CourseReq(String id,String name,int day,int start,int end,int credit) {
//		this.id = id;
//	}

	public String getId() {
		return id;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<String> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<String> courseList) {
		this.courseList = courseList;
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

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

}
